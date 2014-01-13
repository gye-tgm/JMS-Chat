package frantarye;

import org.apache.activemq.ActiveMQConnection;

public class JMSUser extends Thread {
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
