package com.uttt.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ForeachableTest {

	@Test(expected=IllegalArgumentException.class)
	public void construct_zeroStep() {
		@SuppressWarnings("unused")
		Foreachable x = Foreachable.until(0, 10, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void construct_negativeStep() {
		@SuppressWarnings("unused")
		Foreachable x = Foreachable.until(0, 10, -2);
	}

	private static void validateCycle(int refStart, int refLimit, int refStep) {
		final String pfx = "(start,limit,step)=(" + refStart + "," + refLimit + "," + refStep + "): ";

		{
			final int expectedUntilCount = Math.max(0, ((refLimit - refStart + refStep - 1) / refStep));

			int untilCount = 0;
			Integer untilCursor = refStart;
			for (Integer f : Foreachable.until(refStart, refLimit, refStep)) {
				assertEquals("until: " + pfx + "f: ", untilCursor, f);
				untilCursor += refStep;
				++untilCount;
			}
			assertEquals("until: " + pfx + "loop count: ", expectedUntilCount, untilCount);
		}

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		{
			final int expectedToCount = Math.max(0, ((refLimit + refStep - refStart + refStep - 1) / refStep));

			int toCount = 0;
			Integer toCursor = refStart;
			for (Integer f : Foreachable.to(refStart, refLimit, refStep)) {
				assertEquals("to: " + pfx + "f: ", toCursor, f);
				toCursor += refStep;
				++toCount;
			}
			assertEquals("to: " + pfx + "loop count: ", expectedToCount, toCount);
		}
	}

	@Test
	public void cycleUntil_degenerate() {
		validateCycle( 0, 0, 1);
		validateCycle( 1, 1, 1);
		validateCycle( 5, 5, 1);
		validateCycle( 5, 1, 1);
		validateCycle( 5, 0, 1);

		validateCycle( 5, 4, 1);
		validateCycle( 5, 0, 1);
		validateCycle( 5,-5, 1);
		validateCycle(-5,-5, 1);
		validateCycle(-5,-6, 1);
	}

	@Test
	public void cycleUntil_lim1() {
		validateCycle(0, 1, 1);
	}

	@Test
	public void cycleUntil_singleStep() {
		validateCycle(  0,   1, 10);
		validateCycle(  0,   2, 10);
		validateCycle(  0,   5, 10);
		validateCycle(  0,  10, 10);

		validateCycle( -1,   0, 10);
		validateCycle( -2,   0, 10);
		validateCycle( -5,   0, 10);
		validateCycle(-10,   0, 10);

		validateCycle( -1,  -1, 10); // actually a degenerate case, but keeps things "neat"
		validateCycle( -2,  -1, 10);
		validateCycle( -5,  -1, 10);
		validateCycle(-10,  -1, 10);
	}

	@Test
	public void cycle_lim10() {
		validateCycle(0, 10, 1);
	}

	@Test
	public void cycleUntil_lim12_stepDivisor() {
		validateCycle(  0,  12,  2);
		validateCycle(  0,  12,  3);
		validateCycle(  0,  12,  4);
		validateCycle(  0,  12,  6);

		validateCycle( -2,  10,  2);
		validateCycle( -2,  10,  3);
		validateCycle( -2,  10,  4);
		validateCycle( -2,  10,  6);

		validateCycle(-20,  -8,  2);
		validateCycle(-20,  -8,  3);
		validateCycle(-20,  -8,  4);
		validateCycle(-20,  -8,  6);
	}

	@Test
	public void cycleUntil_lim12_stepNonDivisor() {
		validateCycle(  0,  12,   5);
		validateCycle(  0,  12,   7);
		validateCycle(  0,  12,   8);
		validateCycle(  0,  12,   9);
		validateCycle(  0,  12,  10);
		validateCycle(  0,  12,  11);
		validateCycle(  0,  12,  12);

		validateCycle( -2,  10,   5);
		validateCycle( -2,  10,   7);
		validateCycle( -2,  10,   8);
		validateCycle( -2,  10,   9);
		validateCycle( -2,  10,  10);
		validateCycle( -2,  10,  11);
		validateCycle( -2,  10,  12);

		validateCycle(-20,  -8,   5);
		validateCycle(-20,  -8,   7);
		validateCycle(-20,  -8,   8);
		validateCycle(-20,  -8,   9);
		validateCycle(-20,  -8,  10);
		validateCycle(-20,  -8,  11);
		validateCycle(-20,  -8,  12);

	}
}
