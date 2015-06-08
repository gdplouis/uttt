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
			throw ExUtil.create(IllegalArgumentException.class).append("may not be mull").build();
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
			throw ExUtil.create(IllegalArgumentException.class)
				.ident(name, value)
				.append("outside of closed range")
				.append(ExUtil.Join.NONE, "[", lower, ",", upper, "]")
				.build();
		}
	}

	/**
	 * Throws {@code IllegalArgumentException} if the supplied value within the half-closed range, as specified by the
	 * {@code lower} and {@code upper} bounds.
	 *
	 * <P>
	 *
	 * A very common usecase for this check is to validate an index against a size, as for an array or list. For that
	 * purpose, however, use {@link com.uttt.common.ArgCheck#index(String, int, int, String) ArgCheck.index(String, int, int)}
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
			throw ExUtil.create(IllegalArgumentException.class)
				.ident(name, value)
				.append("outside of closed/open range")
				.append(ExUtil.Join.NONE, "[", lower, ",", upper, ")")
				.build();
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
	 * @param sizeQualifier
	 *            An optional string that provides "identifier style" context to the {@code size} argument
	 */
	public static void index(String name, int value, int size, String sizeQualifier) {

		if ((value < 0) || (value >= size)) {
			final String sizeText = ((sizeQualifier == null ? "[" : (sizeQualifier + "[=")) + size+ "]");

			throw ExUtil.create(IndexOutOfBoundsException.class)
				.ident(name, value)
				.append("must be non-negative and less than "+sizeText)
				.build();
		}
	}
}
