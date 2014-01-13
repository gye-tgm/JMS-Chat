package frantarye;

public class JMSChat extends Thread {
	private String url;
	private String subject;
	
	public JMSChat(String subject, String url) {
		this.subject = subject;
	}
	
	@Override
	public void run() {
		
	}
	
	public void sendMessage(String username, String ip, String message) {
		
	}
	
}
