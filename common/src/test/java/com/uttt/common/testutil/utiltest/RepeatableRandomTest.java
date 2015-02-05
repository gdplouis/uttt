package com.uttt.common.testutil.utiltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.uttt.common.Foreachable;
import com.uttt.common.TestExceptionValidator;
import com.uttt.common.testutil.RepeatableRandom;

public class RepeatableRandomTest {

	private enum Foobar {
		TIZ, NUT
	}

	private static RepeatableRandom createAAA(int framesUp, Foobar value) {
		if (value == null) {
			return RepeatableRandom.create(framesUp);
		}
		return RepeatableRandom.create(framesUp, value);
	}

	private static RepeatableRandom createBBB(int framesUp, Foobar value) {
		if (value == null) {
			return RepeatableRandom.create(framesUp);
		}
		return RepeatableRandom.create(framesUp, value);
	}

	private static void checkSame(RepeatableRandom randAAA, RepeatableRandom randBBB) {

		for (final int i : Foreachable.until(50)) {
			double valueAAA = randAAA.nextDouble();
			double valueBBB = randBBB.nextDouble();

			assertEquals("iteration #" + i + ": ", valueAAA, valueBBB, 0.0);
		}
	}

	private static void checkDifferent(RepeatableRandom randAAA, RepeatableRandom randBBB) {

		for (@SuppressWarnings("unused") final int i : Foreachable.until(50)) {
			double valueAAA = randAAA.nextDouble();
			double valueBBB = randBBB.nextDouble();

			if (valueAAA != valueBBB) {
				return;
			}
		}

		fail("random sequences expected to be different");
	}

	@Test
	public void setSeed() {
		RepeatableRandom randAAA = RepeatableRandom.create();

		TestExceptionValidator.validate(IllegalAccessError.class,
			"may not reset seed of a repeatable random sequence",
			new TestExceptionValidator.Trigger() { @Override public void action() {
				randAAA.setSeed(0);
			}}
		);
	}

	@Test
	public void sameCaller_directCreate() {
		RepeatableRandom randAAA = RepeatableRandom.create();
		RepeatableRandom randBBB = RepeatableRandom.create();

		checkSame(randAAA, randBBB);
	}

	@Test
	public void diffCaller_helperCreateUp0() {
		RepeatableRandom randAAA = createAAA(0, null);
		RepeatableRandom randBBB = createBBB(0, null);

		checkDifferent(randAAA, randBBB);
	}

	@Test
	public void sameCaller_directCreate_withSameEnum() {
		RepeatableRandom randAAA = RepeatableRandom.create(0, Foobar.TIZ);
		RepeatableRandom randBBB = RepeatableRandom.create(0, Foobar.TIZ);

		checkSame(randAAA, randBBB);
	}

	@Test
	public void sameCaller_directCreate_withDiffEnum() {
		RepeatableRandom randAAA = RepeatableRandom.create(0, Foobar.TIZ);
		RepeatableRandom randBBB = RepeatableRandom.create(0, Foobar.NUT);

		checkDifferent(randAAA, randBBB);
	}

	@Test
	public void sameCaller_directCreate_withAndWithoutEnum() {
		RepeatableRandom randAAA = RepeatableRandom.create(0, Foobar.TIZ);
		RepeatableRandom randBBB = RepeatableRandom.create();

		checkDifferent(randAAA, randBBB);
	}

	@Test
	public void diffCaller_helperCreateUp0_withSameEnum() {
		RepeatableRandom randAAA = createAAA(0, Foobar.TIZ);
		RepeatableRandom randBBB = createBBB(0, Foobar.TIZ);

		checkDifferent(randAAA, randBBB);
	}

	@Test
	public void diffCaller_helperCreateUp1_withSameEnum() {
		RepeatableRandom randAAA = createAAA(1, Foobar.TIZ);
		RepeatableRandom randBBB = createBBB(1, Foobar.TIZ);

		checkSame(randAAA, randBBB);
	}

	@Test
	public void diffCaller_helperCreateUp1_withDiffEnum() {
		RepeatableRandom randAAA = createAAA(1, Foobar.TIZ);
		RepeatableRandom randBBB = createBBB(1, Foobar.NUT);

		checkDifferent(randAAA, randBBB);
	}
}
