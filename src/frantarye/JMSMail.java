package frantarye;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSMail {
	private String url;
	private String mailbox;
	
	Context jndiContext;
	private Connection connection;
	private Session session;
	private Queue queue;
	private QueueBrowser browser;
	private MessageProducer producer;
	
	public JMSMail(String mailbox, String url) {
		this.url = url;
		this.mailbox = mailbox;
	}
	
	public void sendMail(String mailbox, String message) throws NamingException, JMSException {
		Queue queue_to_send = session.createQueue(mailbox);
		
		producer = session.createProducer(queue_to_send);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		TextMessage msg = session.createTextMessage(message);
		producer.send(msg);
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
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		connection = connectionFactory.createConnection();
		connection.start();
		
		// Create the session
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		queue = session.createQueue(mailbox);
		
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
