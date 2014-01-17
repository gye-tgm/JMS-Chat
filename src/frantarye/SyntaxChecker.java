package frantarye;

/**
 * @author Gary Ye
 * @version 13.01.2013 This class offers a syntax check method, which can be
 *          called statically.
 */
public class SyntaxChecker {
	public enum InputType {
		MESSAGE_SEND, MAIL_SEND, MAILBOX, EMPTY, MAIL_ERROR, MAILBOX_ERROR, EXIT
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
			return InputType.EMPTY;
		String[] split = input.split(" ");
		switch (split[0]) {
		case "MAIL":
			return (split.length >= 3 ? InputType.MAIL_SEND : InputType.MAIL_ERROR);
		case "MAILBOX":
			return split.length == 1 ? InputType.MAILBOX : InputType.MAILBOX_ERROR;
		case "EXIT":
			return InputType.EXIT;	
		}
		// Everything else will be recognized as a normal chat message
		return InputType.MESSAGE_SEND;
	}
}
