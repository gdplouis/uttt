package com.uttt.common.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
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


	// ====================================================================================================

	@Test
	public void place_h1s3() {
		final Board topBoard = new Board(1, 3);

		final Position t00Pos = topBoard.at(0,0);
		t00Pos.place(Token.PLAYER_AAA);

		final Position t12Pos = topBoard.at(1,2);
		t12Pos.place(Token.PLAYER_BBB);

		assertEquals("topBoard.getPlayCount(): ", 2, topBoard.getPlayCount());

		assertEquals("topBoard.getSubToken(0,0): ", Token.PLAYER_AAA, topBoard.getSubToken(0,0));
		assertEquals("topBoard.getSubToken(1,2): ", Token.PLAYER_BBB, topBoard.getSubToken(1,2));
	}

	// ====================================================================================================

	@Override
	@Test
	public void isPlayable_h1s3_win() {
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
	public void isPlayable_h2s3_win() {

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
	public void isPlayable_h3s4_win() {

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
	public void isPlayable_h1s3_draw() {

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
	public void isPlayable_h2s3_draw() {
		fail("NYI");
	}

	@Override
	@Test @Ignore
	public void isPlayable_h3s4_draw() {
		fail("NYI");
	}

	// ====================================================================================================
	// other win test(s)

	@Test
	public void isPlayable_h2s2_win() {

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

	@Test
	public void isPlayable_h3s2_winning_SAVE() {
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
