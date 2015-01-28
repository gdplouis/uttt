package com.uttt.common.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.uttt.common.board.Board;
import com.uttt.common.board.Position;
import com.uttt.common.board.Token;

public class PlayerTest {

	@Test
	public void random_h1s3() {
		Board  board     = new Board(1,3);
		Player playerAAA = PlayerRandom.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerRandom.create(Token.PLAYER_BBB);

		Player player = playerAAA;
		while (board.isPlayable()) {
			Move     move       = player.makeMove(board);
			Position position   = move.toPosition(board);
			Position constraint = position.place(player.getToken());

			assertNull(constraint);

			player = (player == playerAAA) ? playerBBB : playerAAA;

			System.out.println(board.fieldAsPrintableString());
		}

		String expected = "" //
			+ "XXXXXXX\n" //
			+ "Xo|x|oX\n" //
			+ "X-----X\n" //
			+ "Xo|x|xX\n" //
			+ "X-----X\n" //
			+ "Xx|x|oX\n" //
			+ "XXXXXXX\n" //
			+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h2s3_ignoreConstraints() {
		Board  board     = new Board(2,3);
		Player playerAAA = PlayerRandom.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerRandom.create(Token.PLAYER_BBB);

		Player player = playerAAA;
		for (int i = 0; board.isPlayable() && (i < 50); ++i) {
			System.out.println("-------------------------------------------------------------");

			Move     move       = player.makeMove(board);
			System.out.println(move.toString() + " by " + player.getToken());

			Position position   = move.toPosition(board);
			Position constraint = position.place(player.getToken());
			System.out.println("Next move constraint (ignored): " + constraint);

			System.out.println(board.fieldAsPrintableString());

			player = (player == playerAAA) ? playerBBB : playerAAA;
		}

		String expected = "" //
			+ "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
			+ "XXXXXXXX||       ||       X\n" //
			+ "XXo|o|.X|| .|o|x || o|.|o X\n" //
			+ "XX-----X|| ----- || ----- X\n" //
			+ "XX.|o|.X|| x|x|o || .|.|x X\n" //
			+ "XX-----X|| ----- || ----- X\n" //
			+ "XXx|x|xX|| o|x|o || o|o|. X\n" //
			+ "XXXXXXXX||       ||       X\n" //
			+ "X-------------------------X\n" //
			+ "X-------------------------X\n" //
			+ "XOOOOOOO||XXXXXXX||       X\n" //
			+ "XO.|.|oO||X.|.|.X|| .|o|x X\n" //
			+ "XO-----O||X-----X|| ----- X\n" //
			+ "XO.|o|oO||Xx|x|xX|| x|x|. X\n" //
			+ "XO-----O||X-----X|| ----- X\n" //
			+ "XO.|.|oO||X.|.|.X|| o|.|. X\n" //
			+ "XOOOOOOO||XXXXXXX||       X\n" //
			+ "X-------------------------X\n" //
			+ "X-------------------------X\n" //
			+ "X       ||       ||XXXXXXXX\n" //
			+ "X .|.|o || .|x|. ||Xo|.|xXX\n" //
			+ "X ----- || ----- ||X-----XX\n" //
			+ "X x|x|. || .|.|o ||Xx|x|xXX\n" //
			+ "X ----- || ----- ||X-----XX\n" //
			+ "X x|o|x || x|o|o ||Xo|x|.XX\n" //
			+ "X       ||       ||XXXXXXXX\n" //
			+ "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
			+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h3s2_ignoreConstraints() {
		Board  board     = new Board(3,2);
		Player playerAAA = PlayerRandom.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerRandom.create(Token.PLAYER_BBB);

		Player player = playerAAA;
		for (int i = 0; board.isPlayable() && (i < 50); ++i) {
			System.out.println("-------------------------------------------------------------");
			System.out.println("#" + i);

			Move     move       = player.makeMove(board);
			System.out.println(move.toString() + " by " + player.getToken());

			Position position   = move.toPosition(board);
			Position constraint = position.place(player.getToken());
			System.out.println("Next move constraint (ignored): " + constraint);

			System.out.println(board.fieldAsPrintableString());

			player = (player == playerAAA) ? playerBBB : playerAAA;
		}

		String expected = "" //
			+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
			+ "O              |||OOOOOOOOOOOOOOO\n" //
			+ "O      ||      |||OXXXXX||OOOOOOO\n" //
			+ "O  .|. || .|.  |||OXx|.X||Oo|xOOO\n" //
			+ "O  --- || ---  |||OX---X||O---OOO\n" //
			+ "O  .|o || .|.  |||OX.|xX||Oo|.OOO\n" //
			+ "O      ||      |||OXXXXX||OOOOOOO\n" //
			+ "O ------------ |||O------------OO\n" //
			+ "O ------------ |||O------------OO\n" //
			+ "O      ||XXXXX |||OOOOOO||     OO\n" //
			+ "O  .|. ||Xx|.X |||OOo|.O|| .|x OO\n" //
			+ "O  --- ||X---X |||OO---O|| --- OO\n" //
			+ "O  .|x ||Xx|.X |||OOo|.O|| .|. OO\n" //
			+ "O      ||XXXXX |||OOOOOO||     OO\n" //
			+ "O              |||OOOOOOOOOOOOOOO\n" //
			+ "O-------------------------------O\n" //
			+ "O-------------------------------O\n" //
			+ "O-------------------------------O\n" //
			+ "OOOOOOOOOOOOOOO|||              O\n" //
			+ "OOXXXXX||OOOOOO|||      ||      O\n" //
			+ "OOX.|xX||O.|oOO|||  .|. || .|.  O\n" //
			+ "OOX---X||O---OO|||  --- || ---  O\n" //
			+ "OOX.|xX||Ox|oOO|||  .|. || .|.  O\n" //
			+ "OOXXXXX||OOOOOO|||      ||      O\n" //
			+ "OO------------O||| ------------ O\n" //
			+ "OO------------O||| ------------ O\n" //
			+ "OO     ||OOOOOO||| XXXXX||OOOOO O\n" //
			+ "OO o|. ||Oo|.OO||| Xx|.X||O.|oO O\n" //
			+ "OO --- ||O---OO||| X---X||O---O O\n" //
			+ "OO x|. ||O.|oOO||| Xo|xX||O.|oO O\n" //
			+ "OO     ||OOOOOO||| XXXXX||OOOOO O\n" //
			+ "OOOOOOOOOOOOOOO|||              O\n" //
			+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
			+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}
	@Test
	public void random_h3s3_ignoreConstraints() {
		Board  board     = new Board(3,3); // 729 token locations
		Player playerAAA = PlayerRandom.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerRandom.create(Token.PLAYER_BBB);

		Player player = playerAAA;
		for (int i = 0; board.isPlayable() && (i < 800); ++i) {
//			System.out.println("-------------------------------------------------------------"); // TODO:DBG:
//			System.out.println("#" + i);

			Move     move       = player.makeMove(board);
//			System.out.println(move.toString() + " by " + player.getToken()); // TODO:DBG:

			Position position   = move.toPosition(board);
			Position constraint = position.place(player.getToken());
//			System.out.println("Next move constraint (ignored): " + constraint); // TODO:DBG:

//			System.out.println(board.fieldAsPrintableString()); // TODO:DBG:

			player = (player == playerAAA) ? playerBBB : playerAAA;
		}

		String expected = "" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOO|||???????????????????????????|||OOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "OOXXXXXXX||OOOOOOO||OOOOOOOO|||????????||XXXXXXX||XXXXXXX?|||OOOOOOOO||???????||OOOOOOOOO\n" //
				+ "OOXx|x|xX||Oo|o|oO||Ox|x|oOO|||??o|x|o?||X.|o|xX||Xx|o|.X?|||OO.|.|.O||?o|o|x?||O.|x|oOOO\n" //
				+ "OOX-----X||O-----O||O-----OO|||??-----?||X-----X||X-----X?|||OO-----O||?-----?||O-----OOO\n" //
				+ "OOX.|o|.X||Oo|.|.O||Oo|x|.OO|||??o|x|x?||Xx|x|oX||Xo|x|.X?|||OOo|o|.O||?x|x|o?||Ox|.|oOOO\n" //
				+ "OOX-----X||O-----O||O-----OO|||??-----?||X-----X||X-----X?|||OO-----O||?-----?||O-----OOO\n" //
				+ "OOX.|.|.X||O.|o|.O||Oo|o|oOO|||??x|o|x?||Xx|.|oX||X.|.|xX?|||OOo|o|oO||?o|x|o?||O.|.|oOOO\n" //
				+ "OOXXXXXXX||OOOOOOO||OOOOOOOO|||????????||XXXXXXX||XXXXXXX?|||OOOOOOOO||???????||OOOOOOOOO\n" //
				+ "OO-------------------------O|||?-------------------------?|||O-------------------------OO\n" //
				+ "OO-------------------------O|||?-------------------------?|||O-------------------------OO\n" //
				+ "OO       ||OOOOOOO||OOOOOOOO|||?XXXXXXX||???????||XXXXXXX?|||OXXXXXXX||OOOOOOO||XXXXXXXOO\n" //
				+ "OO o|x|x ||Oo|.|oO||Oo|.|oOO|||?Xx|o|xX||?x|o|x?||Xx|x|xX?|||OXo|x|oX||Ox|x|oO||X.|.|.XOO\n" //
				+ "OO ----- ||O-----O||O-----OO|||?X-----X||?-----?||X-----X?|||OX-----X||O-----O||X-----XOO\n" //
				+ "OO .|x|o ||Oo|.|.O||Oo|x|oOO|||?Xo|x|xX||?o|o|x?||Xo|.|xX?|||OXx|x|xX||O.|o|xO||X.|.|.XOO\n" //
				+ "OO ----- ||O-----O||O-----OO|||?X-----X||?-----?||X-----X?|||OX-----X||O-----O||X-----XOO\n" //
				+ "OO o|.|x ||Oo|.|.O||Oo|o|.OO|||?Xo|x|xX||?o|x|o?||X.|.|.X?|||OX.|x|xX||Oo|x|xO||Xx|x|xXOO\n" //
				+ "OO       ||OOOOOOO||OOOOOOOO|||?XXXXXXX||???????||XXXXXXX?|||OXXXXXXX||OOOOOOO||XXXXXXXOO\n" //
				+ "OO-------------------------O|||?-------------------------?|||O-------------------------OO\n" //
				+ "OO-------------------------O|||?-------------------------?|||O-------------------------OO\n" //
				+ "OOOOOOOOO||XXXXXXX||XXXXXXXO|||?XXXXXXX||OOOOOOO||OOOOOOO?|||OXXXXXXX||XXXXXXX||OOOOOOOOO\n" //
				+ "OOO.|o|oO||Xx|x|oX||Xo|x|.XO|||?X.|.|xX||Oo|x|oO||O.|.|oO?|||OXx|o|oX||Xx|x|xX||Oo|x|xOOO\n" //
				+ "OOO-----O||X-----X||X-----XO|||?X-----X||O-----O||O-----O?|||OX-----X||X-----X||O-----OOO\n" //
				+ "OOO.|o|xO||X.|x|xX||X.|x|xXO|||?X.|x|xX||O.|o|xO||Ox|.|oO?|||OXx|x|xX||Xo|o|.X||Oo|o|xOOO\n" //
				+ "OOO-----O||X-----X||X-----XO|||?X-----X||O-----O||O-----O?|||OX-----X||X-----X||O-----OOO\n" //
				+ "OOO.|o|.O||X.|o|xX||X.|x|xXO|||?Xx|x|oX||Oo|o|xO||O.|.|oO?|||OXo|o|.X||Xo|x|xX||Ox|o|oOOO\n" //
				+ "OOOOOOOOO||XXXXXXX||XXXXXXXO|||?XXXXXXX||OOOOOOO||OOOOOOO?|||OXXXXXXX||XXXXXXX||OOOOOOOOO\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOO|||???????????????????????????|||OOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "O---------------------------------------------------------------------------------------O\n" //
				+ "O---------------------------------------------------------------------------------------O\n" //
				+ "O---------------------------------------------------------------------------------------O\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXXO\n" //
				+ "OOXXXXXXX||OOOOOOO||OOOOOOOO|||XOOOOOOO||XXXXXXX||XXXXXXXX|||XXXXXXXX||OOOOOOO||OOOOOOOXO\n" //
				+ "OOXo|o|xX||Oo|.|xO||Oo|o|oOO|||XO.|.|oO||Xx|x|.X||Xx|.|.XX|||XX.|x|oX||Oo|o|.O||Oo|.|oOXO\n" //
				+ "OOX-----X||O-----O||O-----OO|||XO-----O||X-----X||X-----XX|||XX-----X||O-----O||O-----OXO\n" //
				+ "OOXx|x|oX||Oo|o|oO||Oo|x|.OO|||XO.|.|oO||X.|x|oX||Xo|x|oXX|||XXx|x|xX||Oo|.|.O||O.|o|oOXO\n" //
				+ "OOX-----X||O-----O||O-----OO|||XO-----O||X-----X||X-----XX|||XX-----X||O-----O||O-----OXO\n" //
				+ "OOXx|o|.X||O.|x|oO||Oo|x|.OO|||XO.|.|oO||Xo|o|xX||Xx|o|xXX|||XXo|o|.X||Oo|x|.O||Oo|o|xOXO\n" //
				+ "OOXXXXXXX||OOOOOOO||OOOOOOOO|||XOOOOOOO||XXXXXXX||XXXXXXXX|||XXXXXXXX||OOOOOOO||OOOOOOOXO\n" //
				+ "OO-------------------------O|||X-------------------------X|||X-------------------------XO\n" //
				+ "OO-------------------------O|||X-------------------------X|||X-------------------------XO\n" //
				+ "OOXXXXXXX||       ||OOOOOOOO|||X       ||XXXXXXX||???????X|||XXXXXXXX||       ||       XO\n" //
				+ "OOXo|o|xX|| .|.|x ||Oo|o|oOO|||X o|.|. ||Xo|.|.X||?x|o|x?X|||XXo|x|xX|| o|.|x || x|.|. XO\n" //
				+ "OOX-----X|| ----- ||O-----OO|||X ----- ||X-----X||?-----?X|||XX-----X|| ----- || ----- XO\n" //
				+ "OOXx|x|oX|| o|o|x ||Ox|x|oOO|||X x|.|x ||Xx|x|xX||?x|o|x?X|||XXo|x|oX|| x|x|o || .|o|. XO\n" //
				+ "OOX-----X|| ----- ||O-----OO|||X ----- ||X-----X||?-----?X|||XX-----X|| ----- || ----- XO\n" //
				+ "OOXx|x|oX|| x|o|o ||Oo|o|xOO|||X .|.|o ||Xo|x|oX||?o|x|o?X|||XXx|o|xX|| o|.|o || o|x|x XO\n" //
				+ "OOXXXXXXX||       ||OOOOOOOO|||X       ||XXXXXXX||???????X|||XXXXXXXX||       ||       XO\n" //
				+ "OO-------------------------O|||X-------------------------X|||X-------------------------XO\n" //
				+ "OO-------------------------O|||X-------------------------X|||X-------------------------XO\n" //
				+ "OO???????||XXXXXXX||OOOOOOOO|||XXXXXXXX||       ||       X|||XXXXXXXX||       ||XXXXXXXXO\n" //
				+ "OO?x|o|o?||Xo|x|oX||Ox|x|.OO|||XX.|.|xX|| x|.|x || x|o|o X|||XX.|o|xX|| x|.|x ||X.|.|oXXO\n" //
				+ "OO?-----?||X-----X||O-----OO|||XX-----X|| ----- || ----- X|||XX-----X|| ----- ||X-----XXO\n" //
				+ "OO?o|x|x?||Xx|x|oX||Oo|o|oOO|||XXo|.|oX|| x|o|o || .|.|x X|||XXx|x|oX|| o|.|x ||Xx|.|xXXO\n" //
				+ "OO?-----?||X-----X||O-----OO|||XX-----X|| ----- || ----- X|||XX-----X|| ----- ||X-----XXO\n" //
				+ "OO?o|x|o?||Xo|x|.X||Oo|o|xOO|||XXx|x|xX|| o|x|o || x|.|. X|||XXx|.|oX|| .|.|. ||Xx|x|xXXO\n" //
				+ "OO???????||XXXXXXX||OOOOOOOO|||XXXXXXXX||       ||       X|||XXXXXXXX||       ||XXXXXXXXO\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXXO\n" //
				+ "O---------------------------------------------------------------------------------------O\n" //
				+ "O---------------------------------------------------------------------------------------O\n" //
				+ "O---------------------------------------------------------------------------------------O\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXXO\n" //
				+ "OOOOOOOOO||OOOOOOO||OOOOOOOO|||XXXXXXXX||XXXXXXX||       X|||X       ||XXXXXXX||XXXXXXXXO\n" //
				+ "OOOx|.|xO||Oo|o|oO||O.|.|oOO|||XXx|x|xX||X.|.|xX|| x|.|. X|||X x|o|. ||Xo|.|.X||Xx|.|oXXO\n" //
				+ "OOO-----O||O-----O||O-----OO|||XX-----X||X-----X|| ----- X|||X ----- ||X-----X||X-----XXO\n" //
				+ "OOOx|.|xO||O.|.|xO||O.|o|xOO|||XXo|x|xX||X.|x|.X|| .|.|o X|||X o|x|o ||Xx|x|xX||X.|x|.XXO\n" //
				+ "OOO-----O||O-----O||O-----OO|||XX-----X||X-----X|| ----- X|||X ----- ||X-----X||X-----XXO\n" //
				+ "OOOo|o|oO||O.|.|.O||Oo|.|xOO|||XXo|.|oX||Xx|.|.X|| .|o|. X|||X o|.|o ||Xo|x|oX||Xo|.|xXXO\n" //
				+ "OOOOOOOOO||OOOOOOO||OOOOOOOO|||XXXXXXXX||XXXXXXX||       X|||X       ||XXXXXXX||XXXXXXXXO\n" //
				+ "OO-------------------------O|||X-------------------------X|||X-------------------------XO\n" //
				+ "OO-------------------------O|||X-------------------------X|||X-------------------------XO\n" //
				+ "OOOOOOOOO||OOOOOOO||       O|||X       ||XXXXXXX||       X|||XOOOOOOO||XXXXXXX||XXXXXXXXO\n" //
				+ "OOO.|x|oO||O.|o|oO|| x|x|. O|||X .|.|. ||X.|.|xX|| .|.|o X|||XOo|.|xO||Xx|x|.X||Xo|x|xXXO\n" //
				+ "OOO-----O||O-----O|| ----- O|||X ----- ||X-----X|| ----- X|||XO-----O||X-----X||X-----XXO\n" //
				+ "OOO.|.|xO||O.|o|.O|| x|o|x O|||X .|x|o ||X.|.|xX|| x|.|. X|||XO.|o|.O||Xo|o|.X||Xx|x|oXXO\n" //
				+ "OOO-----O||O-----O|| ----- O|||X ----- ||X-----X|| ----- X|||XO-----O||X-----X||X-----XXO\n" //
				+ "OOOo|o|oO||Ox|o|oO|| .|.|. O|||X .|.|o ||X.|.|xX|| x|.|x X|||XO.|.|oO||Xx|x|xX||Xo|x|xXXO\n" //
				+ "OOOOOOOOO||OOOOOOO||       O|||X       ||XXXXXXX||       X|||XOOOOOOO||XXXXXXX||XXXXXXXXO\n" //
				+ "OO-------------------------O|||X-------------------------X|||X-------------------------XO\n" //
				+ "OO-------------------------O|||X-------------------------X|||X-------------------------XO\n" //
				+ "OO       ||       ||OOOOOOOO|||X       ||OOOOOOO||XXXXXXXX|||X       ||OOOOOOO||XXXXXXXXO\n" //
				+ "OO .|.|. || o|x|. ||O.|o|xOO|||X .|.|x ||O.|.|.O||X.|x|xXX|||X x|.|x ||Oo|x|.O||Xo|x|oXXO\n" //
				+ "OO ----- || ----- ||O-----OO|||X ----- ||O-----O||X-----XX|||X ----- ||O-----O||X-----XXO\n" //
				+ "OO .|o|x || x|o|o ||O.|o|.OO|||X x|.|o ||O.|.|.O||X.|x|.XX|||X .|o|x ||Oo|x|.O||X.|x|oXXO\n" //
				+ "OO ----- || ----- ||O-----OO|||X ----- ||O-----O||X-----XX|||X ----- ||O-----O||X-----XXO\n" //
				+ "OO x|.|o || o|.|. ||Oo|o|oOO|||X x|o|o ||Oo|o|oO||Xx|.|xXX|||X x|x|o ||Oo|.|xO||Xo|x|xXXO\n" //
				+ "OO       ||       ||OOOOOOOO|||X       ||OOOOOOO||XXXXXXXX|||X       ||OOOOOOO||XXXXXXXXO\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXXO\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}
}
