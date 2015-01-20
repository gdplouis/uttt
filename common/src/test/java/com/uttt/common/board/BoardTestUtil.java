package com.uttt.common.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import com.uttt.common.Foreachable;

public class BoardTestUtil {

	public static Token flip(Token t) {
		return (t == Token.PLAYER_AAA ? Token.PLAYER_BBB: Token.PLAYER_AAA);
	}

	public static Token flipIf(boolean b, Token t) {
		return (b ? flip(t) : t);
	}

	public static Board forceRowWin(Board board, int row, Token winner) {

		final boolean isBottom = (board.getHeight() <= 1);

		for(final int col : Foreachable.until(board.getSize())) {
			if (isBottom) {
				board.at(row, col).place(winner);
			} else {
				forceRowWin(board.getSubBoard(row, col), row, winner);
			}
		}

		return board;
	}

	public static Board forceColWin(Board board, int col, Token winner) {

		final boolean isBottom = (board.getHeight() <= 1);

		for(final int row : Foreachable.until(board.getSize())) {
			if (isBottom) {
				board.at(row, col).place(winner);
			} else {
				forceColWin(board.getSubBoard(row, col), col, winner);
			}
		}

		return board;
	}

	public static Board forceDiagWin(Board board, boolean inverted, Token winner) {

		final boolean isBottom = (board.getHeight() <= 1);

		if (!inverted) {
			for (final int diag : Foreachable.until(board.getSize())) {
				if (isBottom) {
					board.at(diag, diag).place(winner);
				} else {
					forceDiagWin(board.getSubBoard(diag, diag), inverted, winner);
				}
			}
		} else {
			for (final int row : Foreachable.until(board.getSize())) {
				final int col = board.getSize() - 1 - row;
				if (isBottom) {
					board.at(row, col).place(winner);
				} else {
					forceDiagWin(board.getSubBoard(row, col), inverted, winner);
				}
			}
		}

		return board;
	}

	public static Board forceDraw(Board board) {
		assertTrue("board.getSize() >= 3: ", (board.getSize() >= 3));

		final boolean isBottom = (board.getHeight() <= 1);

		for (final int row : Foreachable.until(board.getSize())) {
			for (final int col : Foreachable.until(board.getSize())) {
				boolean evenRowPair = (0 == ((row / 2) % 2));
				boolean evenCol     = (0 == ((col    ) % 2));

				Token token = flipIf(evenCol, (evenRowPair ? Token.PLAYER_BBB : Token.PLAYER_AAA));

				if (isBottom) {
					board.at(row, col).place(token);
				} else {
					forceDraw(board.getSubBoard(row, col));
				}
			}
		}

		return board;
	}

	// ====================================================================================================

