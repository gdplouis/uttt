package com.uttt.common.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

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

		final Position t12Pos = validator.add(topBoard.at(1, 2));

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		validator.place(topBoard.at(0, 0), Token.PLAYER_AAA);
		validator.place(topBoard.at(0, 1), Token.PLAYER_AAA);
		validator.place(topBoard.at(0, 2), Token.PLAYER_BBB);

		validator.place(topBoard.at(1, 0), Token.PLAYER_BBB);
		validator.place(topBoard.at(1, 1), Token.PLAYER_BBB);
		validator.place(topBoard.at(1, 2), Token.PLAYER_AAA, t12Pos);

		validator.place(topBoard.at(2, 0), Token.PLAYER_AAA);
		validator.place(topBoard.at(2, 1), Token.PLAYER_AAA);
		validator.place(topBoard.at(2, 2), Token.PLAYER_BBB, topBoard);

		validator.checkAllClosed();
	}

	@Override
	@Test
	public void accessor_isPlayable_h2s3_draw() {

		final Board     topBoard  = new Board(2, 3);
		final Validator validator = new PlayableTest.Validator(topBoard);

		final Board t00Board  = validator.add(topBoard.getSubBoard(0, 0));
		final Board t01Board  = validator.add(topBoard.getSubBoard(0, 1));
		final Board t02Board  = validator.add(topBoard.getSubBoard(0, 2));
		final Board t10Board  = validator.add(topBoard.getSubBoard(1, 0));
		final Board t11Board  = validator.add(topBoard.getSubBoard(1, 1));
		final Board t12Board  = validator.add(topBoard.getSubBoard(1, 2));
		final Board t20Board  = validator.add(topBoard.getSubBoard(2, 0));
		final Board t21Board  = validator.add(topBoard.getSubBoard(2, 1));
		final Board t22Board  = validator.add(topBoard.getSubBoard(2, 2));

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		validator.checkClosures("t00Board: ", BoardTestUtil.forceDraw(t00Board));
		validator.checkClosures("t01Board: ", BoardTestUtil.forceDraw(t01Board));
		validator.checkClosures("t02Board: ", BoardTestUtil.forceDraw(t02Board));
		validator.checkClosures("t10Board: ", BoardTestUtil.forceDraw(t10Board));
		validator.checkClosures("t11Board: ", BoardTestUtil.forceDraw(t11Board));
		validator.checkClosures("t12Board: ", BoardTestUtil.forceDraw(t12Board));
		validator.checkClosures("t20Board: ", BoardTestUtil.forceDraw(t20Board));
		validator.checkClosures("t21Board: ", BoardTestUtil.forceDraw(t21Board));
		validator.checkClosures("t22Board: ", BoardTestUtil.forceDraw(t22Board), topBoard);

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		validator.checkAllClosed();
	}

	@Override
	@Test
	public void accessor_isPlayable_h3s4_draw() {

		final Board     topBoard  = new Board(3, 4);
		final Validator validator = new PlayableTest.Validator(topBoard);

		// the testing loop depends on the addAllFromTop using an insert-order-preseving map

		LinkedHashMap<String, Board> boards = validator.addAllFromTop();

		for (final Entry<String, Board> e : boards.entrySet()) {
			Board  board = e.getValue();

			if (board.isBottom()) {
				BoardTestUtil.forceDraw(board);

				List<Board> newlyClosed = new LinkedList<>();
				newlyClosed.add(board);

				while ((board.getPosition() != null) && (board.getPosition().getRow() == 3) && (board.getPosition().getCol() == 3)) {
					newlyClosed.add(board); // okay if this board is twice on the newlyClosed list
					board = board.getParent();

					if (board.getParent() == null) {
						newlyClosed.add(boards.get("top"));
					}
				}

				validator.checkClosures(e.getKey() , newlyClosed.toArray(new Board[0]));
			}
		}

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		validator.checkAllClosed();
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

	private Position n(Board b) { return b.at(0, 0); }
	private Position s(Board b) { return b.at(1, 1); }
	private Position e(Board b) { return b.at(0, 1); }
	private Position w(Board b) { return b.at(1, 0); }

	@Test
	public void place_h3s2() {
		final Board t__Top = new Board(3, 2);
		assertEquals("topBoard.getPlayCount(): ", 0, t__Top.getPlayCount());

		final Board tn_Mid = t__Top.getSubBoard(0, 0);
		final Board ts_Mid = t__Top.getSubBoard(1, 1);
		final Board te_Mid = t__Top.getSubBoard(0, 1);
		final Board tw_Mid = t__Top.getSubBoard(1, 0);

		final Board tnnBtm = tn_Mid.getSubBoard(0, 0);
		final Board tnsBtm = tn_Mid.getSubBoard(1, 1);
		final Board tneBtm = tn_Mid.getSubBoard(0, 1);
		final Board tnwBtm = tn_Mid.getSubBoard(1, 0);

		final Board tsnBtm = ts_Mid.getSubBoard(0, 0);
		final Board tssBtm = ts_Mid.getSubBoard(1, 1);
		final Board tseBtm = ts_Mid.getSubBoard(0, 1);
		final Board tswBtm = ts_Mid.getSubBoard(1, 0);

		final Board tenBtm = te_Mid.getSubBoard(0, 0);
		final Board tesBtm = te_Mid.getSubBoard(1, 1);
		final Board teeBtm = te_Mid.getSubBoard(0, 1);
		final Board tewBtm = te_Mid.getSubBoard(1, 0);

		final Board twnBtm = tw_Mid.getSubBoard(0, 0);
		final Board twsBtm = tw_Mid.getSubBoard(1, 1);
		final Board tweBtm = tw_Mid.getSubBoard(0, 1);
		final Board twwBtm = tw_Mid.getSubBoard(1, 0);

		{{
			final Position nnnNpc = n(tnnBtm).place(Token.PLAYER_AAA);
			assertNotNull("nnnNpc: ", nnnNpc);
			assertSame   ("nnnNpc.derefBoard(): ", tnnBtm, (nnnNpc.derefBoard()));
			assertEquals ("tnnBtm.getPlayCount(): ", 1, tnnBtm.getPlayCount());
			assertEquals ("tneBtm.getPlayCount(): ", 0, tneBtm.getPlayCount());
			assertEquals ("tneBtm.getPlayCount(): ", 0, tnwBtm.getPlayCount());
			assertEquals ("tn_Mid.getPlayCount(): ", 0, tn_Mid.getPlayCount());
			assertEquals ("t__Top.getPlayCount(): ", 0, t__Top.getPlayCount());
		}{
			final Position nneNpc = e(tnnBtm).place(Token.PLAYER_BBB);
			assertNotNull("nneNpc: ", nneNpc);
			assertSame   ("nneNpc.derefBoard(): ", tneBtm, (nneNpc.derefBoard()));
			assertEquals ("tnnBtm.getPlayCount(): ", 2, tnnBtm.getPlayCount());
			assertEquals ("tneBtm.getPlayCount(): ", 0, tneBtm.getPlayCount());
			assertEquals ("tneBtm.getPlayCount(): ", 0, tnwBtm.getPlayCount());
			assertEquals ("tn_Mid.getPlayCount(): ", 0, tn_Mid.getPlayCount());
			assertEquals ("t__Top.getPlayCount(): ", 0, t__Top.getPlayCount());
		}{
			final Position nnsNpc = s(tnnBtm).place(Token.PLAYER_AAA);
			assertNotNull("nnsNpc: ", nnsNpc);
			assertSame   ("nnsNpc.derefBoard(): ", tnsBtm, (nnsNpc.derefBoard()));
			assertEquals ("tnnBtm.getPlayCount(): ", 3, tnnBtm.getPlayCount());
			assertEquals ("tneBtm.getPlayCount(): ", 0, tneBtm.getPlayCount());
			assertEquals ("tneBtm.getPlayCount(): ", 0, tnwBtm.getPlayCount());
			assertEquals ("tn_Mid.getPlayCount(): ", 1, tn_Mid.getPlayCount());
			assertEquals ("t__Top.getPlayCount(): ", 0, t__Top.getPlayCount());
		}{
			final Position nenNpc = n(tneBtm).place(Token.PLAYER_BBB);
			assertNotNull("nenNpc: ", nenNpc);
			assertSame   ("nenNpc.derefBoard(): ", tn_Mid, (nenNpc.derefBoard()));

			assertEquals ("tnnBtm.getPlayCount(): ", 3, tnnBtm.getPlayCount());
			assertEquals ("tneBtm.getPlayCount(): ", 1, tneBtm.getPlayCount());
			assertEquals ("tneBtm.getPlayCount(): ", 0, tnwBtm.getPlayCount());
			assertEquals ("tn_Mid.getPlayCount(): ", 1, tn_Mid.getPlayCount());
			assertEquals ("t__Top.getPlayCount(): ", 0, t__Top.getPlayCount());
		}{
			final Position neeNpc = e(tneBtm).place(Token.PLAYER_BBB);
			assertNotNull("neeNpc: ", neeNpc);
			assertSame   ("neeNpc.derefBoard(): ", tn_Mid, (neeNpc.derefBoard()));

			assertEquals ("tnnBtm.getPlayCount(): ", 3, tnnBtm.getPlayCount());
			assertEquals ("tneBtm.getPlayCount(): ", 2, tneBtm.getPlayCount());
			assertEquals ("tneBtm.getPlayCount(): ", 0, tnwBtm.getPlayCount());
			assertEquals ("tn_Mid.getPlayCount(): ", 2, tn_Mid.getPlayCount());
			assertEquals ("t__Top.getPlayCount(): ", 0, t__Top.getPlayCount());
		}{
			final Position nwnNpc = n(tnwBtm).place(Token.PLAYER_AAA);
			assertNotNull("nwnNpc: ", nwnNpc);
			assertSame   ("nwnNpc.derefBoard(): ", tn_Mid, (nwnNpc.derefBoard()));

			assertEquals ("tnnBtm.getPlayCount(): ", 3, tnnBtm.getPlayCount());
			assertEquals ("tneBtm.getPlayCount(): ", 2, tneBtm.getPlayCount());
			assertEquals ("tneBtm.getPlayCount(): ", 1, tnwBtm.getPlayCount());
			assertEquals ("tn_Mid.getPlayCount(): ", 2, tn_Mid.getPlayCount());
			assertEquals ("t__Top.getPlayCount(): ", 0, t__Top.getPlayCount());
		}{
			final Position nweNpc = e(tnwBtm).place(Token.PLAYER_AAA);
			assertNull   ("nweNpc: ", nweNpc);

			assertEquals ("tnnBtm.getPlayCount(): ", 3, tnnBtm.getPlayCount());
			assertEquals ("tneBtm.getPlayCount(): ", 2, tneBtm.getPlayCount());
			assertEquals ("tneBtm.getPlayCount(): ", 2, tnwBtm.getPlayCount());
			assertEquals ("tn_Mid.getPlayCount(): ", 3, tn_Mid.getPlayCount());
			assertEquals ("t__Top.getPlayCount(): ", 1, t__Top.getPlayCount());
		}}
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

		assertEquals("Position(board, 0, 0): ", "TOP/h1:~(0,0)", new Position(board, 0, 0).toString());
		assertEquals("Position(board, 0, 1): ", "TOP/h1:~(0,1)", new Position(board, 0, 1).toString());
		assertEquals("Position(board, 1, 1): ", "TOP/h1:~(1,1)", new Position(board, 1, 1).toString());
	}

	@Test
	public void asPrintable_h2s2() {
		Board board = new Board(2,2);

		assertEquals("Position(board, 0, 0): ", "TOP/h2:~(0,0)", new Position(board, 0, 0).toString());
		assertEquals("Position(board, 0, 1): ", "TOP/h2:~(0,1)", new Position(board, 0, 1).toString());
		assertEquals("Position(board, 1, 1): ", "TOP/h2:~(1,1)", new Position(board, 1, 1).toString());

		Board t01Board = board.getSubBoard(0, 1);
		assertEquals("Position(t01Board, 0, 0): ", "TOP/h2:~(0,1)~(0,0)", new Position(t01Board, 0, 0).toString());
		assertEquals("Position(t01Board, 0, 1): ", "TOP/h2:~(0,1)~(0,1)", new Position(t01Board, 0, 1).toString());
		assertEquals("Position(t01Board, 1, 1): ", "TOP/h2:~(0,1)~(1,1)", new Position(t01Board, 1, 1).toString());
	}

	@Test
	public void asPrintable_h3s4() {
		Board board = new Board(3,4);

		assertEquals("Position(board, 0, 0): ", "TOP/h3:~(0,0)", new Position(board, 0, 0).toString());
		assertEquals("Position(board, 0, 1): ", "TOP/h3:~(0,1)", new Position(board, 0, 1).toString());
		assertEquals("Position(board, 1, 1): ", "TOP/h3:~(1,1)", new Position(board, 1, 1).toString());

		Board t01Board = board.getSubBoard(0, 1);
		assertEquals("Position(t01Board, 0, 0): ", "TOP/h3:~(0,1)~(0,0)", new Position(t01Board, 0, 0).toString());
		assertEquals("Position(t01Board, 0, 1): ", "TOP/h3:~(0,1)~(0,1)", new Position(t01Board, 0, 1).toString());
		assertEquals("Position(t01Board, 1, 1): ", "TOP/h3:~(0,1)~(1,1)", new Position(t01Board, 1, 1).toString());

		Board t01s11Board = t01Board.getSubBoard(1, 1);
		assertEquals("Position(t01s11Board, 0, 0): ", "TOP/h3:~(0,1)~(1,1)~(0,0)", new Position(t01s11Board, 0, 0).toString());
		assertEquals("Position(t01s11Board, 0, 1): ", "TOP/h3:~(0,1)~(1,1)~(0,1)", new Position(t01s11Board, 0, 1).toString());
		assertEquals("Position(t01s11Board, 1, 1): ", "TOP/h3:~(0,1)~(1,1)~(1,1)", new Position(t01s11Board, 1, 1).toString());
	}
}
