package com.uttt.common.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.uttt.common.Foreachable;
import com.uttt.common.board.Node.Status;

public class BoardTest extends NodeTest {

	@Test
	public void constructor_standard() {
		Board board = new Board(2, 3);

		assertEquals("board.getHeight(): ", 2, board.getHeight());
		assertEquals("board.getSize(): "  , 3, board.getSize());

		Board topNode = board.getSubBoard(1, 2);
		assertEquals("topNode.getClass(): ", Board.class, topNode.getClass());

		Token bottomNode = topNode.getSubToken(1, 2);
		assertEquals("bottomNode.getClass(): ", Token.class, bottomNode.getClass());
	}

	@Test(expected=IllegalArgumentException.class)
	public void constructor_tooShallow() {
		@SuppressWarnings("unused")
		Board board = new Board(0, 999);
	}

	@Test(expected=IllegalArgumentException.class)
	public void constructor_tooTall() {
		@SuppressWarnings("unused")
		Board board = new Board(10, 999);
	}

	@Test(expected=IllegalArgumentException.class)
	public void constructor_tooNarrow() {
		@SuppressWarnings("unused")
		Board board = new Board(1, 1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void constructor_tooWide() {
		@SuppressWarnings("unused")
		Board board = new Board(1, 999);
	}

	// ====================================================================================================

	@Override
	@Test
	public void accessors_getHeight() {
		assertEquals("board(1,3).getHeight(): ", 1, new Board(1,3).getHeight());
		assertEquals("board(2,2).getHeight(): ", 2, new Board(2,2).getHeight());
		assertEquals("board(3,4).getHeight(): ", 3, new Board(3,4).getHeight());
	}

	@Override
	@Test
	public void accessors_getSubNode() {
		Board board = new Board(1, 3);

		Node[][] field = board.getField();

		for (int x : Foreachable.until(board.getSize())) {
			for (int y : Foreachable.until(board.getSize())) {
				assertSame("board.getHeight(): ", field[x][y], board.getSubNode(x,y));
			}
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void accessors_getSubBoard_atBottom() {
		final int expectedRow = 1;
		final int expectedCol = 2;

		Board  board = new Board(2, 3).getSubBoard(expectedRow, expectedCol);

		@SuppressWarnings("unused")
		Board  subBoard = board.getSubBoard(expectedRow, expectedCol);
	}

	@Test
	public void accessors_getSubBoard() {
		final int expectedRow = 1;
		final int expectedCol = 2;

		Board board = new Board(2, 3);

		Board    subBoard = board.getSubBoard(expectedRow, expectedCol);
		Position subPos   = subBoard.getPosition();

		assertSame  ("subPos.getBoard(): ", board      , subPos.getBoard());
		assertEquals("subPos.getRow(): "  , expectedRow, subPos.getRow());
		assertEquals("subPos.getCol(): "  , expectedCol, subPos.getCol());
	}

	@Test(expected=IllegalArgumentException.class)
	public void accessors_getSubToken_aboveBottom() {
		final int expectedRow = 1;
		final int expectedCol = 2;

		Board  board = new Board(2, 3);

		@SuppressWarnings("unused")
		Token  subToken = board.getSubToken(expectedRow, expectedCol);
	}

	@Test
	public void accessors_getSubToken() {
		final int expectedRow = 1;
		final int expectedCol = 2;

		Board board = new Board(2, 3).getSubBoard(0, 0);

		Token    subToken = board.getSubToken(expectedRow, expectedCol);

		assertEquals("subToken: "  , Token.EMPTY, subToken);
	}

	@Test
	public void accessor_getTopBoard_h1s3() {
		final Board  board = new Board(1, 3);

		assertSame("board.getTopBoard(): ", board, board.getTopBoard());
	}

	@Test
	public void accessor_getTopBoard_h2s2() {
		final Board  board = new Board(2, 2);

		assertSame("board.getTopBoard(): ",                  board, board.getTopBoard());
		assertSame("board.getSubBoard(0,0).getTopBoard(): ", board, board.getSubBoard(0,0).getTopBoard());
	}


	@Test
	public void accessor_getTopBoard_h3s4() {
		final Board  board = new Board(3, 4);

		assertSame("TOP: ",             board, board.getTopBoard());
		assertSame("TOP~(0,0): ",       board, board.getSubBoard(0,0).getTopBoard());
		assertSame("TOP~(0,0)~(1,1): ", board, board.getSubBoard(0,0).getSubBoard(1,1).getTopBoard());
	}

	@Test
	public void accessors_getSize() {
		Board board = new Board(1, 3);

		assertEquals("board.getSize(): "  , 3, board.getSize());
	}

	@Test
	public void accessors_getField() {
		Board board = new Board(1, 3);

		Node[][] field = board.getField();

		assertEquals("field.length: ", 3, field.length);
		for (int row : Foreachable.until(board.getSize())) {
			assertEquals("field["+row+"].length: ", 3, field[row].length);
		}
	}

	// ====================================================================================================

	@Test
	public void place_h1s3() {
		Board board = new Board(1, 3);

		Position contraint = board.at(1, 2).place(Token.PLAYER_AAA);

		assertEquals("board.getSubNode(1, 2, Token.class): ", Token.PLAYER_AAA, board.getSubToken(1, 2));
		assertNull("contraint", contraint);
	}

	@Test(expected=IllegalArgumentException.class)
	public void place_h1s3_alreadyFilled() {
		Board board = new Board(1, 3);

		board.at(1, 2).place(Token.PLAYER_AAA);
		board.at(1, 2).place(Token.PLAYER_AAA);
	}

	@Test(expected=IllegalArgumentException.class)
	public void place_h2s2_alreadyFilled() {
		Board board = new Board(2,2);

		board.getSubBoard(0,0).at(1, 2).place(Token.PLAYER_AAA);
		board.getSubBoard(0,0).at(1, 2).place(Token.PLAYER_AAA);
	}

	@Test(expected=IllegalArgumentException.class)
	public void place_h2s3_alreadyWonAtLevel1() {
		Board board    = new Board(2, 3);

		board.getSubBoard(0,0).at(0, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(0,0).at(0, 1).place(Token.PLAYER_AAA);
		board.getSubBoard(0,0).at(0, 2).place(Token.PLAYER_AAA);

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		board.getSubBoard(0,0).at(2, 2).place(Token.PLAYER_BBB);
	}

	@Test(expected=IllegalArgumentException.class)
	public void place_h3s3_alreadyWonAtLevel2() {
		Board board    = new Board(3, 3);

		Board level2winner = board.getSubBoard(0, 0);

		level2winner.getSubBoard(0, 0).at(0, 0).place(Token.PLAYER_AAA);
		level2winner.getSubBoard(0, 0).at(0, 1).place(Token.PLAYER_AAA);
		level2winner.getSubBoard(0, 0).at(0, 2).place(Token.PLAYER_AAA);

		level2winner.getSubBoard(0, 1).at(0, 0).place(Token.PLAYER_AAA);
		level2winner.getSubBoard(0, 1).at(0, 1).place(Token.PLAYER_AAA);
		level2winner.getSubBoard(0, 1).at(0, 2).place(Token.PLAYER_AAA);

		level2winner.getSubBoard(0, 2).at(0, 0).place(Token.PLAYER_AAA);
		level2winner.getSubBoard(0, 2).at(0, 1).place(Token.PLAYER_AAA);
		level2winner.getSubBoard(0, 2).at(0, 2).place(Token.PLAYER_AAA);

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		level2winner.getSubBoard(2, 2).at(2, 1).place(Token.PLAYER_BBB);
	}

	// ====================================================================================================

	@Test()
	public void copyDeep_h1s3() {
		final Board orig = new Board(1, 3);
		{
			final Board copy = orig.copyDeep();
			assertEquals("empty board: ", orig, copy);
		}
		{
			orig.at(0,1).place(Token.PLAYER_AAA);

			final Board copy = orig.copyDeep();
			assertEquals("AAA at (0,1): ", orig, copy);
		}
		{
			orig.at(1,2).place(Token.PLAYER_BBB);

			final Board copy = orig.copyDeep();
			assertEquals("BBB at (2,2): ", orig, copy);
		}
		{
			orig.at(1,1).place(Token.PLAYER_AAA);

			final Board copy = orig.copyDeep();
			assertEquals("AAA at (1,1): ", orig, copy);
		}
		{
			orig.at(2,1).place(Token.PLAYER_BBB);

			final Board copy = orig.copyDeep();
			assertEquals("BBB at (2,1): ", orig, copy);
		}
		{
			final Board draw = orig.copyDeep();

			draw.at(2,0).place(Token.PLAYER_AAA);
			draw.at(0,2).place(Token.PLAYER_BBB);
			draw.at(2,2).place(Token.PLAYER_AAA);
			draw.at(0,0).place(Token.PLAYER_BBB);
			draw.at(1,0).place(Token.PLAYER_AAA);
			assertEquals("draw (orig): ", Status.DRAW, draw.getStatus());

			final Board copy = draw.copyDeep();
			assertEquals("draw (copy): ", Status.DRAW, copy.getStatus());
		}
		{
			orig.at(0,0).place(Token.PLAYER_AAA);

			final Board copy = orig.copyDeep();
			assertEquals("AAA at (0,0): ", orig, copy);
		}
		{
			orig.at(2,2).place(Token.PLAYER_BBB);

			final Board copy = orig.copyDeep();
			assertEquals("BBB at (2,2): ", orig, copy);
		}
		{
			orig.at(0,2).place(Token.PLAYER_AAA);
			assertEquals("AAA wins (orig): ", Status.WINNER_AAA, orig.getStatus());

			final Board copy = orig.copyDeep();
			assertEquals("AAA at (0,2): ", orig, copy);
			assertEquals("AAA wins (copy): ", Status.WINNER_AAA, copy.getStatus());
		}
	}

	@Test()
	public void copyDeep_h2s3() {
		final Board orig = new Board(2, 3);
		{
			final Board copy = orig.copyDeep();
			assertEquals("empty board: ", orig, copy);
		}

		Position[] playPositions = new Position[] { //
				orig.at(0,0).at(0,0), // AAA
				orig.at(0,0).at(1,0), // BBB
				orig.at(0,0).at(0,1), // AAA
				orig.at(0,0).at(1,1), // BBB
				orig.at(0,0).at(0,2), // AAA - wins btm

				orig.at(0,2).at(0,0), // BBB - just to flip token

				orig.at(1,1).at(0,0), // AAA
				orig.at(1,1).at(0,1), // BBB
				orig.at(1,1).at(0,2), // AAA
				orig.at(1,1).at(2,0), // BBB
				orig.at(1,1).at(2,1), // AAA
				orig.at(1,1).at(2,2), // BBB
				orig.at(1,1).at(1,0), // AAA
				orig.at(1,1).at(1,1), // BBB
				orig.at(1,1).at(1,2), // AAA - draw

				orig.at(0,2).at(1,1), // BBB - just to flip token

				orig.at(1,0).at(0,0), // AAA
				orig.at(1,0).at(1,0), // BBB
				orig.at(1,0).at(0,1), // AAA
				orig.at(1,0).at(1,1), // BBB
				orig.at(1,0).at(0,2), // AAA - wins btm

				orig.at(0,2).at(2,2), // BBB - just to flip token (also wins for BBB)

				orig.at(2,0).at(0,0), // AAA
				orig.at(2,0).at(1,0), // BBB
				orig.at(2,0).at(0,1), // AAA
				orig.at(2,0).at(1,1), // BBB
				orig.at(2,0).at(0,2), // AAA - wins btm; wins top
		};

		Token token = Token.PLAYER_AAA;
		for (Position play : playPositions) {
			play.place(token);

			final Board copy = orig.copyDeep();
			assertEquals("play=[" + play + "].place(" + token + "): ", orig, copy);

			token = BoardTestUtil.flip(token);
		}
		assertEquals("final status: ", Status.WINNER_AAA, orig.getStatus());
	}

	@Test()
	public void copyDeep_h3s4() {
		final Board orig = new Board(3, 4);
		{
			final Board copy = orig.copyDeep();
			assertEquals("empty board: ", orig, copy);
		}

		Position[] playPositions = new Position[] { //
				orig.at(0,0).at(0,0), // AAA
				orig.at(0,0).at(1,0), // BBB
				orig.at(0,0).at(0,1), // AAA
				orig.at(0,0).at(1,1), // BBB
				orig.at(0,0).at(0,2), // AAA
				orig.at(0,0).at(1,2), // BBB
				orig.at(0,0).at(0,3), // AAA - wins mid

				orig.at(0,2).at(0,0), // BBB - just to flip token

				orig.at(1,1).at(0,0), // AAA
				orig.at(1,1).at(0,1), // BBB
				orig.at(1,1).at(0,2), // AAA
				orig.at(1,1).at(0,3), // BBB
				orig.at(1,1).at(1,0), // AAA
				orig.at(1,1).at(1,1), // BBB
				orig.at(1,1).at(1,2), // AAA
				orig.at(1,1).at(1,3), // BBB
				orig.at(1,1).at(2,3), // AAA
				orig.at(1,1).at(2,2), // BBB
				orig.at(1,1).at(2,1), // AAA
				orig.at(1,1).at(2,0), // BBB
				orig.at(1,1).at(3,3), // AAA
				orig.at(1,1).at(3,2), // BBB
				orig.at(1,1).at(3,1), // AAA
				orig.at(1,1).at(3,0), // BBB - draw

				orig.at(3,3).at(3,3), // AAA - isolated play
				orig.at(0,2).at(1,1), // BBB - just to flip token

				orig.at(1,0).at(0,0), // AAA
				orig.at(1,0).at(1,0), // BBB
				orig.at(1,0).at(0,1), // AAA
				orig.at(1,0).at(1,1), // BBB
				orig.at(1,0).at(0,2), // AAA
				orig.at(1,0).at(1,2), // BBB
				orig.at(1,0).at(0,3), // AAA - wins mid

				orig.at(0,2).at(2,2), // BBB - just to flip token

				orig.at(2,0).at(0,0), // AAA
				orig.at(2,0).at(1,0), // BBB
				orig.at(2,0).at(0,1), // AAA
				orig.at(2,0).at(1,1), // BBB
				orig.at(2,0).at(0,2), // AAA
				orig.at(2,0).at(1,2), // BBB
				orig.at(2,0).at(0,3), // AAA - wins mid; wins top

				orig.at(0,2).at(3,3), // BBB - just to flip token (also wins mid for BBB)

				orig.at(3,0).at(0,0), // AAA
				orig.at(3,0).at(1,0), // BBB
				orig.at(3,0).at(0,1), // AAA
				orig.at(3,0).at(1,1), // BBB
				orig.at(3,0).at(0,2), // AAA
				orig.at(3,0).at(1,2), // BBB
				orig.at(3,0).at(0,3), // AAA - wins mid; wins top
		};

		Token token = Token.PLAYER_AAA;
		for (Position play : playPositions) {

			Board btmBoard = play.derefBoard();

			BoardTestUtil.forceColWin(btmBoard, 0, token);

			final Board copy = orig.copyDeep();
			assertEquals("play=[" + play + "].place(" + token + "): ", orig, copy);

			token = BoardTestUtil.flip(token);
		}
		assertEquals("final status: ", Status.WINNER_AAA, orig.getStatus());
	}

	// ====================================================================================================
	// exaustive win condition tests across [h1s2 .. h3s5]

	private static void takeAtRowCol(Board board, Token token, int row, int col) {
		if (board.getHeight() == 1) {
			board.at(row, col).place(token);
		} else {
			for (int diag : Foreachable.until(board.getSize())) {
				Board subBoard = board.getSubBoard(row, col);
				takeAtRowCol(subBoard, token, diag, diag);
			}
		}
	}

	private static void exerciseWinConditions(int height, int size) {
		final String hsPfx = "h" + height + "s" + size + ": ";

		// confirm win on each row

		for (int row : Foreachable.until(size)) {
			final Token  token = Token.PLAYER_AAA;
			final String pfx   = hsPfx + "on row [" + row + "]: ";
			final Board  board = new Board(height, size);

			for (int col : Foreachable.until(size)) {
				assertEquals(pfx + "board.getStatus(): ", Token.EMPTY.getStatus(), board.getStatus());
				takeAtRowCol(board, token, row, col);
			}

			assertEquals(pfx + "board.getStatus(): ", token.getStatus(), board.getStatus());
		}

		// confirm win on each col

		for (int col : Foreachable.until(size)) {
			final Token  token = Token.PLAYER_BBB;
			final String pfx   = hsPfx + "on col [" + col + "]: ";
			final Board  board = new Board(height, size);

			for (int row : Foreachable.until(size)) {
				assertEquals(pfx + "board.getStatus(): ", Token.EMPTY.getStatus(), board.getStatus());
				takeAtRowCol(board, token, row, col);
			}

			assertEquals(pfx + "board.getStatus(): ", token.getStatus(), board.getStatus());
		}

		// confirm win on each diagonal

		{ // statement-block to avoid local name conflicts
			final Token  token = Token.PLAYER_AAA;
			final String pfx   = hsPfx + "on (0,0)..(s,s) diagonal: ";
			final Board  board = new Board(height, size);

			for (int diag : Foreachable.until(size)) {
				assertEquals(pfx + "board.getStatus(): ", Token.EMPTY.getStatus(), board.getStatus());
				takeAtRowCol(board, token, diag, diag);
			}
			assertEquals(pfx + "board.getStatus(): ", token.getStatus(), board.getStatus());
		}

		{ // statement-block to avoid local name conflicts
			final Token  token = Token.PLAYER_BBB;
			final String pfx   = hsPfx + "on (0,s)..(s,0) diagonal: ";
			final Board  board = new Board(height, size);

			for (int diag : Foreachable.until(size)) {
				assertEquals(pfx + "board.getStatus(): ", Token.EMPTY.getStatus(), board.getStatus());
				takeAtRowCol(board, token, diag, (size - 1 - diag));
			}
			assertEquals(pfx + "board.getStatus(): ", token.getStatus(), board.getStatus());
		}
	}

	@Test
	public void exerciseWinConditions() {
		for (int h  : Foreachable.to(1, 3)) {
			for (int  s : Foreachable.to(2,5)) {
				exerciseWinConditions(h, s);
			}
		}
	}

	// ====================================================================================================

	@Test
	public void fieldAsPrintableString_h1s3_winLevel1() {
		final String expected = ("\n" //
				+ "OOOOOOO\n" //
				+ "Oo|.|.O\n" //
				+ "O-----O\n" //
				+ "Oo|x|xO\n" //
				+ "O-----O\n" //
				+ "Oo|.|xO\n" //
				+ "OOOOOOO\n" //
				+ "TOP.\n").replaceAll("[ABC]", " ") //
				;

		Board board = new Board(1, 3);

		board.at(1, 2).place(Token.PLAYER_AAA);
		board.at(2, 0).place(Token.PLAYER_BBB);
		board.at(1, 1).place(Token.PLAYER_AAA);
		board.at(1, 0).place(Token.PLAYER_BBB);
		board.at(2, 2).place(Token.PLAYER_AAA);
		board.at(0, 0).place(Token.PLAYER_BBB);

		assertEquals("played 1d-s3: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void fieldAsPrintableString_h2s3_winLevel1() {
		final String expected = ("\n" //
			+ "BBBBBBBBBBBBBBBBBBBBBBBBBBB\n" //
			+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
			+ "BA.|.|.A||A.|.|.A||A.|.|.AB\n" //
			+ "BA-----A||A-----A||A-----AB\n" //
			+ "BA.|.|.A||A.|.|.A||A.|.|.AB\n" //
			+ "BA-----A||A-----A||A-----AB\n" //
			+ "BA.|.|.A||A.|.|.A||A.|.|.AB\n" //
			+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
			+ "B-------------------------B\n" //
			+ "B-------------------------B\n" //
			+ "BAAAAAAA||AAAAAAA||OOOOOOOB\n" //
			+ "BA.|.|.A||A.|.|.A||Oo|.|.OB\n" //
			+ "BA-----A||A-----A||O-----OB\n" //
			+ "BA.|.|.A||A.|.|.A||Oo|x|xOB\n" //
			+ "BA-----A||A-----A||O-----OB\n" //
			+ "BA.|.|.A||A.|.|.A||Oo|.|xOB\n" //
			+ "BAAAAAAA||AAAAAAA||OOOOOOOB\n" //
			+ "B-------------------------B\n" //
			+ "B-------------------------B\n" //
			+ "BXXXXXXX||AAAAAAA||AAAAAAAB\n" //
			+ "BXx|.|.X||A.|.|.A||A.|.|.AB\n" //
			+ "BX-----X||A-----A||A-----AB\n" //
			+ "BXx|o|oX||A.|.|.A||A.|.|.AB\n" //
			+ "BX-----X||A-----A||A-----AB\n" //
			+ "BXx|.|oX||A.|.|.A||A.|.|.AB\n" //
			+ "BXXXXXXX||AAAAAAA||AAAAAAAB\n" //
			+ "BBBBBBBBBBBBBBBBBBBBBBBBBBB\n" //
			+ "TOP.\n").replaceAll("[ABC]", " ") //
			;

		Board board = new Board(2, 3);

		board.getSubBoard(1, 2).at(1, 2).place(Token.PLAYER_AAA);
		board.getSubBoard(1, 2).at(2, 0).place(Token.PLAYER_BBB);
		board.getSubBoard(1, 2).at(1, 1).place(Token.PLAYER_AAA);
		board.getSubBoard(1, 2).at(1, 0).place(Token.PLAYER_BBB);
		board.getSubBoard(1, 2).at(2, 2).place(Token.PLAYER_AAA);
		board.getSubBoard(1, 2).at(0, 0).place(Token.PLAYER_BBB);

		board.getSubBoard(2, 0).at(1, 2).place(Token.PLAYER_BBB);
		board.getSubBoard(2, 0).at(2, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(2, 0).at(1, 1).place(Token.PLAYER_BBB);
		board.getSubBoard(2, 0).at(1, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(2, 0).at(2, 2).place(Token.PLAYER_BBB);
		board.getSubBoard(2, 0).at(0, 0).place(Token.PLAYER_AAA);

		assertEquals("played 2d-s3: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void fieldAsPrintableString_h2s3_winLevel2() {
		final String expected = ("\n" //
			+ "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
			+ "XAAAAAAA||AAAAAAA||XXXXXXXX\n" //
			+ "XA.|.|.A||A.|.|.A||Xx|.|.XX\n" //
			+ "XA-----A||A-----A||X-----XX\n" //
			+ "XA.|.|.A||A.|.|.A||Xx|o|oXX\n" //
			+ "XA-----A||A-----A||X-----XX\n" //
			+ "XA.|.|.A||A.|.|.A||Xx|.|oXX\n" //
			+ "XAAAAAAA||AAAAAAA||XXXXXXXX\n" //
			+ "X-------------------------X\n" //
			+ "X-------------------------X\n" //
			+ "XOOOOOOO||XXXXXXX||OOOOOOOX\n" //
			+ "XOo|.|.O||Xx|.|.X||Oo|.|.OX\n" //
			+ "XO-----O||X-----X||O-----OX\n" //
			+ "XOo|x|xO||Xx|o|oX||Oo|x|xOX\n" //
			+ "XO-----O||X-----X||O-----OX\n" //
			+ "XOo|.|xO||Xx|.|oX||Oo|.|xOX\n" //
			+ "XOOOOOOO||XXXXXXX||OOOOOOOX\n" //
			+ "X-------------------------X\n" //
			+ "X-------------------------X\n" //
			+ "XXXXXXXX||AAAAAAA||AAAAAAAX\n" //
			+ "XXx|.|.X||A.|.|.A||A.|.|.AX\n" //
			+ "XX-----X||A-----A||A-----AX\n" //
			+ "XXx|o|oX||A.|.|.A||A.|.|.AX\n" //
			+ "XX-----X||A-----A||A-----AX\n" //
			+ "XXx|.|oX||A.|.|.A||A.|.|.AX\n" //
			+ "XXXXXXXX||AAAAAAA||AAAAAAAX\n" //
			+ "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
			+ "TOP.\n").replaceAll("[ABC]", " ") //
			;

		Board board = new Board(2, 3);

		board.getSubBoard(0,2).at(1, 2).place(Token.PLAYER_BBB);
		board.getSubBoard(0,2).at(2, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(0,2).at(1, 1).place(Token.PLAYER_BBB);
		board.getSubBoard(0,2).at(1, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(0,2).at(2, 2).place(Token.PLAYER_BBB);
		board.getSubBoard(0,2).at(0, 0).place(Token.PLAYER_AAA);

		board.getSubBoard(1,2).at(1, 2).place(Token.PLAYER_AAA);
		board.getSubBoard(1,2).at(2, 0).place(Token.PLAYER_BBB);
		board.getSubBoard(1,2).at(1, 1).place(Token.PLAYER_AAA);
		board.getSubBoard(1,2).at(1, 0).place(Token.PLAYER_BBB);
		board.getSubBoard(1,2).at(2, 2).place(Token.PLAYER_AAA);
		board.getSubBoard(1,2).at(0, 0).place(Token.PLAYER_BBB);

		board.getSubBoard(2,0).at(1, 2).place(Token.PLAYER_BBB);
		board.getSubBoard(2,0).at(2, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(2,0).at(1, 1).place(Token.PLAYER_BBB);
		board.getSubBoard(2,0).at(1, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(2,0).at(2, 2).place(Token.PLAYER_BBB);
		board.getSubBoard(2,0).at(0, 0).place(Token.PLAYER_AAA);

		board.getSubBoard(1,0).at(1, 2).place(Token.PLAYER_AAA);
		board.getSubBoard(1,0).at(2, 0).place(Token.PLAYER_BBB);
		board.getSubBoard(1,0).at(1, 1).place(Token.PLAYER_AAA);
		board.getSubBoard(1,0).at(1, 0).place(Token.PLAYER_BBB);
		board.getSubBoard(1,0).at(2, 2).place(Token.PLAYER_AAA);
		board.getSubBoard(1,0).at(0, 0).place(Token.PLAYER_BBB);

		board.getSubBoard(1,1).at(1, 2).place(Token.PLAYER_BBB);
		board.getSubBoard(1,1).at(2, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(1,1).at(1, 1).place(Token.PLAYER_BBB);
		board.getSubBoard(1,1).at(1, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(1,1).at(2, 2).place(Token.PLAYER_BBB);
		board.getSubBoard(1,1).at(0, 0).place(Token.PLAYER_AAA);

		assertEquals("played 2d-s3: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void fieldAsPrintableString_h3s2_winLevel2() {
		final String expected = ("\n" //
				+ "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC\n" //
				+ "CXXXXXXXXXXXXXX|||BBBBBBBBBBBBBBC\n" //
				+ "CXXXXXX||XXXXXX|||BAAAAA||AAAAABC\n" //
				+ "CXXo|xX||Xo|xXX|||BA.|.A||A.|.ABC\n" //
				+ "CXX---X||X---XX|||BA---A||A---ABC\n" //
				+ "CXX.|xX||X.|xXX|||BA.|.A||A.|.ABC\n" //
				+ "CXXXXXX||XXXXXX|||BAAAAA||AAAAABC\n" //
				+ "CX------------X|||B------------BC\n" //
				+ "CX------------X|||B------------BC\n" //
				+ "CXAAAAA||OOOOOX|||BAAAAA||AAAAABC\n" //
				+ "CXA.|.A||Ox|oOX|||BA.|.A||A.|.ABC\n" //
				+ "CXA---A||O---OX|||BA---A||A---ABC\n" //
				+ "CXA.|.A||O.|oOX|||BA.|.A||A.|.ABC\n" //
				+ "CXAAAAA||OOOOOX|||BAAAAA||AAAAABC\n" //
				+ "CXXXXXXXXXXXXXX|||BBBBBBBBBBBBBBC\n" //
				+ "C-------------------------------C\n" //
				+ "C-------------------------------C\n" //
				+ "C-------------------------------C\n" //
				+ "CBBBBBBBBBBBBBB|||OOOOOOOOOOOOOOC\n" //
				+ "CBAAAAA||AAAAAB|||OOOOOO||OOOOOOC\n" //
				+ "CBA.|.A||A.|.AB|||OOx|oO||Ox|oOOC\n" //
				+ "CBA---A||A---AB|||OO---O||O---OOC\n" //
				+ "CBA.|.A||A.|.AB|||OO.|oO||O.|oOOC\n" //
				+ "CBAAAAA||AAAAAB|||OOOOOO||OOOOOOC\n" //
				+ "CB------------B|||O------------OC\n" //
				+ "CB------------B|||O------------OC\n" //
				+ "CBAAAAA||AAAAAB|||OAAAAA||XXXXXOC\n" //
				+ "CBA.|.A||A.|.AB|||OA.|.A||Xo|xXOC\n" //
				+ "CBA---A||A---AB|||OA---A||X---XOC\n" //
				+ "CBA.|.A||A.|.AB|||OA.|.A||X.|xXOC\n" //
				+ "CBAAAAA||AAAAAB|||OAAAAA||XXXXXOC\n" //
				+ "CBBBBBBBBBBBBBB|||OOOOOOOOOOOOOOC\n" //
				+ "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC\n" //
				+ "TOP.\n").replaceAll("[ABC]", " ") //
				;

		Board board = new Board(3, 2);

		board.getSubBoard(0,0).getSubBoard(0,0).at(0, 1).place(Token.PLAYER_AAA);
		board.getSubBoard(0,0).getSubBoard(0,0).at(0, 0).place(Token.PLAYER_BBB);
		board.getSubBoard(0,0).getSubBoard(0,0).at(1, 1).place(Token.PLAYER_AAA);

		board.getSubBoard(0,0).getSubBoard(1,1).at(0, 1).place(Token.PLAYER_BBB);
		board.getSubBoard(0,0).getSubBoard(1,1).at(0, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(0,0).getSubBoard(1,1).at(1, 1).place(Token.PLAYER_BBB);

		board.getSubBoard(0,0).getSubBoard(0,1).at(0, 1).place(Token.PLAYER_AAA);
		board.getSubBoard(0,0).getSubBoard(0,1).at(0, 0).place(Token.PLAYER_BBB);
		board.getSubBoard(0,0).getSubBoard(0,1).at(1, 1).place(Token.PLAYER_AAA);

		// - - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - - - - - - - - - - - - - - - -

		board.getSubBoard(1,1).getSubBoard(0,0).at(0, 1).place(Token.PLAYER_BBB);
		board.getSubBoard(1,1).getSubBoard(0,0).at(0, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(1,1).getSubBoard(0,0).at(1, 1).place(Token.PLAYER_BBB);

		board.getSubBoard(1,1).getSubBoard(1,1).at(0, 1).place(Token.PLAYER_AAA);
		board.getSubBoard(1,1).getSubBoard(1,1).at(0, 0).place(Token.PLAYER_BBB);
		board.getSubBoard(1,1).getSubBoard(1,1).at(1, 1).place(Token.PLAYER_AAA);

		board.getSubBoard(1,1).getSubBoard(0,1).at(0, 1).place(Token.PLAYER_BBB);
		board.getSubBoard(1,1).getSubBoard(0,1).at(0, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(1,1).getSubBoard(0,1).at(1, 1).place(Token.PLAYER_BBB);

		assertEquals("played h3-s2, winLevel2: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void fieldAsPrintableString_h3s2_winLevel3() {
		final String expected = ("\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "XXXXXXXXXXXXXXX|||BBBBBBBBBBBBBBX\n" //
				+ "XXXXXXX||XXXXXX|||BAAAAA||AAAAABX\n" //
				+ "XXXo|xX||Xo|xXX|||BA.|.A||A.|.ABX\n" //
				+ "XXX---X||X---XX|||BA---A||A---ABX\n" //
				+ "XXX.|xX||X.|xXX|||BA.|.A||A.|.ABX\n" //
				+ "XXXXXXX||XXXXXX|||BAAAAA||AAAAABX\n" //
				+ "XX------------X|||B------------BX\n" //
				+ "XX------------X|||B------------BX\n" //
				+ "XXAAAAA||OOOOOX|||BAAAAA||AAAAABX\n" //
				+ "XXA.|.A||Ox|oOX|||BA.|.A||A.|.ABX\n" //
				+ "XXA---A||O---OX|||BA---A||A---ABX\n" //
				+ "XXA.|.A||O.|oOX|||BA.|.A||A.|.ABX\n" //
				+ "XXAAAAA||OOOOOX|||BAAAAA||AAAAABX\n" //
				+ "XXXXXXXXXXXXXXX|||BBBBBBBBBBBBBBX\n" //
				+ "X-------------------------------X\n" //
				+ "X-------------------------------X\n" //
				+ "X-------------------------------X\n" //
				+ "XXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOX\n" //
				+ "XXXXXXX||XXXXXX|||OOOOOO||OOOOOOX\n" //
				+ "XXXo|xX||Xo|xXX|||OOx|oO||Ox|oOOX\n" //
				+ "XXX---X||X---XX|||OO---O||O---OOX\n" //
				+ "XXX.|xX||X.|xXX|||OO.|oO||O.|oOOX\n" //
				+ "XXXXXXX||XXXXXX|||OOOOOO||OOOOOOX\n" //
				+ "XX------------X|||O------------OX\n" //
				+ "XX------------X|||O------------OX\n" //
				+ "XXAAAAA||OOOOOX|||OAAAAA||XXXXXOX\n" //
				+ "XXA.|.A||Ox|oOX|||OA.|.A||Xo|xXOX\n" //
				+ "XXA---A||O---OX|||OA---A||X---XOX\n" //
				+ "XXA.|.A||O.|oOX|||OA.|.A||X.|xXOX\n" //
				+ "XXAAAAA||OOOOOX|||OAAAAA||XXXXXOX\n" //
				+ "XXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOX\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "TOP.\n").replaceAll("[ABC]", " ") //
				;

		Board board = new Board(3, 2);

		board.getSubBoard(0,0).getSubBoard(0,0).at(0, 1).place(Token.PLAYER_AAA);
		board.getSubBoard(0,0).getSubBoard(0,0).at(0, 0).place(Token.PLAYER_BBB);
		board.getSubBoard(0,0).getSubBoard(0,0).at(1, 1).place(Token.PLAYER_AAA);

		board.getSubBoard(0,0).getSubBoard(1,1).at(0, 1).place(Token.PLAYER_BBB);
		board.getSubBoard(0,0).getSubBoard(1,1).at(0, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(0,0).getSubBoard(1,1).at(1, 1).place(Token.PLAYER_BBB);

		board.getSubBoard(0,0).getSubBoard(0,1).at(0, 1).place(Token.PLAYER_AAA);
		board.getSubBoard(0,0).getSubBoard(0,1).at(0, 0).place(Token.PLAYER_BBB);
		board.getSubBoard(0,0).getSubBoard(0,1).at(1, 1).place(Token.PLAYER_AAA);

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		board.getSubBoard(1,1).getSubBoard(0,0).at(0, 1).place(Token.PLAYER_BBB);
		board.getSubBoard(1,1).getSubBoard(0,0).at(0, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(1,1).getSubBoard(0,0).at(1, 1).place(Token.PLAYER_BBB);

		board.getSubBoard(1,1).getSubBoard(1,1).at(0, 1).place(Token.PLAYER_AAA);
		board.getSubBoard(1,1).getSubBoard(1,1).at(0, 0).place(Token.PLAYER_BBB);
		board.getSubBoard(1,1).getSubBoard(1,1).at(1, 1).place(Token.PLAYER_AAA);

		board.getSubBoard(1,1).getSubBoard(0,1).at(0, 1).place(Token.PLAYER_BBB);
		board.getSubBoard(1,1).getSubBoard(0,1).at(0, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(1,1).getSubBoard(0,1).at(1, 1).place(Token.PLAYER_BBB);

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		board.getSubBoard(1,0).getSubBoard(0,0).at(0, 1).place(Token.PLAYER_AAA);
		board.getSubBoard(1,0).getSubBoard(0,0).at(0, 0).place(Token.PLAYER_BBB);
		board.getSubBoard(1,0).getSubBoard(0,0).at(1, 1).place(Token.PLAYER_AAA);

		board.getSubBoard(1,0).getSubBoard(1,1).at(0, 1).place(Token.PLAYER_BBB);
		board.getSubBoard(1,0).getSubBoard(1,1).at(0, 0).place(Token.PLAYER_AAA);
		board.getSubBoard(1,0).getSubBoard(1,1).at(1, 1).place(Token.PLAYER_BBB);

		board.getSubBoard(1,0).getSubBoard(0,1).at(0, 1).place(Token.PLAYER_AAA);
		board.getSubBoard(1,0).getSubBoard(0,1).at(0, 0).place(Token.PLAYER_BBB);
		board.getSubBoard(1,0).getSubBoard(0,1).at(1, 1).place(Token.PLAYER_AAA);

		assertEquals("played h3-s2, winLevel3: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void printableField_h1s3_empty() {
		final String expected = ("\n" //
				+ "AAAAAAA\n" //
				+ "A.|.|.A\n" //
				+ "A-----A\n" //
				+ "A.|.|.A\n" //
				+ "A-----A\n" //
				+ "A.|.|.A\n" //
				+ "AAAAAAA\n" //
				+ "TOP.\n").replaceAll("[ABC]", " ") //
				;

		Board board = new Board(1, 3);

		assertEquals("empty 1d-s3: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void printableField_h1s4_empty() {
		final String expected = ("\n" //
				+ "AAAAAAAAA\n" //
				+ "A.|.|.|.A\n" //
				+ "A-------A\n" //
				+ "A.|.|.|.A\n" //
				+ "A-------A\n" //
				+ "A.|.|.|.A\n" //
				+ "A-------A\n" //
				+ "A.|.|.|.A\n" //
				+ "AAAAAAAAA\n" //
				+ "TOP.\n").replaceAll("[ABC]", " ") //
				;

		Board board = new Board(1, 4);

		assertEquals("empty 1d-4s: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void printableField_h2s3_empty() {
		final String expected = ("\n" //
				+ "BBBBBBBBBBBBBBBBBBBBBBBBBBB\n" //
				+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
				+ "BA.|.|.A||A.|.|.A||A.|.|.AB\n" //
				+ "BA-----A||A-----A||A-----AB\n" //
				+ "BA.|.|.A||A.|.|.A||A.|.|.AB\n" //
				+ "BA-----A||A-----A||A-----AB\n" //
				+ "BA.|.|.A||A.|.|.A||A.|.|.AB\n" //
				+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
				+ "B-------------------------B\n" //
				+ "B-------------------------B\n" //
				+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
				+ "BA.|.|.A||A.|.|.A||A.|.|.AB\n" //
				+ "BA-----A||A-----A||A-----AB\n" //
				+ "BA.|.|.A||A.|.|.A||A.|.|.AB\n" //
				+ "BA-----A||A-----A||A-----AB\n" //
				+ "BA.|.|.A||A.|.|.A||A.|.|.AB\n" //
				+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
				+ "B-------------------------B\n" //
				+ "B-------------------------B\n" //
				+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
				+ "BA.|.|.A||A.|.|.A||A.|.|.AB\n" //
				+ "BA-----A||A-----A||A-----AB\n" //
				+ "BA.|.|.A||A.|.|.A||A.|.|.AB\n" //
				+ "BA-----A||A-----A||A-----AB\n" //
				+ "BA.|.|.A||A.|.|.A||A.|.|.AB\n" //
				+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
				+ "BBBBBBBBBBBBBBBBBBBBBBBBBBB\n" //
				+ "TOP.\n").replaceAll("[ABC]", " ") //
				;

		Board board = new Board(2, 3);

		assertEquals("empty 2d-s3: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void printableField_h2s4_empty() {
		final String expected = ("\n" //
				+ "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB\n" //
				+ "BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BA-------A||A-------A||A-------A||A-------AB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BA-------A||A-------A||A-------A||A-------AB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BA-------A||A-------A||A-------A||A-------AB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB\n" //
				+ "B------------------------------------------B\n" //
				+ "B------------------------------------------B\n" //
				+ "BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BA-------A||A-------A||A-------A||A-------AB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BA-------A||A-------A||A-------A||A-------AB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BA-------A||A-------A||A-------A||A-------AB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB\n" //
				+ "B------------------------------------------B\n" //
				+ "B------------------------------------------B\n" //
				+ "BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BA-------A||A-------A||A-------A||A-------AB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BA-------A||A-------A||A-------A||A-------AB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BA-------A||A-------A||A-------A||A-------AB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB\n" //
				+ "B------------------------------------------B\n" //
				+ "B------------------------------------------B\n" //
				+ "BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BA-------A||A-------A||A-------A||A-------AB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BA-------A||A-------A||A-------A||A-------AB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BA-------A||A-------A||A-------A||A-------AB\n" //
				+ "BA.|.|.|.A||A.|.|.|.A||A.|.|.|.A||A.|.|.|.AB\n" //
				+ "BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB\n" //
				+ "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB\n" //
				+ "TOP.\n").replaceAll("[ABC]", " ") //
				;

		Board board = new Board(2, 4);

		assertEquals("empty 2d-s4: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void printableField_h3s2_empty() {
		final String expected = ("\n" //
				+ "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC\n" //
				+ "CBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBC\n" //
				+ "CBAAAAA||AAAAAB|||BAAAAA||AAAAABC\n" //
				+ "CBA.|.A||A.|.AB|||BA.|.A||A.|.ABC\n" //
				+ "CBA---A||A---AB|||BA---A||A---ABC\n" //
				+ "CBA.|.A||A.|.AB|||BA.|.A||A.|.ABC\n" //
				+ "CBAAAAA||AAAAAB|||BAAAAA||AAAAABC\n" //
				+ "CB------------B|||B------------BC\n" //
				+ "CB------------B|||B------------BC\n" //
				+ "CBAAAAA||AAAAAB|||BAAAAA||AAAAABC\n" //
				+ "CBA.|.A||A.|.AB|||BA.|.A||A.|.ABC\n" //
				+ "CBA---A||A---AB|||BA---A||A---ABC\n" //
				+ "CBA.|.A||A.|.AB|||BA.|.A||A.|.ABC\n" //
				+ "CBAAAAA||AAAAAB|||BAAAAA||AAAAABC\n" //
				+ "CBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBC\n" //
				+ "C-------------------------------C\n" //
				+ "C-------------------------------C\n" //
				+ "C-------------------------------C\n" //
				+ "CBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBC\n" //
				+ "CBAAAAA||AAAAAB|||BAAAAA||AAAAABC\n" //
				+ "CBA.|.A||A.|.AB|||BA.|.A||A.|.ABC\n" //
				+ "CBA---A||A---AB|||BA---A||A---ABC\n" //
				+ "CBA.|.A||A.|.AB|||BA.|.A||A.|.ABC\n" //
				+ "CBAAAAA||AAAAAB|||BAAAAA||AAAAABC\n" //
				+ "CB------------B|||B------------BC\n" //
				+ "CB------------B|||B------------BC\n" //
				+ "CBAAAAA||AAAAAB|||BAAAAA||AAAAABC\n" //
				+ "CBA.|.A||A.|.AB|||BA.|.A||A.|.ABC\n" //
				+ "CBA---A||A---AB|||BA---A||A---ABC\n" //
				+ "CBA.|.A||A.|.AB|||BA.|.A||A.|.ABC\n" //
				+ "CBAAAAA||AAAAAB|||BAAAAA||AAAAABC\n" //
				+ "CBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBC\n" //
				+ "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC\n" //
				+ "TOP.\n").replaceAll("[ABC]", " ") //
				;

		Board board = new Board(3, 2);

		assertEquals("empty d3-s2: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void printableField_h3s3_empty() {
		final String expected = ("\n" //
				+ "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC\n" //
				+ "CBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CB-------------------------B|||B-------------------------B|||B-------------------------BC\n" //
				+ "CB-------------------------B|||B-------------------------B|||B-------------------------BC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CB-------------------------B|||B-------------------------B|||B-------------------------BC\n" //
				+ "CB-------------------------B|||B-------------------------B|||B-------------------------BC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBC\n" //
				+ "C---------------------------------------------------------------------------------------C\n" //
				+ "C---------------------------------------------------------------------------------------C\n" //
				+ "C---------------------------------------------------------------------------------------C\n" //
				+ "CBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CB-------------------------B|||B-------------------------B|||B-------------------------BC\n" //
				+ "CB-------------------------B|||B-------------------------B|||B-------------------------BC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CB-------------------------B|||B-------------------------B|||B-------------------------BC\n" //
				+ "CB-------------------------B|||B-------------------------B|||B-------------------------BC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBC\n" //
				+ "C---------------------------------------------------------------------------------------C\n" //
				+ "C---------------------------------------------------------------------------------------C\n" //
				+ "C---------------------------------------------------------------------------------------C\n" //
				+ "CBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CB-------------------------B|||B-------------------------B|||B-------------------------BC\n" //
				+ "CB-------------------------B|||B-------------------------B|||B-------------------------BC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CB-------------------------B|||B-------------------------B|||B-------------------------BC\n" //
				+ "CB-------------------------B|||B-------------------------B|||B-------------------------BC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----AB|||BA-----A||A-----A||A-----ABC\n" //
				+ "CBA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.AB|||BA.|.|.A||A.|.|.A||A.|.|.ABC\n" //
				+ "CBAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAAB|||BAAAAAAA||AAAAAAA||AAAAAAABC\n" //
				+ "CBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBC\n" //
				+ "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC\n" //
				+ "TOP.\n").replaceAll("[ABC]", " ") //
				;

		Board board = new Board(3, 3);

		assertEquals("empty d3-s3: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void printableField_heightTooLarge() {

		Board board = new Board(4, 3);

		board.fieldAsPrintableString();
	}
}
