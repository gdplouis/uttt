package com.uttt.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExUtilTest {

	static class Thing {
		public final int a;
		public final int b;

		public Thing(int a, int b) {
			super();
			this.a = a;
			this.b = b;
		}

		@Override
		public String toString() {
			return "("+a+","+b+")";
		}
	}

	@Test
	public void ident_noValue() {

		final Class<NullPointerException> type = NullPointerException.class;
		try {
			throw ExUtil.create(type)
				.ident("v")
				.build();
		} catch (Throwable t) {
			String expected = "v";
			assertEquals(expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}


	@Test
	public void ident_int_unformatted() {
		final int v = 9;

		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		try {
			throw ExUtil.create(type)
				.ident("v", v)
				.append("...details...")
				.build();
		} catch (Throwable t) {
			String expected = "v[=9]: ...details...";
			assertEquals(expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}

	@Test
	public void ident_long_formatted() {
		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		final long v = 0xbab0faceL;
		try {
			throw ExUtil.create(type)
				.ident("v", v, "0x%08x")
				.append("...details...")
				.build();
		} catch (Throwable t) {
			String expected = "v[=0xbab0face]: ...details...";
			assertEquals(expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}

	@Test
	public void ident_float_unformatted() {
		final float v = 9.5f;

		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		try {
			throw ExUtil.create(type)
				.ident("v", v)
				.append("...details...")
				.build();
		} catch (Throwable t) {
			String expected = "v[=9.5]: ...details...";
			assertEquals(expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}

	@Test
	public void ident_double_formatted() {
		final double v = 9.12345f;

		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		try {
			throw ExUtil.create(type)
				.ident("v", v, "%06.3f")
				.append("...details...")
				.build();
		} catch (Throwable t) {
			String expected = "v[=09.123]: ...details...";
			assertEquals(expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}

	@Test
	public void ident_char_printable() {
		final char v = 'a';

		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		try {
			throw ExUtil.create(type)
				.ident("v", v)
				.append("...details...")
				.build();
		} catch (Throwable t) {
			String expected = "v[='a']: ...details...";
			assertEquals(expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}

	@Test
	public void ident_char_unicode_low() {
		final char v = '\03';

		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		try {
			throw ExUtil.create(type)
				.ident("v", v)
				.append("...details...")
				.build();
		} catch (Throwable t) {
			String expected = "v[='\\u0003']: ...details...";
			assertEquals(expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}

	@Test
	public void ident_char_unicode_high() {
		final char v = 130;

		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		try {
			throw ExUtil.create(type)
				.ident("v", v)
				.append("...details...")
				.build();
		} catch (Throwable t) {
			String expected = "v[='\\u0082']: ...details...";
			assertEquals(expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}

	@Test
	public void ident_char_backslash() {
		final char v = '\b';

		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		try {
			throw ExUtil.create(type)
				.ident("v", v)
				.append("...details...")
				.build();
		} catch (Throwable t) {
			String expected = "v[='\\b']: ...details...";
			assertEquals(expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}

	@Test
	public void ident_String() {
		final String v = "hello";

		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		try {
			throw ExUtil.create(type)
				.ident("v", v)
				.append("...details...")
				.build();
		} catch (Throwable t) {
			String expected = "v[=\"hello\"]: ...details...";
			assertEquals("not as expected: ", expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}

	@Test
	public void ident_String_backslash() {
		final String v = "hello\fworld";

		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		try {
			throw ExUtil.create(type)
				.ident("v", v)
				.append("...details...")
				.build();
		} catch (Throwable t) {
			String expected = "v[=\"" + "hello\\fworld" + "\"]: ...details...";
			assertEquals("not as expected: ", expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}

	@Test
	public void ident_String_unicode() {
		final String v = "hello\fworld\u0003!";

		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		try {
			throw ExUtil.create(type)
				.ident("v", v)
				.append("...details...")
				.build();
		} catch (Throwable t) {
			String expected = "v[=\"" + "hello\\fworld\\u0003!" + "\"]: ...details...";
			assertEquals("not as expected: ", expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}

	@Test
	public void ident_object() {
		final Thing v = new Thing(3,4);

		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		try {
			throw ExUtil.create(type)
				.ident("v", v)
				.append("...details...")
				.build();
		} catch (Throwable t) {
			String expected = "v[=" + "(3,4)" + "]: ...details...";
			assertEquals(expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}

	@Test
	public void append_empty() {
		final Thing v = new Thing(3,4);

		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		try {
			throw ExUtil.create(type)
				.ident("v", v)
				.append()
				.build();
		} catch (Throwable t) {
			String expected = "v[=" + "(3,4)" + "]";
			assertEquals(expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}

	@Test
	public void append_singularNull() {
		final Thing v = new Thing(3,4);

		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		try {
			throw ExUtil.create(type)
				.ident("v", v)
				.append((String)null)
				.build();
		} catch (Throwable t) {
			String expected = "v[=" + "(3,4)" + "]";
			assertEquals(expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}

	@Test
	public void append_multiple_strings() {
		final Thing v = new Thing(3,4);

		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		try {
			throw ExUtil.create(type)
				.ident("v", v)
				.append("hello", null, "world")
				.append("foobar", "tiznut")
				.build();
		} catch (Throwable t) {
			String expected = "v[=" + "(3,4)" + "]: hello world: foobar tiznut";
			assertEquals(expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}

	@Test
	public void append_multiple_mixed() {
		final Thing v = new Thing(3,4);

		final Class<IllegalArgumentException> type = IllegalArgumentException.class;
		try {
			throw ExUtil.create(type)
				.append("hello", null, "world")
				.append("{", v, v, "}")
				.append(ExUtil.Join.NONE, "(", 8, "/", 2, ")", "?")
				.append(ExUtil.Join.SEMI.flag | ExUtil.Join.ENDING.flag | ExUtil.Join.AFTER.flag, "apple", "orange", "zebra")
				.tackon("!!!")
				.tackon("...")
				.build();
		} catch (Throwable t) {
			String expected = "hello world: { (3,4) (3,4) }: (8/2)?: apple; orange; zebra; !!!...";
			assertEquals(expected, t.getMessage());
			assertEquals(type, t.getClass());
		}
	}
}
