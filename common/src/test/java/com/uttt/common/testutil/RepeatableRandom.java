package com.uttt.common.testutil;

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
	 * Thus, a set overloaded methods will all obtain instances with the same seed. If this is unworkable, then use
	 * {@link RepeatableRandom#create(T)} with distinct enumeration values for each such create.
	 */
	public final static RepeatableRandom create() {
		RepeatableRandom rval = create(StackFrameUtil.frameAbove(1));
		return rval;
	}

	/**
	 * Return a {@code RepeatableRandom} instance whose seed is deterministically derived from: (1) the caller's
	 * class, (2) method names, (3) the class name and (4) value-string of some (any) enumeration value.
	 * This allows a single calling method to create distinct {@code RepeatableRandom} instances in a
	 * controlled manner.
	 * <P><B>NOTE:</B> Only the caller's method <i>name</i> is used to derive the seed, not its entire signature.
	 * Thus, a set overloaded methods will all obtain instances with the same seed. If this is unworkable, be sure to
	 * use distinct enumeration values for each such create.
	 *
	 * @param value Any enumeration value convenient for the caller to distinguish among creations
	 */
	public static <T extends Enum<T>> RepeatableRandom create(T value) {
		if (value == null) {
			throw new IllegalArgumentException("enum value may not be null");
		}

		RepeatableRandom rval = create(StackFrameUtil.frameAbove(1), value.getClass().getCanonicalName(), value.toString());
		return rval;
	}

	@Override
	public synchronized void setSeed(long newSeed) {
		// super constructor calls (somehow) [#setSeed], so we need to detect first time vs. reset

		if (seed != null) {
			throw new IllegalAccessError("may not reset seed of a repeatable random sequence");
		}

		super.setSeed(newSeed);
	}
}
