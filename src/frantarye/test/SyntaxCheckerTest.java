/**
 * 
 */
package frantarye.test;

import static org.junit.Assert.*;

import org.junit.Test;

import frantarye.SyntaxChecker;
import frantarye.SyntaxChecker.InputType;

/**
 * @author Gary Ye
 * @version 13.01.2013
 * Tests against various possible inputs.
 */
public class SyntaxCheckerTest {

	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckInputEmpty() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("");
		assertEquals(type, InputType.ERROR);
	}
	
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckInputOneWord() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("Fox");
		assertEquals(type, InputType.MESSAGE_SEND);
	}
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckInputMoreWords() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("The quick brown fox jumps over the lazy dog");
		assertEquals(type, InputType.MESSAGE_SEND);
	}
	
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckInputMailboxOnly() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("MAILBOX");
		assertEquals(type, InputType.MAILBOX);
	}

	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckInputWhitespaceMailbox() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput(" MAILBOX");
		assertEquals(type, InputType.MESSAGE_SEND);
	}
	
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckInputMailboxWhitespaces() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("MAILBOX  ");
		assertEquals(type, InputType.MAILBOX);
	}
	
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckInputMailboxSpelledWrong() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("mailbox ");
		assertEquals(type, InputType.MESSAGE_SEND);
	}
	
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckMailCorrectMessage() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("MAIL 192.168.163.129 juhu :)");
		assertEquals(type, InputType.MAIL_SEND);
	}
	
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckMailNoMessage() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("MAIL 192.168.163.129");
		assertEquals(type, InputType.ERROR);
	}
	
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckMailDNSInsteadOfIPMessage() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("MAIL DNS ist Cool");
		assertEquals(type, InputType.MAIL_SEND);
	}

}