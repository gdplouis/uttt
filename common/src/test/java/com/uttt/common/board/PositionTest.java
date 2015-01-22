package com.uttt.common.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

public class PositionTest implements PlayableTest {

	@Test(expected = IllegalArgumentException.class)
	public void construct_boardNull() {
		@SuppressWarnings("unused")
		Position x = new Position(null, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void construct_rowNeg() {
		@SuppressWarnings("unused")
		Position x = new Position(new Board(1, 3), -1, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void construct_rowTooBig() {
		@SuppressWarnings("unused")
		Position x = new Position(new Board(1, 3), 99, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void construct_colNeg() {
		@SuppressWarnings("unused")
		Position x = new Position(new Board(1, 3), 0, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void construct_colTooBig() {
		@SuppressWarnings("unused")
		Position x = new Position(new Board(1, 3), 0, 99);
	}

	// ====================================================================================================

	@Test
	public void accessor_getBoard() {
		Board    board = new Board(1, 3);
		Position pos   = new Position(board, 1, 2);

		assertSame(board, pos.getBoard());
	}

	@Test
	public void accessor_getRow() {
		Board    board = new Board(1, 3);
		Position pos   = new Position(board, 1, 2);

		assertEquals(1, pos.getRow());
	}

	@Test
	public void accessor_getCol() {
		Board    board = new Board(1, 3);
		Position pos   = new Position(board, 1, 2);

		assertEquals(2, pos.getCol());
	}

	@Test
	public void accessor_deref() {
		Board    board    = new Board(2, 3); // needs be at least h2 for this to be sensible

		Board    t12Board = board.getSubBoard(1,  2);
		Position t12pos   = board.at (1, 2);

		assertSame(t12Board, t12pos.derefBoard());
	}

	// ====================================================================================================
	// tests defined in [PlayableTest]

	@Override
	@Test
	public void accessor_getHeight_h1s3() {
		final Board topBoard = new Board(1,3);

		assertEquals("topBoard.at(1,2).getHeight(): ", 1, topBoard.at(1,2).getHeight());
	}

	@Override
	@Test
	public void accessor_getHeight_h2s3() {
		final Board topBoard = new Board(2,3);

		assertEquals("topBoard.at(1,2).getHeight(): ",         2, topBoard.at(1,2).getHeight());
		assertEquals("topBoard.at(1,2).at(0,0).getHeight(): ", 1, topBoard.at(1,2).at(0,0).getHeight());
	}

	@Override
	@Test
	public void accessor_getHeight_h3s4() {
		final Board topBoard = new Board(3,4);

		assertEquals("topBoard.at(1,2).getHeight(): ",                 3, topBoard.at(1,2).getHeight());
		assertEquals("topBoard.at(1,2).at(0,0).getHeight(): ",         2, topBoard.at(1,2).at(0,0).getHeight());
		assertEquals("topBoard.at(1,2).at(0,0).at(2,0).getHeight(): ", 1, topBoard.at(1,2).at(0,0).at(2,0).getHeight());
	}

	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	@Override
	@Test
	public void accessor_isTop_h1s3() {
		final Board topBoard = new Board(1,3);

		assertTrue("topBoard.at(1,2).isTop(): ", topBoard.at(1,2).isTop());
	}

	@Override
	@Test
	public void accessor_isTop_h2s3() {
		final Board topBoard = new Board(2,3);

		assertTrue ("topBoard.at(1,2).isTop(): ",         topBoard.at(1,2).isTop());
		assertFalse("topBoard.at(1,2).at(0,0).isTop(): ", topBoard.at(1,2).at(0,0).isTop());
	}

	@Override
	@Test
	public void accessor_isTop_h3s4() {
		final Board topBoard = new Board(3,4);

		assertTrue ("topBoard.at(1,2).isTop(): ",                 topBoard.at(1,2).isTop());
		assertFalse("topBoard.at(1,2).at(0,0).isTop(): ",         topBoard.at(1,2).at(0,0).isTop());
		assertFalse("topBoard.at(1,2).at(0,0).at(2,0).isTop(): ", topBoard.at(1,2).at(0,0).at(2,0).isTop());
	}

	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	@Override
	@Test
	public void accessor_isBottom_h1s3() {
		final Board topBoard = new Board(1,3);

		assertTrue("topBoard.at(1,2).isBottom(): ", topBoard.at(1,2).isBottom());
	}

	@Override
	@Test
	public void accessor_isBottom_h2s3() {
		final Board topBoard = new Board(2,3);

		assertFalse("topBoard.at(1,2).isBottom(): ",         topBoard.at(1,2).isBottom());
		assertTrue ("topBoard.at(1,2).at(0,0).isBottom(): ", topBoard.at(1,2).at(0,0).isBottom());
	}

	@Override
	@Test
	public void accessor_isBottom_h3s4() {
		final Board topBoard = new Board(3,4);

		assertFalse("topBoard.at(1,2).isBottom(): ",                 topBoard.at(1,2).isBottom());
		assertFalse("topBoard.at(1,2).at(0,0).isBottom(): ",         topBoard.at(1,2).at(0,0).isBottom());
		assertTrue ("topBoard.at(1,2).at(0,0).at(2,0).isBottom(): ", topBoard.at(1,2).at(0,0).at(2,0).isBottom());
	}

	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	@Override
	@Test
	public void accessor_getTopBoard_h1s3() {
		final Board topBoard = new Board(1, 3);
		{
			final Position t01Pos = topBoard.at(0, 1);
			assertSame("t01Pos.getTopBoard(): ", topBoard, t01Pos.getTopBoard());
		}
	}

	@Override
	@Test
	public void accessor_getTopBoard_h2s3() {
		final Board topBoard = new Board(2, 3);
		{
			final Position t01Pos = topBoard.at(0, 1);
			assertSame("t01Pos.getTopBoard(): ", topBoard, t01Pos.getTopBoard());
		}
		{
			final Position t01s11Pos = topBoard.getSubBoard(0, 1).at(1, 1);
			assertSame("t01s11Pos.getTopBoard(): ", topBoard, t01s11Pos.getTopBoard());
		}
	}

	@Override
	@Test
	public void accessor_getTopBoard_h3s4() {
		final Board topBoard = new Board(3,4);
		{
			final Position t01Pos = topBoard.at(0, 1);
			assertSame("t01Pos.getTopBoard(): ", topBoard, t01Pos.getTopBoard());
		}
		{
			final Position t01s11Pos = topBoard.getSubBoard(0, 1).at(1, 1);
			assertSame("t01s11Pos.getTopBoard(): ", topBoard, t01s11Pos.getTopBoard());
		}
		{
			final Position t01s11s00Pos = topBoard.getSubBoard(0, 1).getSubBoard(1, 1).at(0,0);
			assertSame("t01s11s00Pos.getTopBoard(): ", topBoard, t01s11s00Pos.getTopBoard());
		}
	}

	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	@Override
	@Test
	public void accessor_getPosition_h1s3() {
		final Board topBoard    = new Board(1,3);
		{
			final Position t01Pos = topBoard.at(0,1);
			assertSame("t01Pos: ", t01Pos, t01Pos.getPosition());
		}
	}

	@Override
	@Test
	public void accessor_getPosition_h2s3() {
		final Board topBoard = new Board(2, 3);
		{
			final Position t01Pos = topBoard.at(0, 1);
			assertSame("t01Pos: ", t01Pos, t01Pos.getPosition());
		}
		{
			final Position t01s11Pos = topBoard.getSubBoard(0, 1).at(1, 1);
			assertSame("t01s11Pos: ", t01s11Pos, t01s11Pos.getPosition());
		}
	}

	@Override
	@Test
	public void accessor_getPosition_h3s4() {
		final Board topBoard = new Board(3, 4);
		{
			final Position t01Pos = topBoard.at(0, 1);
			assertSame("t01Pos: ", t01Pos, t01Pos.getPosition());
		}
		{
			final Position t01s11Pos = topBoard.getSubBoard(0, 1).at(1, 1);
			assertSame("t01s11Pos: ", t01s11Pos, t01s11Pos.getPosition());
		}
		{
			final Position t01s11s00Pos = topBoard.getSubBoard(0, 1).getSubBoard(1, 1).at(0,0);
			assertSame("t01s11s00Pos: ", t01s11s00Pos, t01s11s00Pos.getPosition());
		}
	}

	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	@Override
	@Test
	public void accessor_isPlayable_h1s3_win() {
		final Board     topBoard  = new Board(1, 3);
		final Validator validator = new PlayableTest.Validator(topBoard);

		final Position  t12Pos    = validator.add(topBoard.at(1, 2));

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		validator.place(topBoard.at(0,0), Token.PLAYER_AAA);
		validator.place(topBoard.at(0,1), Token.PLAYER_AAA);
		validator.place(topBoard.at(0,2), Token.PLAYER_AAA, topBoard, t12Pos);

		validator.checkAllClosed();
	}

	@Override
	@Test
	public void accessor_isPlayable_h2s3_win() {

		final Board     topBoard  = new Board(2, 3);
		final Validator validator = new PlayableTest.Validator(topBoard);

		final Board     t00Board  = validator.add(topBoard.getSubBoard(0, 0));
		final Board     t01Board  = validator.add(topBoard.getSubBoard(0, 1));
		final Board     t02Board  = validator.add(topBoard.getSubBoard(0, 2));

		final Position  t10sxxPos = validator.add(topBoard.at(1, 0));
		final Position  t00s11Pos = validator.add(t00Board.at(1, 1));

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		validator.place(t00Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t00Board.at(1,0), Token.PLAYER_AAA);
		validator.place(t00Board.at(2,0), Token.PLAYER_AAA, t00Board, t00s11Pos);

		validator.place(t01Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t01Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t01Board.at(0,2), Token.PLAYER_AAA, t01Board);

		validator.place(t02Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t02Board.at(1,1), Token.PLAYER_AAA);
		validator.place(t02Board.at(2,2), Token.PLAYER_AAA, t02Board, t10sxxPos, topBoard);

		validator.checkAllClosed();
	}

	@Override
	@Test
	public void accessor_isPlayable_h3s4_win() {

		final Board     topBoard  = new Board(3, 4);
		final Validator validator = new PlayableTest.Validator(topBoard);

		final Board     t00Board    = validator.add(topBoard.getSubBoard(0, 0));
		final Board     t01Board    = validator.add(topBoard.getSubBoard(0, 1));
		final Board     t02Board    = validator.add(topBoard.getSubBoard(0, 2));
		final Board     t03Board    = validator.add(topBoard.getSubBoard(0, 3));

		final Board     t00s00Board = validator.add(t00Board.getSubBoard(0, 0));
		final Board     t00s01Board = validator.add(t00Board.getSubBoard(0, 1));
		final Board     t00s02Board = validator.add(t00Board.getSubBoard(0, 2));
		final Board     t00s03Board = validator.add(t00Board.getSubBoard(0, 3));

		final Board     t01s00Board = validator.add(t01Board.getSubBoard(0, 0));
		final Board     t01s01Board = validator.add(t01Board.getSubBoard(0, 1));
		final Board     t01s02Board = validator.add(t01Board.getSubBoard(0, 2));
		final Board     t01s03Board = validator.add(t01Board.getSubBoard(0, 3));

		final Board     t02s00Board = validator.add(t02Board.getSubBoard(0, 0));
		final Board     t02s01Board = validator.add(t02Board.getSubBoard(0, 1));
		final Board     t02s02Board = validator.add(t02Board.getSubBoard(0, 2));
		final Board     t02s03Board = validator.add(t02Board.getSubBoard(0, 3));

		final Board     t03s00Board = validator.add(t03Board.getSubBoard(0, 0));
		final Board     t03s01Board = validator.add(t03Board.getSubBoard(0, 1));
		final Board     t03s02Board = validator.add(t03Board.getSubBoard(0, 2));
		final Board     t03s03Board = validator.add(t03Board.getSubBoard(0, 3));

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		validator.place(t00s00Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t00s00Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t00s00Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t00s00Board.at(0,3), Token.PLAYER_AAA, t00s00Board);

		validator.place(t00s01Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t00s01Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t00s01Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t00s01Board.at(0,3), Token.PLAYER_AAA, t00s01Board);

		validator.place(t00s02Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t00s02Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t00s02Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t00s02Board.at(0,3), Token.PLAYER_AAA, t00s02Board);

		validator.place(t00s03Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t00s03Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t00s03Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t00s03Board.at(0,3), Token.PLAYER_AAA, t00s03Board, t00Board);

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		validator.place(t01s00Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t01s00Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t01s00Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t01s00Board.at(0,3), Token.PLAYER_AAA, t01s00Board);

		validator.place(t01s01Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t01s01Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t01s01Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t01s01Board.at(0,3), Token.PLAYER_AAA, t01s01Board);

		validator.place(t01s02Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t01s02Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t01s02Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t01s02Board.at(0,3), Token.PLAYER_AAA, t01s02Board);

		validator.place(t01s03Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t01s03Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t01s03Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t01s03Board.at(0,3), Token.PLAYER_AAA, t01s03Board, t01Board);

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		validator.place(t02s00Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t02s00Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t02s00Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t02s00Board.at(0,3), Token.PLAYER_AAA, t02s00Board);

		validator.place(t02s01Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t02s01Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t02s01Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t02s01Board.at(0,3), Token.PLAYER_AAA, t02s01Board);

		validator.place(t02s02Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t02s02Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t02s02Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t02s02Board.at(0,3), Token.PLAYER_AAA, t02s02Board);

		validator.place(t02s03Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t02s03Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t02s03Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t02s03Board.at(0,3), Token.PLAYER_AAA, t02s03Board, t02Board);

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		validator.place(t03s00Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t03s00Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t03s00Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t03s00Board.at(0,3), Token.PLAYER_AAA, t03s00Board);

		validator.place(t03s01Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t03s01Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t03s01Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t03s01Board.at(0,3), Token.PLAYER_AAA, t03s01Board);

		validator.place(t03s02Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t03s02Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t03s02Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t03s02Board.at(0,3), Token.PLAYER_AAA, t03s02Board);

		validator.place(t03s03Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t03s03Board.at(0,1), Token.PLAYER_AAA);
		validator.place(t03s03Board.at(0,2), Token.PLAYER_AAA);
		validator.place(t03s03Board.at(0,3), Token.PLAYER_AAA, t03s03Board, t03Board, topBoard);

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		validator.checkAllClosed();
	}

	@Override
	@Test
	public void accessor_isPlayable_h1s3_draw() {

		final Board     topBoard  = new Board(1, 3);
		final Validator validator = new PlayableTest.Validator(topBoard);

		final Position  t12Pos    = validator.add(topBoard.at(1, 2));

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		validator.place(topBoard.at(0,0), Token.PLAYER_AAA);
		validator.place(topBoard.at(0,1), Token.PLAYER_AAA);
		validator.place(topBoard.at(0,2), Token.PLAYER_AAA, topBoard, t12Pos);

		validator.checkAllClosed();
		}

	@Override
	@Test @Ignore
	public void accessor_isPlayable_h2s3_draw() {
		fail("NYI");
	}

	@Override
	@Test @Ignore
	public void accessor_isPlayable_h3s4_draw() {
		fail("NYI");
	}

	// ====================================================================================================

	@Test
	public void deref_h1s3() {
		final Board    topBoard = new Board(1, 3);

		final Position t12Pos   = new Position(topBoard, 1, 2);
		t12Pos.place(Token.PLAYER_AAA);
		assertSame("t12Pos.derefToken(): ", Token.PLAYER_AAA, (t12Pos.derefToken()));
	}

	@Test
	public void deref_h2s3() {
		final Board    topBoard = new Board(2, 3);

		final Board    t12Board = topBoard.getSubBoard(1, 2);
		final Position t12Pos   = new Position(topBoard, 1, 2);
		assertSame("t12Pos.derefBoard(): ", t12Board, (t12Pos.derefBoard()));

		final Position t12s00Pos = new Position(t12Board, 0, 0);
		t12s00Pos.place(Token.PLAYER_BBB);
		assertSame("t12s00Pos.derefToken(): ", Token.PLAYER_BBB, (t12s00Pos.derefToken()));
	}

	@Test
	public void deref_h3s4() {
		final Board    topBoard = new Board(3,4);

		final Board    t12Board = topBoard.getSubBoard(1, 2);
		final Position t12Pos   = new Position(topBoard, 1, 2);
		assertSame("t12Pos.derefBoard(): ", t12Board, (t12Pos.derefBoard()));

		final Board    t12s00Board = t12Board.getSubBoard(0, 0);
		final Position t12s00Pos   = new Position(t12Board, 0, 0);
		assertSame("t12Pos.derefBoard(): ", t12s00Board, (t12s00Pos.derefBoard()));

		final Position t12s00s11Pos = new Position(t12s00Board, 1, 1);
		t12s00s11Pos.place(Token.PLAYER_AAA);
		assertSame("t12s00Pos.derefToken(): ", Token.PLAYER_AAA, (t12s00s11Pos.derefToken()));
	}

	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	@Test
	public void place_h1s3() {
		final Board topBoard = new Board(1, 3);
		assertEquals("topBoard.getPlayCount(): ", 0, topBoard.getPlayCount());

		final Position t00NPC = topBoard.at(0,0).place(Token.PLAYER_AAA);
		assertNull("t00NPC: ", t00NPC);
		assertEquals("topBoard.getSubToken(0,0): ", Token.PLAYER_AAA, topBoard.getSubToken(0,0));
		assertEquals("topBoard.getPlayCount(): ", 1, topBoard.getPlayCount());


		final Position t12NPC = topBoard.at(1,2).place(Token.PLAYER_BBB);
		assertNull("t12NPC: ", t12NPC);
		assertEquals("topBoard.getSubToken(1,2): ", Token.PLAYER_BBB, topBoard.getSubToken(1,2));
		assertEquals("topBoard.getPlayCount(): ", 2, topBoard.getPlayCount());
	}

	@Test
	public void place_h2s3() {
		final Board topBoard = new Board(2, 3);
		assertEquals("topBoard.getPlayCount(): ", 0, topBoard.getPlayCount());

		final Board t00Board    = topBoard.getSubBoard(0, 0);
		final Board t01Board    = topBoard.getSubBoard(0, 1);
		final Board t02Board    = topBoard.getSubBoard(0, 2);
		final Board t11Board    = topBoard.getSubBoard(1, 1);
		final Board t12Board    = topBoard.getSubBoard(1, 2);
		final Board t22Board    = topBoard.getSubBoard(2, 2);

		{
			final Position t00s00NPC = t00Board.at(0,0).place(Token.PLAYER_AAA);
			final Position t00s01NPC = t00Board.at(0,1).place(Token.PLAYER_AAA);
			final Position t00s02NPC = t00Board.at(0,2).place(Token.PLAYER_AAA);
			assertNotNull("t00s00NPC: ", t00s00NPC);
			assertNotNull("t00s01NPC: ", t00s01NPC);
			assertNotNull("t00s02NPC: ", t00s02NPC);

			assertSame   ("t00s00NPC.derefBoard(): ", t00Board, (t00s00NPC.derefBoard()));
			assertSame   ("t00s01NPC.derefBoard(): ", t01Board, (t00s01NPC.derefBoard()));
			assertSame   ("t00s02NPC.derefBoard(): ", t02Board, (t00s02NPC.derefBoard()));

			assertEquals("t00Board.getPlayCount(): ", 3, t00Board.getPlayCount());
			assertEquals("t01Board.getPlayCount(): ", 0, t01Board.getPlayCount());
			assertEquals("t02Board.getPlayCount(): ", 0, t02Board.getPlayCount());
			assertEquals("t11Board.getPlayCount(): ", 0, t11Board.getPlayCount());
			assertEquals("t12Board.getPlayCount(): ", 0, t12Board.getPlayCount());
			assertEquals("t22Board.getPlayCount(): ", 0, t22Board.getPlayCount());

			assertEquals("topBoard.getPlayCount(): ", 1, topBoard.getPlayCount());
		}
		{
			final Position t01s00NPC = t01Board.at(0,0).place(Token.PLAYER_BBB);
			assertNull("t01s00NPC: ", t01s00NPC);

			final Position t01s02NPC = t01Board.at(0,2).place(Token.PLAYER_AAA);
			final Position t01s12NPC = t01Board.at(1,2).place(Token.PLAYER_AAA);
			final Position t01s22NPC = t01Board.at(2,2).place(Token.PLAYER_AAA);
			assertNotNull("t01s02NPC: ", t01s02NPC);
			assertNotNull("t01s12NPC: ", t01s12NPC);
			assertNotNull("t01s22NPC: ", t01s22NPC);

			assertSame   ("t01s02NPC.derefBoard(): ", t02Board, (t01s02NPC.derefBoard()));
			assertSame   ("t01s12NPC.derefBoard(): ", t12Board, (t01s12NPC.derefBoard()));
			assertSame   ("t01s22NPC.derefBoard(): ", t22Board, (t01s22NPC.derefBoard()));

			assertEquals("t00Board.getPlayCount(): ", 3, t00Board.getPlayCount());
			assertEquals("t01Board.getPlayCount(): ", 4, t01Board.getPlayCount());
			assertEquals("t02Board.getPlayCount(): ", 0, t02Board.getPlayCount());
			assertEquals("t11Board.getPlayCount(): ", 0, t11Board.getPlayCount());
			assertEquals("t12Board.getPlayCount(): ", 0, t12Board.getPlayCount());
			assertEquals("t22Board.getPlayCount(): ", 0, t22Board.getPlayCount());

			assertEquals("topBoard.getPlayCount(): ", 2, topBoard.getPlayCount());
		}
	}

	@Test
	public void place_h3s2() {
		fail("NYI");
		fail("NYI - validate constraint");
	}

	// ====================================================================================================
	// tests other than from [PlayableTest]

	@Test @Deprecated
	public void accessor_isPlayable_h2s2_win() {

		final Board     topBoard  = new Board(2, 2);
		final Validator validator = new PlayableTest.Validator(topBoard);

		final Board     t00Board  = validator.add(topBoard.getSubBoard(0, 0));
		final Board     t01Board  = validator.add(topBoard.getSubBoard(0, 1));

		final Position  t10sxxPos = validator.add(topBoard.at(1, 0));
		final Position  t00s11Pos = validator.add(t00Board.at(1, 1));

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		validator.place(t00Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t00Board.at(1,0), Token.PLAYER_AAA, t00Board, t00s11Pos);

		validator.place(t01Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t01Board.at(0,1), Token.PLAYER_AAA, topBoard, t01Board, t10sxxPos);

		validator.checkAllClosed();
	}

	@Test @Deprecated
	public void accessor_isPlayable_h3s2_winning_SAVE() {
		final Board     topBoard   = new Board(3, 2);
		final Validator validator  = new PlayableTest.Validator(topBoard);

		final Board    t00Board    = validator.add(topBoard.getSubBoard(0, 0));
		final Board    t01Board    = validator.add(topBoard.getSubBoard(0, 1));
		final Board    t11Board    = validator.add(topBoard.getSubBoard(1, 1));

		final Board    t00s00Board = validator.add(t00Board.getSubBoard(0, 0));
		final Board    t00s01Board = validator.add(t00Board.getSubBoard(0, 1));
		final Board    t00s11Board = validator.add(t00Board.getSubBoard(1, 1));

		final Board    t01s00Board = validator.add(t01Board.getSubBoard(0, 0));
		final Board    t01s01Board = validator.add(t01Board.getSubBoard(0, 1));
		final Board    t01s11Board = validator.add(t01Board.getSubBoard(1, 1));

		final Board    t11s00Board = validator.add(t11Board.getSubBoard(0, 0));
		final Board    t11s01Board = validator.add(t11Board.getSubBoard(0, 1));
		final Board    t11s11Board = validator.add(t11Board.getSubBoard(1, 1));

		final Position t10Pos      = validator.add(topBoard.at(1,0));

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		validator.place(t00s00Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t00s00Board.at(1,0), Token.PLAYER_AAA, t00s00Board);

		validator.place(t00s01Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t00s01Board.at(1,0), Token.PLAYER_AAA, t00s01Board, t00s11Board, t00Board);

		validator.place(t01s00Board.at(0,0), Token.PLAYER_BBB);
		validator.place(t01s00Board.at(1,0), Token.PLAYER_BBB, t01s00Board);

		validator.place(t01s01Board.at(0,0), Token.PLAYER_BBB);
		validator.place(t01s01Board.at(1,0), Token.PLAYER_BBB, t01s01Board, t01s11Board, t01Board);

		validator.place(t11s00Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t11s00Board.at(1,0), Token.PLAYER_AAA, t11s00Board);

		validator.place(t11s01Board.at(0,0), Token.PLAYER_AAA);
		validator.place(t11s01Board.at(1,0), Token.PLAYER_AAA, t11s01Board, t11s11Board, t11Board, topBoard, t10Pos);

		validator.checkAllClosed();
	}

	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	@Test(expected=IllegalArgumentException.class)
	public void at_h1s3_bottom() {
		final Board    board  = new Board(1,3);
		final Position t00Pos = board.at(0,0);

		t00Pos.at(1,2);
	}

	@Test(expected=IllegalArgumentException.class)
	public void at_h2s3_bottom() {
		final Board    board     = new Board(2,3);
		final Position t00Pos    = board.at(0,0);
		final Position t00s00Pos = t00Pos.at(0,0);

		t00s00Pos.at(1,2);
	}

	@Test
	public void at_h2s3() {
		final Board    board     = new Board(2,3);

		final Board    t00Board  = board.getSubBoard(0, 0);
		final Position t00Pos    = board.at(0,0);
		assertSame("t00Pos.derefBoard(): ", t00Board, (t00Pos.derefBoard()));
	}

	@Test
	public void at_h3s4() {
		final Board    board     = new Board(3,4);

		final Board    t00Board  = board.getSubBoard(0, 0);
		final Position t00Pos    = board.at(0,0);
		assertSame("t00Pos.derefBoard(): ", t00Board, (t00Pos.derefBoard()));

		final Board    t00s12Board  = t00Board.getSubBoard(1,2);
		final Position t00s12Pos    = t00Pos.at(1,2);
		assertSame("t00s12Pos.derefBoard(): ", t00s12Board, (t00s12Pos.derefBoard()));
	}

	// ==============================================================================================================
	// printable text - ensure no surprises!

	@Test
	public void asPrintable_h1s3() {
		Board board = new Board(1,3);

		assertEquals("Position(board, 0, 0): ", "TOP(h1)~(0,0)", new Position(board, 0, 0).asPrintable());
		assertEquals("Position(board, 0, 1): ", "TOP(h1)~(0,1)", new Position(board, 0, 1).asPrintable());
		assertEquals("Position(board, 1, 1): ", "TOP(h1)~(1,1)", new Position(board, 1, 1).asPrintable());
	}

	@Test
	public void asPrintable_h2s2() {
		Board board = new Board(2,2);

		assertEquals("Position(board, 0, 0): ", "TOP(h2)~(0,0)", new Position(board, 0, 0).asPrintable());
		assertEquals("Position(board, 0, 1): ", "TOP(h2)~(0,1)", new Position(board, 0, 1).asPrintable());
		assertEquals("Position(board, 1, 1): ", "TOP(h2)~(1,1)", new Position(board, 1, 1).asPrintable());

		Board t01Board = board.getSubBoard(0, 1);
		assertEquals("Position(t01Board, 0, 0): ", "TOP(h2)~(0,1)~(0,0)", new Position(t01Board, 0, 0).asPrintable());
		assertEquals("Position(t01Board, 0, 1): ", "TOP(h2)~(0,1)~(0,1)", new Position(t01Board, 0, 1).asPrintable());
		assertEquals("Position(t01Board, 1, 1): ", "TOP(h2)~(0,1)~(1,1)", new Position(t01Board, 1, 1).asPrintable());
	}

	@Test
	public void asPrintable_h3s4() {
		Board board = new Board(3,4);

		assertEquals("Position(board, 0, 0): ", "TOP(h3)~(0,0)", new Position(board, 0, 0).asPrintable());
		assertEquals("Position(board, 0, 1): ", "TOP(h3)~(0,1)", new Position(board, 0, 1).asPrintable());
		assertEquals("Position(board, 1, 1): ", "TOP(h3)~(1,1)", new Position(board, 1, 1).asPrintable());

		Board t01Board = board.getSubBoard(0, 1);
		assertEquals("Position(t01Board, 0, 0): ", "TOP(h3)~(0,1)~(0,0)", new Position(t01Board, 0, 0).asPrintable());
		assertEquals("Position(t01Board, 0, 1): ", "TOP(h3)~(0,1)~(0,1)", new Position(t01Board, 0, 1).asPrintable());
		assertEquals("Position(t01Board, 1, 1): ", "TOP(h3)~(0,1)~(1,1)", new Position(t01Board, 1, 1).asPrintable());

		Board t01s11Board = t01Board.getSubBoard(1, 1);
		assertEquals("Position(t01s11Board, 0, 0): ", "TOP(h3)~(0,1)~(1,1)~(0,0)", new Position(t01s11Board, 0, 0).asPrintable());
		assertEquals("Position(t01s11Board, 0, 1): ", "TOP(h3)~(0,1)~(1,1)~(0,1)", new Position(t01s11Board, 0, 1).asPrintable());
		assertEquals("Position(t01s11Board, 1, 1): ", "TOP(h3)~(0,1)~(1,1)~(1,1)", new Position(t01s11Board, 1, 1).asPrintable());
	}
}
