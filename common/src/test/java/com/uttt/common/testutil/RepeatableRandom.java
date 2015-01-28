package com.uttt.common.testutil;

import java.util.Random;

public class RepeatableRandom extends Random {

	private static final long serialVersionUID = 1L;

	private final int seed;

	private RepeatableRandom(int seed) {
		super(seed);

		this.seed = seed;
	}

	public final int getSeed() {
		return seed;
	}

	/**
	 *
	 */

	public final static RepeatableRandom create() {
		final StackTraceElement frame = StackFrameUtil.frameAbove(1);

		final int seedForCaller = frame.getClassName().hashCode() + 17 * frame.getMethodName().hashCode();

		return new RepeatableRandom(seedForCaller);
	}
}