	@Test
	public void forceRowWin_h1s3() {
		final String expected = ("\n" //
				+ "XXXXXXX\n" //
				+ "Xx|x|xX\n" //
				+ "X-----X\n" //
				+ "X.|.|.X\n" //
				+ "X-----X\n" //
				+ "X.|.|.X\n" //
				+ "XXXXXXX\n" //
				+ "TOP.\n").replaceAll("[ABC]", " ") //
				;

		Board board = forceRowWin(new Board(1, 3), 0, Token.PLAYER_AAA);

		assertEquals("h1s3 - forced row win [0]: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void forceRowWin_h2s3() {
		final String expected = ("\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "OAAAAAAA||AAAAAAA||AAAAAAAO\n" //
				+ "OA.|.|.A||A.|.|.A||A.|.|.AO\n" //
				+ "OA-----A||A-----A||A-----AO\n" //
				+ "OA.|.|.A||A.|.|.A||A.|.|.AO\n" //
				+ "OA-----A||A-----A||A-----AO\n" //
				+ "OA.|.|.A||A.|.|.A||A.|.|.AO\n" //
				+ "OAAAAAAA||AAAAAAA||AAAAAAAO\n" //
				+ "O-------------------------O\n" //
				+ "O-------------------------O\n" //
				+ "OOOOOOOO||OOOOOOO||OOOOOOOO\n" //
				+ "OO.|.|.O||O.|.|.O||O.|.|.OO\n" //
				+ "OO-----O||O-----O||O-----OO\n" //
				+ "OOo|o|oO||Oo|o|oO||Oo|o|oOO\n" //
				+ "OO-----O||O-----O||O-----OO\n" //
				+ "OO.|.|.O||O.|.|.O||O.|.|.OO\n" //
				+ "OOOOOOOO||OOOOOOO||OOOOOOOO\n" //
				+ "O-------------------------O\n" //
				+ "O-------------------------O\n" //
				+ "OAAAAAAA||AAAAAAA||AAAAAAAO\n" //
				+ "OA.|.|.A||A.|.|.A||A.|.|.AO\n" //
				+ "OA-----A||A-----A||A-----AO\n" //
				+ "OA.|.|.A||A.|.|.A||A.|.|.AO\n" //
				+ "OA-----A||A-----A||A-----AO\n" //
				+ "OA.|.|.A||A.|.|.A||A.|.|.AO\n" //
				+ "OAAAAAAA||AAAAAAA||AAAAAAAO\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "TOP.\n").replaceAll("[ABC]", " ") //
				;

		Board board = forceRowWin(new Board(2, 3), 1, Token.PLAYER_BBB);

		assertEquals("h2s3 - forced row win [1]: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test @Ignore
	public void forceRowWin_h3s4() {
		fail("NYI");
	}

	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	@Test
	public void forceColWin_h1s3() {
		final String expected = ("\n" //
				+ "XXXXXXX\n" //
				+ "Xx|.|.X\n" //
				+ "X-----X\n" //
				+ "Xx|.|.X\n" //
				+ "X-----X\n" //
				+ "Xx|.|.X\n" //
				+ "XXXXXXX\n" //
				+ "TOP.\n").replaceAll("[ABC]", " ") //
				;

		Board board = forceColWin(new Board(1, 3), 0, Token.PLAYER_AAA);

		assertEquals("h1s3 - forced col win [0]: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void forceColWin_h2s3() {
		final String expected = ("\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "OAAAAAAA||OOOOOOO||AAAAAAAO\n" //
				+ "OA.|.|.A||O.|o|.O||A.|.|.AO\n" //
				+ "OA-----A||O-----O||A-----AO\n" //
				+ "OA.|.|.A||O.|o|.O||A.|.|.AO\n" //
				+ "OA-----A||O-----O||A-----AO\n" //
				+ "OA.|.|.A||O.|o|.O||A.|.|.AO\n" //
				+ "OAAAAAAA||OOOOOOO||AAAAAAAO\n" //
				+ "O-------------------------O\n" //
				+ "O-------------------------O\n" //
				+ "OAAAAAAA||OOOOOOO||AAAAAAAO\n" //
				+ "OA.|.|.A||O.|o|.O||A.|.|.AO\n" //
				+ "OA-----A||O-----O||A-----AO\n" //
				+ "OA.|.|.A||O.|o|.O||A.|.|.AO\n" //
				+ "OA-----A||O-----O||A-----AO\n" //
				+ "OA.|.|.A||O.|o|.O||A.|.|.AO\n" //
				+ "OAAAAAAA||OOOOOOO||AAAAAAAO\n" //
				+ "O-------------------------O\n" //
				+ "O-------------------------O\n" //
				+ "OAAAAAAA||OOOOOOO||AAAAAAAO\n" //
				+ "OA.|.|.A||O.|o|.O||A.|.|.AO\n" //
				+ "OA-----A||O-----O||A-----AO\n" //
				+ "OA.|.|.A||O.|o|.O||A.|.|.AO\n" //
				+ "OA-----A||O-----O||A-----AO\n" //
				+ "OA.|.|.A||O.|o|.O||A.|.|.AO\n" //
				+ "OAAAAAAA||OOOOOOO||AAAAAAAO\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "TOP.\n").replaceAll("[ABC]", " ") //
				;

		Board board = forceColWin(new Board(2, 3), 1, Token.PLAYER_BBB);

		assertEquals("h2s3 - forced col win [1]: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test @Ignore
	public void forceColWin_h3s4() {
		fail("NYI");
	}

	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	@Test
	public void forceDiagWin_h1s3() {
		{
			final String expected = ("\n" //
					+ "XXXXXXX\n" //
					+ "Xx|.|.X\n" //
					+ "X-----X\n" //
					+ "X.|x|.X\n" //
					+ "X-----X\n" //
					+ "X.|.|xX\n" //
					+ "XXXXXXX\n" //
					+ "TOP.\n").replaceAll("[ABC]", " ") //
					;

			Board board = forceDiagWin(new Board(1, 3), false, Token.PLAYER_AAA);

			assertEquals("h1s3 - forced daig win: ", expected, ("\n" + board.fieldAsPrintableString()));
		}
		{
			final String expected = ("\n" //
					+ "XXXXXXX\n" //
					+ "X.|.|xX\n" //
					+ "X-----X\n" //
					+ "X.|x|.X\n" //
					+ "X-----X\n" //
					+ "Xx|.|.X\n" //
					+ "XXXXXXX\n" //
					+ "TOP.\n").replaceAll("[ABC]", " ") //
					;

			Board board = forceDiagWin(new Board(1, 3), true, Token.PLAYER_AAA);

			assertEquals("h1s3 - forced inverted diag win: ", expected, ("\n" + board.fieldAsPrintableString()));
		}
	}

	@Test
	public void forceDiagWin_h2s3() {
		{
			final String expected = ("\n" //
					+ "OOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
					+ "OOOOOOOO||AAAAAAA||AAAAAAAO\n" //
					+ "OOo|.|.O||A.|.|.A||A.|.|.AO\n" //
					+ "OO-----O||A-----A||A-----AO\n" //
					+ "OO.|o|.O||A.|.|.A||A.|.|.AO\n" //
					+ "OO-----O||A-----A||A-----AO\n" //
					+ "OO.|.|oO||A.|.|.A||A.|.|.AO\n" //
					+ "OOOOOOOO||AAAAAAA||AAAAAAAO\n" //
					+ "O-------------------------O\n" //
					+ "O-------------------------O\n" //
					+ "OAAAAAAA||OOOOOOO||AAAAAAAO\n" //
					+ "OA.|.|.A||Oo|.|.O||A.|.|.AO\n" //
					+ "OA-----A||O-----O||A-----AO\n" //
					+ "OA.|.|.A||O.|o|.O||A.|.|.AO\n" //
					+ "OA-----A||O-----O||A-----AO\n" //
					+ "OA.|.|.A||O.|.|oO||A.|.|.AO\n" //
					+ "OAAAAAAA||OOOOOOO||AAAAAAAO\n" //
					+ "O-------------------------O\n" //
					+ "O-------------------------O\n" //
					+ "OAAAAAAA||AAAAAAA||OOOOOOOO\n" //
					+ "OA.|.|.A||A.|.|.A||Oo|.|.OO\n" //
					+ "OA-----A||A-----A||O-----OO\n" //
					+ "OA.|.|.A||A.|.|.A||O.|o|.OO\n" //
					+ "OA-----A||A-----A||O-----OO\n" //
					+ "OA.|.|.A||A.|.|.A||O.|.|oOO\n" //
					+ "OAAAAAAA||AAAAAAA||OOOOOOOO\n" //
					+ "OOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
					+ "TOP.\n").replaceAll("[ABC]", " ") //
					;

			Board board = forceDiagWin(new Board(2, 3), false, Token.PLAYER_BBB);

			assertEquals("h2s3 - forced diag win: ", expected, ("\n" + board.fieldAsPrintableString()));
		}
		{
			final String expected = ("\n" //
					+ "OOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
					+ "OAAAAAAA||AAAAAAA||OOOOOOOO\n" //
					+ "OA.|.|.A||A.|.|.A||O.|.|oOO\n" //
					+ "OA-----A||A-----A||O-----OO\n" //
					+ "OA.|.|.A||A.|.|.A||O.|o|.OO\n" //
					+ "OA-----A||A-----A||O-----OO\n" //
					+ "OA.|.|.A||A.|.|.A||Oo|.|.OO\n" //
					+ "OAAAAAAA||AAAAAAA||OOOOOOOO\n" //
					+ "O-------------------------O\n" //
					+ "O-------------------------O\n" //
					+ "OAAAAAAA||OOOOOOO||AAAAAAAO\n" //
					+ "OA.|.|.A||O.|.|oO||A.|.|.AO\n" //
					+ "OA-----A||O-----O||A-----AO\n" //
					+ "OA.|.|.A||O.|o|.O||A.|.|.AO\n" //
					+ "OA-----A||O-----O||A-----AO\n" //
					+ "OA.|.|.A||Oo|.|.O||A.|.|.AO\n" //
					+ "OAAAAAAA||OOOOOOO||AAAAAAAO\n" //
					+ "O-------------------------O\n" //
					+ "O-------------------------O\n" //
					+ "OOOOOOOO||AAAAAAA||AAAAAAAO\n" //
					+ "OO.|.|oO||A.|.|.A||A.|.|.AO\n" //
					+ "OO-----O||A-----A||A-----AO\n" //
					+ "OO.|o|.O||A.|.|.A||A.|.|.AO\n" //
					+ "OO-----O||A-----A||A-----AO\n" //
					+ "OOo|.|.O||A.|.|.A||A.|.|.AO\n" //
					+ "OOOOOOOO||AAAAAAA||AAAAAAAO\n" //
					+ "OOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
					+ "TOP.\n").replaceAll("[ABC]", " ") //
					;

			Board board = forceDiagWin(new Board(2, 3), true, Token.PLAYER_BBB);

			assertEquals("h2s3 - forced diag win: ", expected, ("\n" + board.fieldAsPrintableString()));
		}
	}

	@Test @Ignore
	public void forceDiagWin_h3s4() {
		fail("NYI");
	}

	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	@Test
	public void forceDraw_h1s3() {
		final String expected = ("\n" //
				+ "AAAAAAA\n" //
				+ "Ax|o|xA\n" //
				+ "A-----A\n" //
				+ "Ax|o|xA\n" //
				+ "A-----A\n" //
				+ "Ao|x|oA\n" //
				+ "AAAAAAA\n" //
				+ "TOP.\n").replaceAll("[A]", "?").replaceAll("[ABC]", " ") //
				;

		Board board = forceDraw(new Board(1, 3));

		assertEquals("h1s3 - forced draw: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void forceDraw_h1s4() {
		final String expected = ("\n" //
				+ "AAAAAAAAA\n" //
				+ "Ax|o|x|oA\n" //
				+ "A-------A\n" //
				+ "Ax|o|x|oA\n" //
				+ "A-------A\n" //
				+ "Ao|x|o|xA\n" //
				+ "A-------A\n" //
				+ "Ao|x|o|xA\n" //
				+ "AAAAAAAAA\n" //
				+ "TOP.\n").replaceAll("[A]", "?").replaceAll("[ABC]", " ") //
				;

		Board board = forceDraw(new Board(1, 4));

		assertEquals("h1s3 - forced draw: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void forceDraw_h1s5() {
		final String expected = ("\n" //
				+ "AAAAAAAAAAA\n" //
				+ "Ax|o|x|o|xA\n" //
				+ "A---------A\n" //
				+ "Ax|o|x|o|xA\n" //
				+ "A---------A\n" //
				+ "Ao|x|o|x|oA\n" //
				+ "A---------A\n" //
				+ "Ao|x|o|x|oA\n" //
				+ "A---------A\n" //
				+ "Ax|o|x|o|xA\n" //
				+ "AAAAAAAAAAA\n" //
				+ "TOP.\n").replaceAll("[A]", "?").replaceAll("[ABC]", " ") //
				;

		Board board = forceDraw(new Board(1, 5));

		assertEquals("h1s3 - forced draw: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void forceDraw_h2s3() {
		final String expected = ("\n" //
				+ "BBBBBBBBBBBBBBBBBBBBBBBBBBB\n" //
				+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
				+ "BAx|o|xA||Ax|o|xA||Ax|o|xAB\n" //
				+ "BA-----A||A-----A||A-----AB\n" //
				+ "BAx|o|xA||Ax|o|xA||Ax|o|xAB\n" //
				+ "BA-----A||A-----A||A-----AB\n" //
				+ "BAo|x|oA||Ao|x|oA||Ao|x|oAB\n" //
				+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
				+ "B-------------------------B\n" //
				+ "B-------------------------B\n" //
				+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
				+ "BAx|o|xA||Ax|o|xA||Ax|o|xAB\n" //
				+ "BA-----A||A-----A||A-----AB\n" //
				+ "BAx|o|xA||Ax|o|xA||Ax|o|xAB\n" //
				+ "BA-----A||A-----A||A-----AB\n" //
				+ "BAo|x|oA||Ao|x|oA||Ao|x|oAB\n" //
				+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
				+ "B-------------------------B\n" //
				+ "B-------------------------B\n" //
				+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
				+ "BAx|o|xA||Ax|o|xA||Ax|o|xAB\n" //
				+ "BA-----A||A-----A||A-----AB\n" //
				+ "BAx|o|xA||Ax|o|xA||Ax|o|xAB\n" //
				+ "BA-----A||A-----A||A-----AB\n" //
				+ "BAo|x|oA||Ao|x|oA||Ao|x|oAB\n" //
				+ "BAAAAAAA||AAAAAAA||AAAAAAAB\n" //
				+ "BBBBBBBBBBBBBBBBBBBBBBBBBBB\n" //
				+ "TOP.\n").replaceAll("[AB]", "?").replaceAll("[ABC]", " ") //
				;

		Board board = forceDraw(new Board(2, 3));

		assertEquals("h2s3 - forced draw: ", expected, ("\n" + board.fieldAsPrintableString()));
	}

	@Test
	public void forceDraw_h3s4() {
		final String expected = ("\n" //
				+ "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC\n" //
				+ "CBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBC\n" //
				+ "C-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------C\n" //
				+ "C-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------C\n" //
				+ "C-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------C\n" //
				+ "CBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBC\n" //
				+ "C-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------C\n" //
				+ "C-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------C\n" //
				+ "C-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------C\n" //
				+ "CBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBC\n" //
				+ "C-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------C\n" //
				+ "C-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------C\n" //
				+ "C-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------C\n" //
				+ "CBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CB------------------------------------------B|||B------------------------------------------B|||B------------------------------------------B|||B------------------------------------------BC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oAB|||BAx|o|x|oA||Ax|o|x|oA||Ax|o|x|oA||Ax|o|x|oABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------AB|||BA-------A||A-------A||A-------A||A-------ABC\n" //
				+ "CBAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xAB|||BAo|x|o|xA||Ao|x|o|xA||Ao|x|o|xA||Ao|x|o|xABC\n" //
				+ "CBAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAAB|||BAAAAAAAAA||AAAAAAAAA||AAAAAAAAA||AAAAAAAAABC\n" //
				+ "CBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|||BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBC\n" //
				+ "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC\n" //
				+ "TOP.\n").replaceAll("[ABC]", "?") //
				;


		Board board = forceDraw(new Board(3, 4));

		assertEquals("h3s4 - forced draw: ", expected, ("\n" + board.fieldAsPrintableString()));
	}
}
