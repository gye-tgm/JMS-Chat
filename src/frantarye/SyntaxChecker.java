package frantarye;

/**
 * @author Gary Ye
 * @version 13.01.2013 This class offers a syntax check method, which can be
 *          called statically.
 */
public class SyntaxChecker {
	/*
	 * private static final String ERROR_1 = ""; private static final String
	 * ERROR_2 = ""; private static final String ERROR_3 = ""; private static
	 * final String ERROR_4 = "";
	 */

	public enum InputType {
		MESSAGE_SEND, MAIL_SEND, MAILBOX, ERROR
	}

	/**
	 * Checks if the input of the user is valid and detects the type of the
	 * command.
	 * 
	 * @param input
	 *            the input string
	 * @return the type of the command
	 */
	public static InputType checkInput(String input) {
		if (input.isEmpty())
			return InputType.ERROR;
		String[] split = input.split(" ");
		switch (split[0]) {
		case "MAIL":
			if (split.length >= 3)
				return InputType.MAIL_SEND;
			else {
				System.err.println("usage: MAIL <ip_partner> <nachricht>");
				return InputType.ERROR;
			}
		case "MAILBOX":
			if (split.length == 1)
				return InputType.MAILBOX;
			else {
				System.err.println("usage: MAILBOX");
				return InputType.ERROR;
			}
		}
		// Everything else will be recognized as a normal chat message
		return InputType.MESSAGE_SEND;
	}
}
