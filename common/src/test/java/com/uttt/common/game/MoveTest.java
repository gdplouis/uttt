package com.uttt.common.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MoveTest {

	@Test
	public void accessors() {
		Move move = new Move(1,5);

		assertNull  ("parent: ", move.subMove);

		assertEquals("row: ", 1, move.row);
		assertEquals("col: ", 5, move.col);
	}

	@Test
	public void test_toString() {
		Move   moveBtm     = new Move(1,5);
		String expectedBtm = "M~(1,5)";
		assertEquals("moveBtm.toString(): ", expectedBtm, moveBtm.toString());

		Move   moveMid     = moveBtm.within(2,4);
		String expectedMid = expectedBtm.replace("M", "M~(2,4)");
		assertEquals("moveMid.toString(): ", expectedMid, moveMid.toString());

		Move   moveTop     = moveBtm.within(3,3);
		String expectedTop = expectedBtm.replace("M", "M~(3,3)");
		assertEquals("moveTop.toString(): ", expectedTop, moveTop.toString());
	}

}
