package frantarye;

import java.util.ArrayList;

import org.apache.activemq.command.Message;

public class JMSMail {
	private String mailbox;
	
	public JMSMail(String mailbox) {		
	}
	
	public void sendMail(String mailbox, Message message) {
	}
	
	public ArrayList<String> retrieveMails() {
		return null;
	}
}
