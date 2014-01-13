package frantarye;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSMail {
	private String url;
	private String mailbox;
	
	Context jndiContext;
	private QueueConnection connection;
	private QueueSession session;
	private Queue queue;
	private QueueBrowser browser;
	private QueueSender sender;
	
	public JMSMail(String mailbox, String url) {
		this.url = url;
		this.mailbox = mailbox;
	}
	
	public void sendMail(String mailbox, String message) throws NamingException, JMSException {
		queue = (Queue)jndiContext.lookup(url + "/" + mailbox);
		sender = session.createSender(queue);
		
		TextMessage msg = session.createTextMessage(message);
		sender.send(msg);
	}
	
	public ArrayList<String> retrieveMails() throws JMSException {
		ArrayList<String> mails = new ArrayList<>();
		
		Enumeration messages = browser.getEnumeration();
		while(messages.hasMoreElements()) {
			TextMessage message = (TextMessage)messages.nextElement();
			if(message.getText() != null) {
				mails.add(message.getText()); // save text of mail
				message.acknowledge();
			}
		}
		
		return mails;
	}
	
	/**
	 * Connects this participant to the in 'subject' specified chatroom.
	 * @throws JMSException JMSException thrown if a JMSException occurred
	 * @throws NamingException 
	 */
	public void connect() throws JMSException, NamingException {
		Context jndiContext = new InitialContext();
		QueueConnectionFactory factory = (QueueConnectionFactory)jndiContext.lookup(url + "/QueueConnectionFactory");
		
		queue = (Queue)jndiContext.lookup(url + "/" + mailbox);
		
		connection = factory.createQueueConnection();
		session =  connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		
		browser = session.createBrowser(queue);	
	}
	/**
	 * Disconnects this participant from the in 'subject' specified chatroom. Automatically stops receiving messages.
	 * @throws JMSException JMSException thrown if a JMSException occurred
	 */
	public void disconnect() throws JMSException {
		browser.close();
		session.close();
		connection.close();
	}
}
