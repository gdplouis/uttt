package com.uttt.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * A JUnit test augmenter that validates an expected exception (ala {@code @Test(expected=ExType.class)}),
 * but also verifies the exception's message detail. Mostly, this foofalah is needed because of the iteraction
 * with standard JUnit stack filtering and using {@code junit.Assert.*} methods in a test that
 * has an expected exception by way of the {@code @Test(expected=...)} annotation.
 *
 * <P>How to use:
 * <PRE>
 * {@literal @}Test
 * public void <B><I>descriptiveMethodName</I></B>() {
 *     TestExceptionValidator.validate(<B><I>Exception</I></B>.class,
 *         <B><I>"Abc[=123]: lorem ipsum",</I></B>
 *         new TestExceptionValidator.Trigger() {
 *             {@literal @}Override
 *             public void action() {
 *                 <B><I>// simple code triggering expected exception & message, above</I></B>
 *             }
 *         }
 *     );
 * }</PRE>
 */
public interface TestExceptionValidator {

	/**
	 * Client code will annonymously extend the {@code Trigger} class and define it's single abstract
	 * method, {@code action()}. Typically, this will be done within the argument list of a call to
	 * {@link com.uttt.common.TestExceptionValidator.validate(Class<?>, String, Trigger) validate}
	 *
	 */
	public interface Trigger {
		public void action();
	}

	/**
	 * Will run {@code trigger.action()} within a {@code try - catch (Throwable)} construct and then validate
	 * that an exception was indeed caught, of the right type, and that it's message is as expected.
	 *
	 * @param type
	 * @param expectedMsg
	 * @param trigger
	 */
	public static void validate(Class<?> type, String expectedMsg, Trigger trigger) {
		Throwable caught = null;
		try {
			trigger.action();
		} catch (Throwable t) {
			caught = t;
		}

		try {
			assertNotNull("exception expected, but null", caught);

			assertSame   ("exception class: ",   type,        caught.getClass());
			assertEquals ("exception message: ", expectedMsg, caught.getMessage());
		} catch (Throwable t) {
			// simplify any assert-thrown exception and tie cause to "real" exception; then re-throw

			t.setStackTrace(new StackTraceElement[0]);
			t.initCause(caught);

			throw t;
		}
	}
}
