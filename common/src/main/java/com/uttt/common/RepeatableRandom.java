package com.uttt.common;

import java.util.Random;

public class RepeatableRandom extends Random {

	private static final long serialVersionUID = 1L;

	private final Long seed;

	private RepeatableRandom(long seed) {
		super(seed);

		this.seed = seed;
	}

	public final long getSeed() {
		return seed;
	}

	private final static RepeatableRandom create(StackTraceElement frame, String... extras) {

		int seedForCaller = frame.getClassName().hashCode() + 17 * frame.getMethodName().hashCode();

		if (extras != null) {
			for (final String extra : extras) {
				seedForCaller *= 17;
				seedForCaller += extra.hashCode();
			}
		}

		return new RepeatableRandom(seedForCaller);
	}

	/**
	 * Return a {@code RepeatableRandom} instance whose seed is deterministically derived from: (1) the caller's
	 * class and (2) method names.
	 * <P><B>NOTE:</B> Only the caller's method <i>name</i> is used to derive the seed, not its entire signature.
	 * Thus, a group of overloaded methods will all obtain instances with the same seed. If this is unworkable, then use
	 * {@link RepeatableRandom#create(int, T)} with distinct enumeration values for each such create.
	 *
	 */
	public final static RepeatableRandom create() {
		return create(1);
	}

	/**
	 * Return a {@code RepeatableRandom} instance whose seed is deterministically derived from an active stack frame
	 * a specified number of frames above the caller's frame. From that frame, the method's class and (simple) method
	 * name are combined to form the seed.
	 * <P><B>NOTE:</B> Only the frame's method <i>name</i> is used to derive the seed, not its entire signature.
	 * Thus, a group of overloaded methods will all obtain instances with the same seed. If this is unworkable, then use
	 * {@link RepeatableRandom#create(int, T)} with distinct enumeration values for each such create.
	 *
	 * @param framesUp the number of frames above the caller to probe for the class & method names
	 *
	 * @throws IllegalArgumentException {@code framesUp} is non-positive
	 */
	public final static RepeatableRandom create(int framesUp) {
		if (framesUp < 0) {
			throw ExUtil.create(IllegalArgumentException.class)
				.ident("framesUp", framesUp)
				.append("must be non-negative")
				.build();
		}
		RepeatableRandom rval = create(StackFrameUtil.frameAbove(1 + framesUp));
		return rval;
	}

	/**
	 * Return a {@code RepeatableRandom} instance whose seed is deterministically derived from an active stack frame
	 * a specified number of frames above the caller's frame. From that frame, the method's class and (simple) method
	 * name are combined, along with the enumeration value's class-name and value-name, to form the seed.
	 * This allows a single calling method to create distinct {@code RepeatableRandom} instances in a
	 * controlled manner.
	 * <P><B>NOTE:</B> Only the caller's method <i>name</i> is used to derive the seed, not its entire signature.
	 * Thus, a group of overloaded methods will all obtain instances with the same seed. If this is unworkable, be sure to
	 * use distinct enumeration values for each such create.
	 *
	 * @param framesUp the number of frames above the caller to probe for the class & method names
	 * @param value Any non-null enumeration value convenient for the caller to distinguish among creations
	 *
	 * @throws IllegalArgumentException {@code framesUp} is non-positive; {@code value} is null
	 */
	public static <T extends Enum<T>> RepeatableRandom create(int framesUp, T value) {
		if (value == null) {
			throw ExUtil.create(NullPointerException.class)
			.ident("value", value)
			.build();
		}

		RepeatableRandom rval = create(StackFrameUtil.frameAbove(1+framesUp), value.getClass().getCanonicalName(), value.toString());
		return rval;
	}

	@Override
	public synchronized void setSeed(long newSeed) {
		// super constructor calls (somehow) [#setSeed], so we need to detect first time vs. reset

		if (seed != null) {
			throw ExUtil.create(IllegalAccessError.class)
				.append("may not reset seed of a repeatable random sequence")
				.build();
		}

		super.setSeed(newSeed);
	}
}
