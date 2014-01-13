package frantarye;

import org.apache.activemq.ActiveMQConnection;

/**
 * @author Gary Ye
 * @version 13.01.2013
 * Objects of this 
 */
public class JMSUser extends Thread {
	public static String URL = "";
	
	private JMSChat chat;
	private JMSMail mail;
	
	private String ip;
	private String username;
	
	public JMSUser(String ip_message_broker, String username, String subject) {
		
	}
	
	@Override
	public void run() {
		
	}
	
	public static void main(String[] args) {
		
	}
}
