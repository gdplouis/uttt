package com.uttt.common.testutil.utiltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.uttt.common.Foreachable;
import com.uttt.common.testutil.RepeatableRandom;

public class RepeatableRandomTest {

	private enum Foo {
		BAR, TIZ, NUT
	}

	private static RepeatableRandom createAAA(Foo value) {
		if (value == null) {
			return RepeatableRandom.create();
		}
		return RepeatableRandom.create(value);
	}

	private static RepeatableRandom createBBB(Foo value) {
		if (value == null) {
			return RepeatableRandom.create();
		}
		return RepeatableRandom.create(value);
	}

	@Test(expected=IllegalAccessError.class)
	public void setSeed() {
		RepeatableRandom randAAA = RepeatableRandom.create();

		randAAA.setSeed(0);
	}

	@Test
	public void sameCaller() {
		RepeatableRandom randAAA = RepeatableRandom.create();
		RepeatableRandom randBBB = RepeatableRandom.create();

		for (final int i : Foreachable.until(50)) {
			double valueAAA = randAAA.nextDouble();
			double valueBBB = randBBB.nextDouble();

			assertEquals("iteration #" + i + ": ", valueAAA, valueBBB, 0.0);
		}
	}

	@Test
	public void diffCaller() {
		RepeatableRandom randAAA = createAAA(null);
		RepeatableRandom randBBB = createBBB(null);

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
	public void sameCaller_withSameEnum() {
		RepeatableRandom randAAA = RepeatableRandom.create(Foo.BAR);
		RepeatableRandom randBBB = RepeatableRandom.create(Foo.BAR);

		for (final int i : Foreachable.until(50)) {
			double valueAAA = randAAA.nextDouble();
			double valueBBB = randBBB.nextDouble();

			assertEquals("iteration #" + i + ": ", valueAAA, valueBBB, 0.0);
		}
	}

	@Test
	public void sameCaller_withDiffEnum() {
		RepeatableRandom randAAA = RepeatableRandom.create(Foo.BAR);
		RepeatableRandom randBBB = RepeatableRandom.create(Foo.TIZ);

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
	public void sameCaller_withAndWithoutEnum() {
		RepeatableRandom randAAA = RepeatableRandom.create(Foo.BAR);
		RepeatableRandom randBBB = RepeatableRandom.create();

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
	public void diffCaller_withSameEnum() {
		RepeatableRandom randAAA = createAAA(Foo.BAR);
		RepeatableRandom randBBB = createBBB(Foo.BAR);

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
