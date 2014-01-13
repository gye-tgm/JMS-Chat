package frantarye;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * This class is an Interface to the given mailbox. It also provides a method to send a mail to someone else's mailbox.
 * @author Elias Frantar
 * @version 13.1.14
 */
public class JMSMail {
	/**
	 * The error to print when catching JMSExceptions thrown by this class.
	 */
	public static final String JMS_ERROR = "A JMS error occured!: "; // the error to print when catching Exceptions
	
	private String url; // URL to the server
	private String mailbox; // name of you own mailbox
	
	Context jndiContext;
	private Connection connection;
	private Session session;
	private Queue queue;
	private QueueBrowser browser;
	private MessageProducer producer;
	
	/**
	 * Creates a new mailbox interface to the given mailbox
	 * @param mailbox the name of the mailbox
	 * @param url the URL to the mailbox
	 */
	public JMSMail(String mailbox, String url) {
		this.url = url;
		this.mailbox = mailbox;
	}
	
	/**
	 * Sends a message to the given mailbox. connect() must haven been called before!
	 * @param mailbox the name of the mailbox to send to
	 * @param message the message to send
	 * @throws NamingException thrown if a NamingException occurred
	 */
	public void sendMail(String mailbox, String message) throws JMSException {
		Queue queue_to_send = session.createQueue(mailbox);
		
		producer = session.createProducer(queue_to_send);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		TextMessage msg = session.createTextMessage(message);
		producer.send(msg);
	}
	
	/**
	 * Retrieves all mails from your own mailbox. (also includes mails you have already view some time before) connect() must have been called before!
	 * @return a list all received mails
	 * @throws JMSException thrown if a JMSException occurred
	 */
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
	 * Connects this mailbox. Creates a new one if necessary
	 * @throws JMSException JMSException thrown if a JMSException occurred
	 */
	public void connect() throws JMSException{
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		connection = connectionFactory.createConnection();
		connection.start();
		
		// Create the session
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		queue = session.createQueue(mailbox);
		
		browser = session.createBrowser(queue);
	}
	/**
	 * Disconnects from this mailbox. Afterwards reading and sending is not possible anymore.
	 * @throws JMSException JMSException thrown if a JMSException occurred
	 */
	public void disconnect() throws JMSException {
		browser.close();
		session.close();
		connection.close();
	}
}
