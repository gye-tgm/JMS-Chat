package frantarye;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

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
	 */
	public JMSUser(String ip_message_broker, String username, String subject) {
		// Own attributes
		this.username = username;
		try {
			ip = Inet4Address.getLocalHost().getHostAddress();	
		} catch (UnknownHostException e) {
			System.err.println("The localhost is unknown.");
			System.exit(1);
		}
		
		// Chat message
		String url = String.format("tcp://%s:61616", ip_message_broker);
		System.out.println(url);
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
		
		try {
			chat.connect();
		} catch (JMSException e1) {
			e1.printStackTrace();
		}
		
		chat.startReceiving(); // Start listening

		try {
			while ((inputLine = in.readLine()) != null) {
				SyntaxChecker.InputType type = SyntaxChecker
						.checkInput(inputLine);
				switch (type) {
				case ERROR:
					System.err.println("Unknown error\n");
					break;
				case MAIL_SEND:
					String[] split = inputLine.split(" ");
					// MAIL mailbox, message is the rest of the line
					mail.sendMail(split[1],
							Arrays.copyOfRange(split, 2, split.length)
									.toString());
					break;
				case MAILBOX:
					ArrayList<String> mails = null;
					try {
						mails = mail.retrieveMails();
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
					for (String m : mails)
						System.out.println(m);
					break;
				case MESSAGE_SEND:
					try {
						chat.sendMessage(username, ip, inputLine);
					} catch (JMSException e) {
						System.err.println(JMSChat.JMS_ERROR + "\n" + e.getMessage());
					}
					break;
				}
			}
		} catch (IOException e) {
			System.err.println("IOException occured!\n");
		}
	}

	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("usage: vsdbchat <ip_message_broker> <username> <chatroom>");
		} else {
			new JMSUser(args[0], args[1], args[2]).start();
		}
	}
}
