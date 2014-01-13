package frantarye;

public class SyntaxChecker {
	/*
	private static final String ERROR_1 = "";
	private static final String ERROR_2 = "";
	private static final String ERROR_3 = "";
	private static final String ERROR_4 = "";
	*/
	
	public enum InputType {
		MESSAGE_SEND,
		MAIL_SEND,
		MAILBOX,
		ERROR
	}
	
	public static InputType checkInput(String input) {
		return InputType.ERROR;
	}
}
