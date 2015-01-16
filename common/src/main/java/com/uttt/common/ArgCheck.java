package com.uttt.common;

public class ArgCheck {

	public static void notNull(String name, Object value) {
		if (value == null) {
			throw new IllegalArgumentException(name + ": may not be null");
		}
	}

	public static void rangeClosed(String name, int value, int lower, int upper) {

		if ((value < lower) || (value > upper)) {
			StringBuilder sb = new StringBuilder();

			sb.append(name).append(" = [").append(value).append("] ");
			sb.append("outside of closed range ");
			sb.append("[");
			sb.append(lower);
			sb.append(",");
			sb.append(upper);
			sb.append("]");

			throw new IllegalArgumentException(sb.toString());
		}
	}

	public static void rangeClosedOpen(String name, int value, int lower, int upper) {

		if ((value < lower) || (value >= upper)) {
			StringBuilder sb = new StringBuilder();

			sb.append(name).append(": ");
			sb.append("outside of closed/open range ");
			sb.append("[");
			sb.append(lower);
			sb.append(",");
			sb.append(upper);
			sb.append(")");

			throw new IllegalArgumentException(sb.toString());
		}
	}
}
