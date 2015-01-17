package com.uttt.common.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class BoardTest extends NodeTest {

	private static final int STANDARD_HEIGHT = 2;
	private static final int STANDARD_SIZE   = 3;

	static private final int[] rHeight = new int[STANDARD_HEIGHT];
	static private final int[] rSize   = new int[STANDARD_SIZE  ];
	static {
		for (int i = 1; i < rSize  .length; ++i) rSize  [i] = i;
		for (int i = 1; i < rHeight.length; ++i) rHeight[i] = i;
	}

	@SuppressWarnings("unused")
	private static Token flip(Token t) {
		return (t == Token.PLAYER_AAA ? Token.PLAYER_BBB: Token.PLAYER_AAA);
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	@Override
	@Test
	public void accessors_getSubNode() {
		Board board = new Board(1, STANDARD_SIZE);

		Node[][] field = board.getField();

		for (int x : rSize) {
			for (int y : rSize) {
				assertSame("board.getHeight(): ", field[x][y], board.getSubNode(x,y));
			}
		}
	}

	@Override
	@Test
	public void accessors_getHeight() {
		Board board = new Board(1, STANDARD_SIZE);

		assertEquals("board.getHeight(): ", 1, board.getHeight());
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	@Test
	public void newBoard_standard() {
		Board board = new Board(2, STANDARD_SIZE);

		assertEquals("board.getHeight(): ", STANDARD_HEIGHT, board.getHeight());
		assertEquals("board.getSize(): "  , STANDARD_SIZE  , board.getSize());

		Board topNode = board.getSubNode(1, 2, Board.class);
		assertEquals("topNode.getClass(): ", Board.class, topNode.getClass());

		Token bottomNode = topNode.getSubNode(1, 2, Token.class);
		assertEquals("bottomNode.getClass(): ", Token.class, bottomNode.getClass());
	}

	@Test(expected=IllegalArgumentException.class)
	public void newBoard_tooShallow() {
		@SuppressWarnings("unused")
		Board board = new Board(0, 999);
	}

	@Test(expected=IllegalArgumentException.class)
	public void newBoard_tooTall() {
		@SuppressWarnings("unused")
		Board board = new Board(10, 999);
	}

	@Test(expected=IllegalArgumentException.class)
	public void newBoard_tooNarrow() {
		@SuppressWarnings("unused")
		Board board = new Board(1, 1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void newBoard_tooWide() {
		@SuppressWarnings("unused")
		Board board = new Board(1, 999);
	}

	@Test
	public void accessors_getSize() {
		Board board = new Board(1, STANDARD_SIZE);

		assertEquals("board.getSize(): "  , STANDARD_SIZE, board.getSize());
	}

	@Test
	public void accessors_getField() {
		Board board = new Board(1, STANDARD_SIZE);

		Node[][] field = board.getField();

		assertEquals("field.length: ", STANDARD_SIZE, field.length);
		for (int row :rSize) {
			assertEquals("field["+row+"].length: ", STANDARD_SIZE, field[row].length);
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void placeToken_h1s3_alreadyFilled() {
		Board board = new Board(1, STANDARD_SIZE);

		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 2));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 2));
	}

	private static void takeAtRowCol(Board board, Token token, int row, int col) {
		if (board.getHeight() == 1) {
			Coordinates coord = new Coordinates(row, col);
			board.placeToken(token, coord);
		} else {
			for (int diag = 0; diag < board.getSize(); ++diag) {
				takeAtRowCol(board.getSubNode(row, col, Board.class), token, diag, diag);
			}
		}
	}

	private static void exerciseWinConditions(int height, int size) {
		final String hsPfx = "h" + height + "s" + size + ": ";

		// confirm win on each row

		for (int row = 0; row < size; ++row) {
			final Token  token = Token.PLAYER_AAA;
			final String pfx   = hsPfx + "on row [" + row + "]: ";
			final Board  board = new Board(height, size);

			for (int col = 0; col < size; ++col) {
				assertEquals(pfx + "board.getStatus(): ", Token.EMPTY.getStatus(), board.getStatus());
				takeAtRowCol(board, token, row, col);
			}

			assertEquals(pfx + "board.getStatus(): ", token.getStatus(), board.getStatus());
		}

		// confirm win on each col

		for (int col = 0; col < size; ++col) {
			final Token  token = Token.PLAYER_BBB;
			final String pfx   = hsPfx + "on col [" + col + "]: ";
			final Board  board = new Board(height, size);

			for (int row = 0; row < size; ++row) {
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

			for (int diag = 0; diag < size; ++diag) {
				assertEquals(pfx + "board.getStatus(): ", Token.EMPTY.getStatus(), board.getStatus());
				takeAtRowCol(board, token, diag, diag);
			}
			assertEquals(pfx + "board.getStatus(): ", token.getStatus(), board.getStatus());
		}

		{ // statement-block to avoid local name conflicts
			final Token  token = Token.PLAYER_BBB;
			final String pfx   = hsPfx + "on (0,s)..(s,0) diagonal: ";
			final Board  board = new Board(height, size);

			for (int diag = 0; diag < size; ++diag) {
				assertEquals(pfx + "board.getStatus(): ", Token.EMPTY.getStatus(), board.getStatus());
				takeAtRowCol(board, token, diag, (size - 1 - diag));
			}
			assertEquals(pfx + "board.getStatus(): ", token.getStatus(), board.getStatus());
		}
	}

	@Test
	public void exerciseWinConditions() {
		exerciseWinConditions(1,2);
		exerciseWinConditions(1,3);
		exerciseWinConditions(1,4);
	}

	@Test
	public void placeToken_h1s3() {
		Board board = new Board(1, STANDARD_SIZE);

		Coordinates restriction = board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 2));

		assertNull("restriction", restriction);
		assertEquals("board.getSubNode(1, 2, Token.class): ", Token.PLAYER_AAA, board.getSubNode(1, 2, Token.class));
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

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
				).replaceAll("[ABC]", " ") //
				;

		Board board = new Board(1, STANDARD_SIZE);

		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 2));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(2, 0));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 1));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(1, 0));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(2, 2));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 0));

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
			).replaceAll("[ABC]", " ") //
			;

		Board board = new Board(STANDARD_HEIGHT, STANDARD_SIZE);

		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 2).within(1,2));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(2, 0).within(1,2));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 1).within(1,2));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(1, 0).within(1,2));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(2, 2).within(1,2));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 0).within(1,2));

		board.placeToken(Token.PLAYER_BBB, new Coordinates(1, 2).within(2, 0));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(2, 0).within(2, 0));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(1, 1).within(2, 0));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 0).within(2, 0));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(2, 2).within(2, 0));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 0).within(2, 0));

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
				).replaceAll("[ABC]", " ") //
				;

		Board board = new Board(3, 2);

		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 1).within(0,0).within(0,0));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 1).within(0,0).within(0,0));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 0).within(0,0).within(0,0));

		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 1).within(1,1).within(0,0));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(1, 1).within(1,1).within(0,0));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 0).within(1,1).within(0,0));

		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 1).within(0,1).within(0,0));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 1).within(0,1).within(0,0));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 0).within(0,1).within(0,0));

		//

		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 1).within(0,0).within(1,1));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(1, 1).within(0,0).within(1,1));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 0).within(0,0).within(1,1));

		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 1).within(1,1).within(1,1));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 1).within(1,1).within(1,1));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 0).within(1,1).within(1,1));

		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 1).within(0,1).within(1,1));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(1, 1).within(0,1).within(1,1));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 0).within(0,1).within(1,1));

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
				).replaceAll("[ABC]", " ") //
				;

		Board board = new Board(3, 2);

		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 1).within(0,0).within(0,0));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 1).within(0,0).within(0,0));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 0).within(0,0).within(0,0));

		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 1).within(1,1).within(0,0));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(1, 1).within(1,1).within(0,0));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 0).within(1,1).within(0,0));

		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 1).within(0,1).within(0,0));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 1).within(0,1).within(0,0));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 0).within(0,1).within(0,0));

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 1).within(0,0).within(1,1));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(1, 1).within(0,0).within(1,1));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 0).within(0,0).within(1,1));

		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 1).within(1,1).within(1,1));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 1).within(1,1).within(1,1));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 0).within(1,1).within(1,1));

		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 1).within(0,1).within(1,1));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(1, 1).within(0,1).within(1,1));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 0).within(0,1).within(1,1));

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 1).within(0,0).within(1,0));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 1).within(0,0).within(1,0));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 0).within(0,0).within(1,0));

		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 1).within(1,1).within(1,0));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(1, 1).within(1,1).within(1,0));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 0).within(1,1).within(1,0));

		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 1).within(0,1).within(1,0));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 1).within(0,1).within(1,0));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 0).within(0,1).within(1,0));

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
				).replaceAll("[ABC]", " ") //
				;

		Board board = new Board(1, STANDARD_SIZE);

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
				).replaceAll("[ABC]", " ") //
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
				).replaceAll("[ABC]", " ") //
				;

		Board board = new Board(STANDARD_HEIGHT, STANDARD_SIZE);

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
				).replaceAll("[ABC]", " ") //
				;

		Board board = new Board(STANDARD_HEIGHT, 4);

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
				).replaceAll("[ABC]", " ") //
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
				).replaceAll("[ABC]", " ") //
				;

		Board board = new Board(3, STANDARD_SIZE);

		assertEquals("empty d3-s3: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void printableField_heightTooLarge() {

		Board board = new Board(4, STANDARD_SIZE);

		board.fieldAsPrintableString();
	}
}
