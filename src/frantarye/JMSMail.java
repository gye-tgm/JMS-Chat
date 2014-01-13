package frantarye;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.Message;

public class JMSMail {
	private String url;
	private String mailbox;
	
	private Connection connection;
	private Session session;
	private Queue queue;
	private QueueBrowser browser;
	private QueueSender sender;
	
	public JMSMail(String mailbox, String url) {
		this.url = url;
		this.mailbox = mailbox;
	}
	
	public void sendMail(String mailbox, String message) {
		
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
	 */
	public void connect() throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		connection = connectionFactory.createConnection();
		connection.start();
		
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
