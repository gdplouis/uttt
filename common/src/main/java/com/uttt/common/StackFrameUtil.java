package com.uttt.common;

import org.apache.log4j.Logger;


public class StackFrameUtil {

	/**
	 * Return the {@code StackTraceElement} of the running thread's call stack associated with the
	 * stack frame a given number of frames above the caller. To obtain a trace element associated with
	 * it's own stack frame, the caller should use a {@code levelsUp} value of zero (0). For the caller's
	 * immediate caller, use a value of one (1), and so on.
	 *
	 * @param levelsUp
	 * @return
	 */
	public final static StackTraceElement frameAbove(final int levelsUp) {

		// must be non-negative levelsUp count

		if (levelsUp < 0) {
			return null;
		}

		// probe the stack

		final StackTraceElement[] frames = Thread.currentThread().getStackTrace();

		if ((frames == null) || (frames.length == 0)) {
			return null;
		}

		// add 2 to the levelsUp value: one for this method, and one for Thread#getStackTrace() itself

		StackTraceElement rval = frames[Math.min((levelsUp + 2), (frames.length - 1))];

		return rval;
	}

	private final static String whoami(StackTraceElement frame) {
		final String className  = frame.getClassName();
		final String methodName = frame.getMethodName();

		final int dotPos = className.lastIndexOf('.');

		final String shortName = (dotPos < 0) ? className : className.substring(dotPos + 1);

		final String rval = shortName + "." + methodName;

		return rval;
	}

	/**
	 * Constructs and returns a string identifying the caller by short class- and method-name.
	 * <B>NOTE:</B> This identifier does NOT include file or line number.
	 *
	 * @return
	 */
	public final static String whoami() {
		final StackTraceElement frame = frameAbove(1);

		final String rval = "[" + whoami(frame) + "]";
		return rval;
	}

	/**
	 * Gets the {@code Logger} associated with the calling method. The name of such a logger is the dot-concatenated
	 * class name and the method name, as provided by the caller's {@code StackTraceElement}.
	 *
	 */
	public final static Logger methodLogger() {
		final StackTraceElement frame = frameAbove(1);
		final String loggerName = frame.getClassName() + "." + frame.getMethodName();

		return Logger.getLogger(loggerName);
	}
}
