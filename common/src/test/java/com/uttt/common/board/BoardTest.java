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

	private static Token flip(Token t) {
		return (t == Token.PLAYER_AAA ? Token.PLAYER_BBB: Token.PLAYER_AAA);
	}

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
		new Board(0, 999);
	}

	@Test(expected=IllegalArgumentException.class)
	public void newBoard_tooTall() {
		new Board(10, 999);
	}

	@Test(expected=IllegalArgumentException.class)
	public void newBoard_tooNarrow() {
		new Board(1, 1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void newBoard_tooWide() {
		new Board(1, 999);
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
		for (int x :rSize) {
			assertEquals("field["+x+"].length: ", STANDARD_SIZE, field[x].length);
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void placeToken_h1s3_alreadyFilled() {
		Board board = new Board(1, STANDARD_SIZE);

		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 2));
		board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 2));
	}

	@Test
	public void placeToken_h1s3() {
		Board board = new Board(1, STANDARD_SIZE);

		Coordinates restriction = board.placeToken(Token.PLAYER_AAA, new Coordinates(1, 2));

		assertNull("restriction", restriction);
		assertEquals("board.getSubNode(1, 2, Token.class): ", Token.PLAYER_AAA, board.getSubNode(1, 2, Token.class));
	}

	@Test
	public void fieldAsPrintableString_h1s3() {
		final String expected = ("\n" //
				+ "AAAAAAA\n" //
				+ "Ao|.|.A\n" //
				+ "A-----A\n" //
				+ "Ao|x|xA\n" //
				+ "A-----A\n" //
				+ "Ao|.|xA\n" //
				+ "AAAAAAA\n" //
				).replace('A', ' ') //
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
	public void fieldAsPrintableString_h2s3() {
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
			+ "BA.|.|.A||A.|.|.A||Ao|.|.AB\n" //
			+ "BA-----A||A-----A||A-----AB\n" //
			+ "BA.|.|.A||A.|.|.A||Ao|x|xAB\n" //
			+ "BA-----A||A-----A||A-----AB\n" //
			+ "BA.|.|.A||A.|.|.A||Ao|.|xAB\n" //
			+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
			+ "B-------------------------B\n" //
			+ "B-------------------------B\n" //
			+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
			+ "BAx|.|.A||A.|.|.A||A.|.|.AB\n" //
			+ "BA-----A||A-----A||A-----AB\n" //
			+ "BAx|o|oA||A.|.|.A||A.|.|.AB\n" //
			+ "BA-----A||A-----A||A-----AB\n" //
			+ "BAx|.|oA||A.|.|.A||A.|.|.AB\n" //
			+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
			+ "BBBBBBBBBBBBBBBBBBBBBBBBBBB\n" //
			).replace('A', ' ').replace('B', ' ') //
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
	public void fieldAsPrintableString_h3s2() {
		final String expected = ("\n" //
				+ "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC\n" //
				+ "CBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBC\n" //
				+ "CBAAAAA||AAAAAB|||BAAAAA||AAAAABC\n" //
				+ "CBA.|xA||A.|.AB|||BA.|.A||A.|.ABC\n" //
				+ "CBA---A||A---AB|||BA---A||A---ABC\n" //
				+ "CBA.|.A||A.|.AB|||BA.|.A||A.|.ABC\n" //
				+ "CBAAAAA||AAAAAB|||BAAAAA||AAAAABC\n" //
				+ "CB------------B|||B------------BC\n" //
				+ "CB------------B|||B------------BC\n" //
				+ "CBAAAAA||AAAAAB|||BAAAAA||AAAAABC\n" //
				+ "CBA.|.A||A.|.AB|||BA.|.A||Ao|.ABC\n" //
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
				).replace('A', ' ').replace('B', ' ').replace('C', ' ') //
				;

		Board board = new Board(3, 2);

		board.placeToken(Token.PLAYER_AAA, new Coordinates(0, 1).within(0,0).within(0,0));
		board.placeToken(Token.PLAYER_BBB, new Coordinates(0, 0).within(1,1).within(0,1));

		assertEquals("played d3-s2: ", expected, ("\n" + board.fieldAsPrintableString()));
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
				).replace('A', ' ') //
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
				).replace('A', ' ') //
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
				).replace('A', ' ').replace('B', ' ') //
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
				).replace('A', ' ').replace('B', ' ') //
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
				).replace('A', ' ').replace('B', ' ').replace('C', ' ') //
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
				).replace('A', ' ').replace('B', ' ').replace('C', ' ') //
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
