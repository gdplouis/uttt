package com.uttt.common.testutil.utiltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.uttt.common.Foreachable;
import com.uttt.common.testutil.RepeatableRandom;

public class RepeatableRandomTest {

	@Test
	public void randseq_sameCaller() {
		RepeatableRandom randAAA = RepeatableRandom.create();
		RepeatableRandom randBBB = RepeatableRandom.create();

		for (final int i : Foreachable.until(50)) {
			double valueAAA = randAAA.nextDouble();
			double valueBBB = randBBB.nextDouble();

			assertEquals("iteration #" + i + ": ", valueAAA, valueBBB, 0.0);
		}
	}

	private static RepeatableRandom createAAA() {
		return RepeatableRandom.create();
	}

	private static RepeatableRandom createBBB() {
		return RepeatableRandom.create();
	}

	@Test
	public void randseq_diffCaller() {
		RepeatableRandom randAAA = createAAA();
		RepeatableRandom randBBB = createBBB();

		for (@SuppressWarnings("unused") final int i : Foreachable.until(50)) {
			double valueAAA = randAAA.nextDouble();
			double valueBBB = randBBB.nextDouble();

			if (valueAAA != valueBBB) {
				return;
			}
		}

		fail("random sequences expected to be different");
	}
}
