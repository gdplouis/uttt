package com.uttt.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Exception builder to support "fluent" programming and standardized error message format.
 */
public final class ExUtil<T extends Throwable> {

	public static enum Join {
		/**
		 * No separator for items in a single append()
		 */
		NONE ((String)null),

		/**
		 * Use a comma (',') as separator for items in a single append()
		 */
		COMMA(","),

		/**
		 *  Use a space (' ') as separator for items in a single append()
		 */
		SPACE(" "),

		/**
		 *  Use a semi-colon (';') as separator for items in a single append()
		 */
		SEMI (";"),

		/**
		 *  Use a (forward) slash ('/') as separator for items in a single append()
		 */
		SLASH("/"),

		/**
		 * Flag indicator: also put the separator (if any) at the ending of the append() portion (possibly with BEFORE
		 * and/or AFTER spaces, as indicated by other flags)
		 */
		ENDING(0x1000),
		/**
		 * Flag indicator: put an additional space (' ') BEFORE each occurance of the separator
		 */
		BEFORE(0x1100),
		/**
		 * Flag indicator: put an additional space (' ') AFTER each occurance of the separator; very useful to combine
		 * with the {@code Join.COMMA} or {@code Join.SEMI}
		 */
		AFTER (0x1200),
		;

		/*pkg*/ final String  sep;
		public  final int     flag;

		Join(String sep) {
			this.flag = this.ordinal();
			this.sep  = sep;
		}

		Join(int flag) {
			this.flag = flag;
			this.sep  = null;
		}

		/*pkg*/ boolean isSetIn(int flags) {
			return (this.flag & flags) != 0;
		}
	}

	private final Class<T>      exClass;
	private final StringBuilder sb;

	private String msgIdent = null;
	private String msgValue = null;

	private ExUtil(Class<T> exClass) {
		this.exClass = exClass;
		this.sb      = new StringBuilder();
	}

	public static <U extends Throwable> ExUtil<U> create(Class<U> exClass) {
		return new ExUtil<>(exClass);
	}

	/**
	 * Construct and return an exception of the class specified by the {@code #create()}, without a (possibly null)
	 * chained {@code cause}. Such construction is done via reflection, first looking for a constructor like
	 * {@code Exception(String, Throwable)}, then for just {@code Exception(String)}. The stack frame trace of the
	 * exception is then cleaned up (to remove the leading elements that refer to the reflection mechanism itself
	 * as well as the involved [ExUtil] methods).
	 */
	public T build(Throwable cause) {

		boolean takesCause;

		Constructor<T> constructor;
		try {
			constructor = exClass.getConstructor(String.class, Throwable.class);
			takesCause  = true;
		} catch (NoSuchMethodException | SecurityException e) {
			try {
				constructor = exClass.getConstructor(String.class);
				takesCause  = false;
			} catch (NoSuchMethodException | SecurityException e2) {
				throw new RuntimeException("Can't find/access constructor for [" + exClass.getName() + "]", e2);
			}
		}

		try {
			prependIdentAndValue();

			T builtEx;
			if (takesCause) {
				builtEx = constructor.newInstance(sb.toString(), cause);
			} else {
				builtEx = constructor.newInstance(sb.toString());
			}

			// remove the stack trace elements present because of this utility

			StackTraceElement[] stackTrace = builtEx.getStackTrace();
			builtEx.setStackTrace(stackTrace);
			int i = 0;
			{
				while (i < stackTrace.length && !stackTrace[i].getMethodName().equals("build")) ++i;
				while (i < stackTrace.length &&  stackTrace[i].getMethodName().equals("build")) ++i;
			}
			StackTraceElement[] decapTrace = new StackTraceElement[stackTrace.length - i];
			System.arraycopy(stackTrace, i, decapTrace, 0, decapTrace.length);
			builtEx.setStackTrace(decapTrace);

			return builtEx;

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("Failed to construct exception [" + exClass.getName() + "]", e);
		}
	}

	/**
	 * Construct and return an exception of the class specified by the {@code #create()}, without a chained
	 * {@code cause}
	 *
	 * @see #build(Throwable)
	 */
	public T build() {
		return build(null);
	}

	/**
	 * Note the given identifier text for later use as part of the early (leftwards) message content.
	 */
	public ExUtil<T> ident(String ident) {
		this.msgIdent = ident;
		return this;
	}

