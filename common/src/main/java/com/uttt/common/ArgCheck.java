package com.uttt.common;

/**
 * A static utility class providing standardized checking of method arguments, throwing meaningful / readable
 * exceptions when the desired invariants are violated.
 *
 * <P>
 *
 * All of the {@code ArgCheck} methods throw an {@code IllegalArgumentException} with a helpful message prefixed by a
 * caller supplied definition identification. Typically, this is an argument name, but may be any useful string, e.g.
 * "foo.getThing()".
 */
public class ArgCheck {

	private ArgCheck() { }

	private static <T> StringBuilder appendNameValue(StringBuilder sb, String name, T value) {

		return sb.append(name).append("=").append(value).append(": ");
	}

	/**
	 * Throws {@code IllegalArgumentException} if the supplied value is null.
	 *
	 * @param name
	 *            Definition identifcation within the caller's source code context, typically an argument name.
	 * @param value
	 *            The value being checked.
	 */
	public static void notNull(String name, Object value) {
		if (value == null) {
			throw new IllegalArgumentException(name + ": may not be null");
		}
	}

	/**
	 * Throws {@code IllegalArgumentException} if the supplied value within the fully closed range, as specified by the
	 * {@code lower} and {@code upper} bounds.
	 *
	 * @param name
	 *            Definition identifcation within the caller's source code context, typically an argument name.
	 * @param value
	 *            The value being checked.
	 * @param lower
	 *            The lower inclusive bound of the range
	 * @param upper
	 *            The upper inclusive bound of the range
	 */
	public static void rangeClosed(String name, int value, int lower, int upper) {

		if ((value < lower) || (value > upper)) {
			StringBuilder sb = new StringBuilder();

			appendNameValue(sb, name, value)
					.append("outside of closed range ") //
					.append("[").append(lower).append(",").append(upper).append("]");

			throw new IllegalArgumentException(sb.toString());
		}
	}

	/**
	 * Throws {@code IllegalArgumentException} if the supplied value within the half-closed range, as specified by the
	 * {@code lower} and {@code upper} bounds.
	 *
	 * <P>
	 *
	 * A very common usecase for this check is to validate an index against a size, as for an array or list. For that
	 * purpose, however, use {@link com.uttt.common.ArgCheck#index(String, int, int) ArgCheck.index(String, int, int)}
	 * which better represents what the caller is verifying.
	 *
	 * @param name
	 *            Definition identifcation within the caller's source code context, typically an argument name.
	 * @param value
	 *            The value being checked.
	 * @param lower
	 *            The lower inclusive bound of the range
	 * @param upper
	 *            The upper exclusive bound of the range
	 */
	public static void rangeClosedOpen(String name, int value, int lower, int upper) {

		if ((value < lower) || (value >= upper)) {
			StringBuilder sb = new StringBuilder();

			appendNameValue(sb, name, value)
					.append("outside of closed/open range ") //
					.append("[").append(lower).append(",").append(upper).append(")");

			throw new IllegalArgumentException(sb.toString());
		}
	}

	/**
	 * Throws {@code IllegalArgumentException} if the supplied value is either negative, or not-less-than {@code size}.
	 * This is the canonical validation to use for array or list bounds checking, among others.
	 *
	 * @param name
	 *            Definition identifcation within the caller's source code context, typically an argument name.
	 * @param value
	 *            The value being checked.
	 * @param size
	 *            The upper exclusive bound of the range
	 */
	public static void index(String name, int value, int size) {

		if ((value < 0) || (value >= size)) {
			StringBuilder sb = new StringBuilder();

			appendNameValue(sb, name, value)
					.append("bad index: ") //
					.append("must be non-negative and less than ").append(size);

			throw new IllegalArgumentException(sb.toString());
		}
	}
}
