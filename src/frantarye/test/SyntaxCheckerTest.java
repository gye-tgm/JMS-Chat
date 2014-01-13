/**
 * 
 */
package frantarye.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
	public void testCheckInput1() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("");
		assertEquals(type, InputType.ERROR);
	}
	
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckInput2() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("aaaaaaaaaaaaaaaaa");
		assertEquals(type, InputType.MESSAGE_SEND);
	}
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckInput3() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("The quick brown fox jumps over the lazy dog");
		assertEquals(type, InputType.MESSAGE_SEND);
	}
	
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckInputMailbox1() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("MAILBOX");
		assertEquals(type, InputType.MAILBOX);
	}

	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckInputMailbox2() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput(" MAILBOX");
		assertEquals(type, InputType.MESSAGE_SEND);
	}
	
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckInputMailbox3() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("MAILBOX  ");
		assertEquals(type, InputType.MAILBOX);
	}
	
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckInputMailbox4() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("mailbox ");
		assertEquals(type, InputType.MESSAGE_SEND);
	}
	
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckMail1() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("MAIL 192.168.163.129 juhu :)");
		assertEquals(type, InputType.MAIL_SEND);
	}
	
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckMail2() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("MAIL 192.168.163.129");
		assertEquals(type, InputType.ERROR);
	}
	
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckMail3() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("MAIL DNS ist Cool");
		assertEquals(type, InputType.MAIL_SEND);
	}
	
	/**
	 * Test method for {@link frantarye.SyntaxChecker#checkInput(java.lang.String)}.
	 */
	@Test
	public void testCheckMail4() {
		SyntaxChecker.InputType type = SyntaxChecker.checkInput("MAIL DNS ist Cool");
		assertEquals(type, InputType.MAIL_SEND);
	}
	
	

}