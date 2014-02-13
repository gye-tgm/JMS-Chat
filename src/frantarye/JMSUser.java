package frantarye;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.jms.JMSException;

/**
 * @author Gary Ye
 * @version 13.01.2013 The user class runs the logic of this application. It
 *          reads the message from the standard input and handles it
 *          correspondingly. It also runs a thread in the background, that
 *          receives the messages, which will be printed in the standard output.
 */
public class JMSUser extends Thread {
	private JMSChat chat;
	private JMSMail mail;

	private String ip;
	private String username;

	/**
	 * Constructs a new user.
	 * 
	 * @param ip_message_broker
	 *            the IP address of the message broker
	 * @param username
	 *            the user name of this user
	 * @param subject
	 *            the subject of the chat room
	 * @param iface
	 * 			  the name of the iface to use the IP-address from
	 */
	public JMSUser(String ip_message_broker, String username, String subject, String iface) {
		// Own attributes
		this.username = username;
		
		try {
			ip = getIp(iface);	
		} catch (Exception e) {
			System.err.println("The given interface does not exist!\nException: " + e.getMessage());
			System.exit(1);
		}
		
		// Chat message
		String url = String.format("tcp://%s:61616", ip_message_broker);
		System.out.println(url);
		System.out.println(ip);
		chat = new JMSChat(subject, url);
		// Mail Box
		mail = new JMSMail(ip, url);
	}

	/**
	 * Reads the commands from the standard input and handles it
	 * correspondingly.
	 */
	@Override
	public void run() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String inputLine;
		
		mail.connect();
		
		try {
			chat.connect();
		} catch (JMSException e1) {
			System.out.println("Could not connect to the server");
			System.exit(1);
			// e1.printStackTrace();
		}
		
		chat.startReceiving(); // Start listening
		boolean done = false;
		try {
			while (!done && (inputLine = in.readLine()) != null) {
				SyntaxChecker.InputType type = SyntaxChecker
						.checkInput(inputLine);
				switch (type) {
				case EMPTY:
					break;
				case MAILBOX_ERROR:
					System.err.println("usage: MAILBOX");
					break;
				case MAIL_ERROR:
					System.err.println("usage: MAIL <ip_partner> <nachricht>");
					break;
				case EXIT:
					done = true;// Will quit the while loop
					break;
				case MAIL_SEND:
					String[] split = inputLine.split(" ");
					StringBuffer msg = new StringBuffer("");
					for(int i = 2; i < split.length; i++) {
						if(i > 2) msg.append(' ');
						msg.append(split[i]);
					}
					mail.sendMail(split[1], msg.toString());
					break;
				case MAILBOX:
					ArrayList<String> mails = null;
					mails = mail.retrieveMails();
					for (String m : mails)
						System.out.println(m);
					break;
				case MESSAGE_SEND:
					try {
						chat.sendMessage(username, ip, inputLine);
					} 
					catch (JMSException e) {
						System.err.println(JMSChat.JMS_ERROR + "\n" + e.getMessage());
					}
					break;
				}
			}
		}
		catch (IOException e) {
			System.err.println("IOException occured!\n");
		}
	}
	
	/**
	 * Returns the ipv4 address of the given network interface (if exists)
	 * @param ifaceName the name of the interface
	 * @return the ipv4 address of this interface;
	 * @throws SocketException thrown if a SocketException occurred
	 * @throws UnknownHostException 
	 */
	public String getIp(String ifaceName) throws SocketException, UnknownHostException {	    
		NetworkInterface iface = NetworkInterface.getByName(ifaceName);
		Enumeration<InetAddress> ee = iface.getInetAddresses();
		while(ee.hasMoreElements()) {
			String ip = ee.nextElement().getHostAddress().toString();
			if(ip.contains(".")) // if the address contains a '.' it is a ipv4 one
					return ip;
		}
		if(ip == null)
			throw new UnknownHostException(); // interface does not exist
		
		return ip; // if there is no ipv4 just return the ipv6
	}

	public static void main(String[] args) {
		if (args.length != 4) {
			System.err.println("usage: vsdbchat <ip_message_broker> <username> <chatroom> <network interface>");
		} else {
			new JMSUser(args[0], args[1], args[2], args[3]).start();
		}
	}
}
