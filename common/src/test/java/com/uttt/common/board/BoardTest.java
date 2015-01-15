package com.uttt.common.board;

import static org.junit.Assert.assertEquals;
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

	@Test
	public void dumpField_heightOne() {
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

		assertEquals("empty 3x3: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void dumpField_heightTwo() {
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

		Board board = new Board(2, STANDARD_SIZE);

		assertEquals("empty 3x3: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void dumpField_heightThree() {
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

		assertEquals("empty 3x3: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void dumpField_heightFour() {

		Board board = new Board(4, STANDARD_SIZE);

		board.fieldAsPrintableString();
	}
}
