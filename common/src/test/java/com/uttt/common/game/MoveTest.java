package com.uttt.common.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.uttt.common.board.Board;
import com.uttt.common.board.Position;

public class MoveTest {

	@Test
	public void accessors() {
		final Move   moveBtm     = new Move(1,5);
		assertSame  ("moveBtm.parent: ",    null, moveBtm.subMove);
		assertEquals("moveBtm.row: ",          1, moveBtm.row);
		assertEquals("moveBtm.col: ",          5, moveBtm.col);

		final Move   moveMid     = moveBtm.within(2,4);
		assertSame  ("moveMid.parent: ", moveBtm, moveMid.subMove);
		assertEquals("moveMid.row: ",          2, moveMid.row);
		assertEquals("moveMid.col: ",          4, moveMid.col);

		final Move   moveTop     = moveBtm.within(3,0);
		assertSame  ("moveTop.parent: ", moveBtm, moveTop.subMove);
		assertEquals("moveTop.row: ",          3, moveTop.row);
		assertEquals("moveTop.col: ",          0, moveTop.col);
	}

	@Test
	public void object_toString() {
		final Move   moveBtm     = new Move(1,5);
		String expectedBtm = "M~(1,5)";
		assertEquals("moveBtm.toString(): ", expectedBtm, moveBtm.toString());

		final Move   moveMid     = moveBtm.within(2,4);
		String expectedMid = "M~(2,4)~(1,5)";
		assertEquals("moveMid.toString(): ", expectedMid, moveMid.toString());

		final Move   moveTop     = moveMid.within(3,0);
		String expectedTop = "M~(3,0)~(2,4)~(1,5)";
		assertEquals("moveTop.toString(): ", expectedTop, moveTop.toString());
	}

	@Test
	public void toPosition_h1s3() {
		final Board topBoard = new Board(1,3);
		final Move  t00Move  = new Move(0,0);

		final Position t00MovePos = t00Move.toPosition(topBoard);

	}
}
