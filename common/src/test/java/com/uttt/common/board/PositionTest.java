package com.uttt.common.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

public class PositionTest {

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

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

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

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	@Test
	public void isPlayable_h1s3() {
		Board    board = new Board(1, 3);
		Position pos   = new Position(board, 1, 2);

		assertTrue("new board: ", pos.isPlayable());

		board.updatePosition(Token.PLAYER_AAA,0,0);
		assertTrue("AAA at (0,0): ", pos.isPlayable());

		board.updatePosition(Token.PLAYER_AAA,0,1);
		assertTrue("AAA at (0,1): ", pos.isPlayable());

		board.updatePosition(Token.PLAYER_AAA,0,2);
		assertFalse("AAA wins (0,*): ", pos.isPlayable());
	}

	@Test
	public void isPlayable_h2s2() {
		final Board    topBoard = new Board(2, 2);
		final Board    t00Board = topBoard.getSubBoard(0, 0);
		final Board    t01Board = topBoard.getSubBoard(0, 1);

		final Position t10sxxPos  = new Position(topBoard, 1, 0);
		final Position t11s11Pos  = new Position(t00Board, 1, 1);

		t00Board.updatePosition(Token.PLAYER_AAA,0,0);
		assertTrue ("AAA at T(0,0)-(0,0): topBoard .isPlayable()", (topBoard .isPlayable()));
		assertTrue ("AAA at T(0,0)-(0,0): t00Board .isPlayable()", (t00Board .isPlayable()));
		assertTrue ("AAA at T(0,0)-(0,0): t01Board .isPlayable()", (t01Board .isPlayable()));
		assertTrue ("AAA at T(0,0)-(0,0): t10sxxPos.isPlayable()", (t10sxxPos.isPlayable()));
		assertTrue ("AAA at T(0,0)-(0,0): t11s11Pos.isPlayable()", (t11s11Pos.isPlayable()));

		t00Board.updatePosition(Token.PLAYER_AAA,1,0);
		assertTrue ("AAA at T(0,0)-(1,0): topBoard .isPlayable()", (topBoard .isPlayable()));
		assertFalse("AAA at T(0,0)-(1,0): t00Board .isPlayable()", (t00Board .isPlayable()));
		assertTrue ("AAA at T(0,0)-(1,0): t01Board .isPlayable()", (t01Board .isPlayable()));
		assertTrue ("AAA at T(0,0)-(1,0): t10sxxPos.isPlayable()", (t10sxxPos.isPlayable()));
		assertFalse("AAA at T(0,0)-(1,0): t11s11Pos.isPlayable()", (t11s11Pos.isPlayable()));

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		t01Board.updatePosition(Token.PLAYER_AAA,0,0);
		assertTrue ("AAA at T(0,1)-(0,0): topBoard .isPlayable()", (topBoard .isPlayable()));
		assertFalse("AAA at T(0,1)-(0,0): t00Board .isPlayable()", (t00Board .isPlayable()));
		assertTrue ("AAA at T(0,1)-(0,0): t01Board .isPlayable()", (t01Board .isPlayable()));
		assertTrue ("AAA at T(0,1)-(0,0): t10sxxPos.isPlayable()", (t10sxxPos.isPlayable()));
		assertFalse("AAA at T(0,1)-(0,0): t11s11Pos.isPlayable()", (t11s11Pos.isPlayable()));

		t01Board.updatePosition(Token.PLAYER_AAA,0,1);
		assertFalse("AAA at T(0,1)-(0,1): topBoard .isPlayable()", (topBoard .isPlayable()));
		assertFalse("AAA at T(0,1)-(0,1): t00Board .isPlayable()", (t00Board .isPlayable()));
		assertFalse("AAA at T(0,1)-(0,1): t01Board .isPlayable()", (t01Board .isPlayable()));
		assertFalse("AAA at T(0,1)-(0,1): t10sxxPos.isPlayable()", (t10sxxPos.isPlayable()));
		assertFalse("AAA at T(0,1)-(0,1): t11s11Pos.isPlayable()", (t11s11Pos.isPlayable()));
	}

	private static void assertPlayability(Board topBoard, Set<Board> playables, Set<Board> unplayables, String pfx, Board... newlyUnplayable) {
		if (newlyUnplayable.length > 0) {
			playables.removeAll(Arrays.asList(newlyUnplayable));
			unplayables.addAll (Arrays.asList(newlyUnplayable));
		}

		for (Board board : playables) {
			if (!board.isPlayable()) {
				Position position = board.getPosition();
				String posString  = (position == null ? "TOP." : position.asPrintable());
				String msg = pfx + ": board not playable: " + posString + "\n" + topBoard.fieldAsPrintableString();
				org.junit.Assert.fail(msg);
			}
		}

		for (Board board : unplayables) {
			if (board.isPlayable()) {
				Position position = board.getPosition();
				String posString  = (position == null ? "TOP." : position.asPrintable());
				String msg = pfx + ": board is playable: " + posString + "\n" + topBoard.fieldAsPrintableString();
				org.junit.Assert.fail(msg);
			}
		}
	}

