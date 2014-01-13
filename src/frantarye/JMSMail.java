package frantarye;

import java.util.ArrayList;

import org.apache.activemq.command.Message;

public class JMSMail {
	private String url;
	private String mailbox;
	
	public JMSMail(String mailbox, String url) {		
	}
	
	public void sendMail(String mailbox, String message) {
	}
	
	public ArrayList<String> retrieveMails() {
		return null;
	}
}
