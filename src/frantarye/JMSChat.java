package frantarye;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Connection;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * This class is a participant in a given JMS chatroom 'subject'. It manages the connection and sending and receiving messages.
 * @author Elias Frantar
 * @version 13.1.2013
 */
public class JMSChat extends Thread {
	/**
	 * The error to print when catching JMSExceptions thrown by this class.
	 */
	public static final String JMS_ERROR = "A JMS error occured!: "; // the error to print when catching Exceptions
	
	private String url;
	private String subject;
	private boolean connected; // to verify if disconnecting was on purpose or accidently when catching Exceptions
	
	private Connection connection;
	private Session session;
	private Destination destination;
	
	private MessageProducer producer; // sends messages
	private MessageConsumer consumer; // receives messages
	
	/**
	 * Creates a new participant in the chatroom 'subject' in the JMS 'url'
	 * @param subject the name of the chatroom (JMS-topic)
	 * @param url the URL to the JMS, required for establishing a connection
	 */
	public JMSChat(String subject, String url) {
		this.subject = subject;
		connected = false;
	}
	
	/**
	 * Starts receiving messages. Receiving automatically terminates when calling disconnect(). connect() must be called beforehand.
	 */
	public void startReceiving() {
		start();
	}
	@Override
	public void run() {
		try {
			while(true) {
				TextMessage message = (TextMessage)consumer.receive();
				
				if ( message != null ) {
			      	System.out.println(message.getText());
			      	message.acknowledge();
			    }
			}
		}
		catch(JMSException e) {
			if(connected)
				System.out.println(JMS_ERROR + e.getMessage());
		}
	}
	
	/**
	 * Boradcasts the given message in this chatroom. connect() must be called beforehand
	 * @param username name of the message sender
	 * @param ip IP-address of the sender
	 * @param message the message to send
	 * @throws JMSException thrown if a JMSException occurred
	 */
	public void sendMessage(String username, String ip, String message) throws JMSException {
		TextMessage message_to_send = session.createTextMessage(username + " " +  ip + ": " + message);
		producer.send(message_to_send);
	}
	
	/**
	 * Connects this participant to the in 'subject' specified chatroom.
	 * @throws JMSException JMSException thrown if a JMSException occurred
	 */
	public void connect() throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		connection = connectionFactory.createConnection();
		connection.start();
		
		// Create the session
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createTopic(subject);
		
		producer = session.createProducer(destination);
		producer.setDeliveryMode( DeliveryMode.NON_PERSISTENT );
		
		consumer = session.createConsumer(destination);
		
		connected = true;
	}
	/**
	 * Disconnects this participant from the in 'subject' specified chatroom. Automatically stops receiving messages.
	 * @throws JMSException JMSException thrown if a JMSException occurred
	 */
	public void disconnect() throws JMSException {
		connected = false; // we may throw Exceptions when closing, the user should not get notified by these 
		
		producer.close();
		consumer.close();
		session.close();
		connection.close();
	}
}