	@Test
	public void isPlayable_h3s2() {
		final Board    topBoard    = new Board(3, 2);

		final Board    t00Board    = topBoard.getSubBoard(0, 0);
		final Board    t01Board    = topBoard.getSubBoard(0, 1);
		final Board    t11Board    = topBoard.getSubBoard(1, 1);

		final Board    t00s00Board = t00Board.getSubBoard(0, 0);
		final Board    t00s01Board = t00Board.getSubBoard(0, 1);
		final Board    t00s11Board = t00Board.getSubBoard(1, 1);

		final Board    t01s00Board = t01Board.getSubBoard(0, 0);
		final Board    t01s01Board = t01Board.getSubBoard(0, 1);
		final Board    t01s11Board = t01Board.getSubBoard(1, 1);

		final Board    t11s00Board = t11Board.getSubBoard(0, 0);
		final Board    t11s01Board = t11Board.getSubBoard(0, 1);
		final Board    t11s11Board = t11Board.getSubBoard(1, 1);

		final Set<Board> unplayableBoards = new LinkedHashSet<>();
		final Set<Board> playableBoards   = new LinkedHashSet<>();
		playableBoards.addAll(Arrays.asList( //
				topBoard,

				t00Board,
				t01Board,
				t11Board,

				t00s00Board,
				t00s01Board,
				t00s11Board,

				t01s00Board,
				t01s01Board,
				t01s11Board,

				t11s00Board,
				t11s01Board,
				t11s11Board
		));

		assertPlayability(topBoard, playableBoards, unplayableBoards, "new board");

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		t00s00Board.updatePosition(Token.PLAYER_AAA,0,0);
		assertPlayability(topBoard, playableBoards, unplayableBoards, "AAA at t00s11Board~(0,0)");

		t00s00Board.updatePosition(Token.PLAYER_AAA,1,0);
		assertPlayability(topBoard, playableBoards, unplayableBoards, "AAA at t00s11Board~(1,0)", t00s00Board);

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		t00s01Board.updatePosition(Token.PLAYER_AAA,0,0);
		assertPlayability(topBoard, playableBoards, unplayableBoards, "AAA at t00s01Board~(0,0)");

		t00s01Board.updatePosition(Token.PLAYER_AAA,1,0);
		assertPlayability(topBoard, playableBoards, unplayableBoards, "AAA at t00s01Board~(1,0)", t00s01Board, t00s11Board, t00Board);

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		t01s00Board.updatePosition(Token.PLAYER_BBB,0,0);
		assertPlayability(topBoard, playableBoards, unplayableBoards, "BBB at t01s00Board~(0,0)");

		t01s00Board.updatePosition(Token.PLAYER_BBB,1,0);
		assertPlayability(topBoard, playableBoards, unplayableBoards, "BBB at t01s00Board~(1,0)", t01s00Board);

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		t01s01Board.updatePosition(Token.PLAYER_BBB,0,0);
		assertPlayability(topBoard, playableBoards, unplayableBoards, "BBB at t01s01Board~(0,0)");

		t01s01Board.updatePosition(Token.PLAYER_BBB,1,0);
		assertPlayability(topBoard, playableBoards, unplayableBoards, "BBB at t01s01Board~(1,0)", t01s01Board, t01s11Board, t01Board);

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		t11s00Board.updatePosition(Token.PLAYER_AAA,0,0);
		assertPlayability(topBoard, playableBoards, unplayableBoards, "AAA at t11s00Board~(0,0)");

		t11s00Board.updatePosition(Token.PLAYER_AAA,1,0);
		assertPlayability(topBoard, playableBoards, unplayableBoards, "AAA at t11s00Board~(1,0)", t11s00Board);

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		t11s01Board.updatePosition(Token.PLAYER_AAA,0,0);
		assertPlayability(topBoard, playableBoards, unplayableBoards, "AAA at t11s01Board~(0,0)");

		t11s01Board.updatePosition(Token.PLAYER_AAA,1,0);
		assertPlayability(topBoard, playableBoards, unplayableBoards, "AAA at t11s01Board~(1,0)", t11s01Board, t11s11Board, t11Board, topBoard);

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	}
}