	/**
	 * Note the given identifier text and value for later use as part of the early (leftwards) message content.
	 */
	public ExUtil<T> ident(String ident, Object value) {
		this.msgIdent = ident;
		this.msgValue = value.toString();
		return this;
	}

	/**
	 * Note the given identifier text and value for later use as part of the early (leftwards) message content.
	 */
	public ExUtil<T> ident(String ident, String value) {
		this.msgIdent = ident;
		this.msgValue = (value == null ? "null" : ( "\"" + StringEscapeUtils.escapeJava(value) + "\""));
		return this;
	}

	/**
	 * Note the given identifier text and value for later use as part of the early (leftwards) message content.
	 */
	public ExUtil<T> ident(String ident, char value) {
		this.msgIdent = ident;
		this.msgValue = "'" + StringEscapeUtils.escapeJava(""+value) + "'";
		return this;
	}

	/**
	 * Note the given identifier text and value for later use as part of the early (leftwards) message content,
	 * using the given format to convert the value to string text.
	 */
	public ExUtil<T> ident(String ident, long value, String format) {
		this.msgIdent = ident;
		this.msgValue = (format == null ? Long.toString(value) : String.format(format, value));
		return this;
	}

	/**
	 * Note the given identifier text and value for later use as part of the early (leftwards) message content.
	 */
	public ExUtil<T> ident(String ident, long value) {
		return ident(ident,  value, (String) null);
	}

	/**
	 * Note the given identifier text and value for later use as part of the early (leftwards) message content,
	 * using the given format to convert the value to string text.
	 */
	public ExUtil<T> ident(String ident, double value, String format) {
		this.msgIdent = ident;
		this.msgValue = (format == null ? Double.toString(value) : String.format(format, value));
		return this;
	}

	/**
	 * Note the given identifier text and value for later use as part of the early (leftwards) message content.
	 */
	public ExUtil<T> ident(String ident, double value) {
		return ident(ident,  value, (String) null);
	}

	/**
	 * Append a sequence of items (varags) onto the pending exception's detail message. Use the {@code flags} to
	 * indicate how items should be joined, e.g. what separator (if any), with extra space before/after that separator,
	 * if the separator should always be added at the end of the sequence, etc. Also, start off with a ": " high-level
	 * separator if the accumulated message text is not empty.
	 */
	public ExUtil<T> append(Integer flags, Object... messages) {
		final int  ordinal = flags & 0xFF;
		final Join join    = Join.values()[ordinal];

		final boolean ending = (flags & Join.ENDING.flag) == Join.ENDING.flag;
		final boolean before = (flags & Join.BEFORE.flag) == Join.BEFORE.flag;
		final boolean after  = (flags & Join.AFTER .flag) == Join.AFTER .flag;

		if ((messages != null) && (messages.length > 0)) {
			if (sb.length() > 0) {
				sb.append(": ");
			}

			boolean useSep = false;
			for (final Object message : messages) {
				if (message != null) {
					if (join.sep != null) {
						if (useSep) {
							if (before) sb.append(' ');

							sb.append(join.sep);

							if (after) sb.append(' ');
						}
					}

					sb.append(message);
					useSep = true;
				}
			}
			if (join.sep != null) {
				if (ending) {
					if (before) sb.append(' ');

					sb.append(join.sep);

					if (after) sb.append(' ');
				}
			}
		}

		return this;
	}

	public ExUtil<T> append(Join join, Object... messages) {
		return append(join.flag, messages);
	}

	public ExUtil<T> append(Object... messages) {
		return append(Join.SPACE, messages);
	}

	/**
	 * For fine-grained control by the caller, this method will simply append the given string onto the
	 * exception's message accumulation buffer with no separator logic at all.
	 */
	public  ExUtil<T>  tackon(String message) {
		sb.append(message);
		return this;
	}

	// ====================================================================================================

	private void prependIdentAndValue() {
		StringBuilder prefix = new StringBuilder();

		if (msgIdent != null) {
			prefix.append(msgIdent);

			if (msgValue != null) {
				prefix.append("[=").append(msgValue).append("]");
			}
		} else {
			if (msgValue != null) {
				prefix.append(msgValue);
			}
		}

		if (prefix.length() > 0) {
			if (sb.length() > 0) {
				prefix.append(": ");
			}

			sb.insert(0, prefix);
		}
	}
}
