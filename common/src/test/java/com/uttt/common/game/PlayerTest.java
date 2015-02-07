package com.uttt.common.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.uttt.common.StackFrameUtil;
import com.uttt.common.board.Board;
import com.uttt.common.board.Position;
import com.uttt.common.board.Token;

public class PlayerTest {

	@Test
	public void random_h1s3() {

		final Board board = random_h1s3Board();
		final String expected = "" //
				+ "OOOOOOO\n" //
				+ "Ox|x|oO\n" //
				+ "O-----O\n" //
				+ "O.|o|.O\n" //
				+ "O-----O\n" //
				+ "Oo|.|xO\n" //
				+ "OOOOOOO\n" //
				+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}
	
	@Test
	public void random_h2s3_ignoreConstraints() {

		final Board board = random_h2s3_ignoreConstraintsBoard();
		final String expected = "" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "X       ||       ||OOOOOOOX\n" //
				+ "X .|x|x || x|o|. ||Oo|.|.OX\n" //
				+ "X ----- || ----- ||O-----OX\n" //
				+ "X x|.|x || .|o|. ||O.|o|.OX\n" //
				+ "X ----- || ----- ||O-----OX\n" //
				+ "X x|o|. || o|.|. ||Oo|x|oOX\n" //
				+ "X       ||       ||OOOOOOOX\n" //
				+ "X-------------------------X\n" //
				+ "X-------------------------X\n" //
				+ "XXXXXXXX||OOOOOOO||       X\n" //
				+ "XX.|.|.X||O.|o|oO|| o|.|x X\n" //
				+ "XX-----X||O-----O|| ----- X\n" //
				+ "XXx|x|xX||O.|.|.O|| .|o|. X\n" //
				+ "XX-----X||O-----O|| ----- X\n" //
				+ "XX.|.|.X||Oo|o|oO|| o|x|. X\n" //
				+ "XXXXXXXX||OOOOOOO||       X\n" //
				+ "X-------------------------X\n" //
				+ "X-------------------------X\n" //
				+ "XXXXXXXX||XXXXXXX||XXXXXXXX\n" //
				+ "XXo|x|oX||Xo|x|xX||Xx|x|oXX\n" //
				+ "XX-----X||X-----X||X-----XX\n" //
				+ "XXo|x|xX||X.|.|xX||X.|x|.XX\n" //
				+ "XX-----X||X-----X||X-----XX\n" //
				+ "XXx|x|oX||Xo|o|xX||Xx|x|oXX\n" //
				+ "XXXXXXXX||XXXXXXX||XXXXXXXX\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h2s3_followConstraints() {

		final Board board = random_h2s3_followConstraintsBoard();
		final String expected = "" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "XXXXXXXX||       ||OOOOOOOX\n" //
				+ "XXx|x|.X|| o|.|. ||Oo|x|xOX\n" //
				+ "XX-----X|| ----- ||O-----OX\n" //
				+ "XXo|x|oX|| x|.|x ||O.|.|xOX\n" //
				+ "XX-----X|| ----- ||O-----OX\n" //
				+ "XXx|x|.X|| .|o|x ||Oo|o|oOX\n" //
				+ "XXXXXXXX||       ||OOOOOOOX\n" //
				+ "X-------------------------X\n" //
				+ "X-------------------------X\n" //
				+ "XXXXXXXX||       ||OOOOOOOX\n" //
				+ "XXo|o|.X|| .|o|o ||Oo|.|oOX\n" //
				+ "XX-----X|| ----- ||O-----OX\n" //
				+ "XXo|x|oX|| o|.|x ||Ox|o|xOX\n" //
				+ "XX-----X|| ----- ||O-----OX\n" //
				+ "XXx|x|xX|| .|.|o ||Oo|o|.OX\n" //
				+ "XXXXXXXX||       ||OOOOOOOX\n" //
				+ "X-------------------------X\n" //
				+ "X-------------------------X\n" //
				+ "XXXXXXXX||OOOOOOO||       X\n" //
				+ "XX.|o|oX||Oo|.|xO|| .|.|x X\n" //
				+ "XX-----X||O-----O|| ----- X\n" //
				+ "XXx|x|xX||Oo|x|xO|| .|.|x X\n" //
				+ "XX-----X||O-----O|| ----- X\n" //
				+ "XXx|.|.X||Oo|x|.O|| o|x|o X\n" //
				+ "XXXXXXXX||OOOOOOO||       X\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h3s2_ignoreConstraints() {

		final Board board = random_h3s2_ignoreConstraintsBoard();
		final String expected = "" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "OXXXXXXXXXXXXXX|||              O\n" //
				+ "OXXXXXX||     X|||      ||XXXXX O\n" //
				+ "OXXx|.X|| .|. X|||  x|. ||X.|xX O\n" //
				+ "OXX---X|| --- X|||  --- ||X---X O\n" //
				+ "OXXx|.X|| .|. X|||  .|. ||Xx|.X O\n" //
				+ "OXXXXXX||     X|||      ||XXXXX O\n" //
				+ "OX------------X||| ------------ O\n" //
				+ "OX------------X||| ------------ O\n" //
				+ "OXOOOOO||XXXXXX|||      ||      O\n" //
				+ "OXOo|.O||Xx|xXX|||  .|. || x|.  O\n" //
				+ "OXO---O||X---XX|||  --- || ---  O\n" //
				+ "OXO.|oO||X.|.XX|||  .|o || o|.  O\n" //
				+ "OXOOOOO||XXXXXX|||      ||      O\n" //
				+ "OXXXXXXXXXXXXXX|||              O\n" //
				+ "O-------------------------------O\n" //
				+ "O-------------------------------O\n" //
				+ "O-------------------------------O\n" //
				+ "OOOOOOOOOOOOOOO|||OOOOOOOOOOOOOOO\n" //
				+ "OOOOOOO||OOOOOO|||O     ||OOOOOOO\n" //
				+ "OOOo|xO||O.|.OO|||O o|. ||O.|oOOO\n" //
				+ "OOO---O||O---OO|||O --- ||O---OOO\n" //
				+ "OOOo|.O||Oo|oOO|||O .|. ||Ox|oOOO\n" //
				+ "OOOOOOO||OOOOOO|||O     ||OOOOOOO\n" //
				+ "OO------------O|||O------------OO\n" //
				+ "OO------------O|||O------------OO\n" //
				+ "OOXXXXX||     O|||OXXXXX||OOOOOOO\n" //
				+ "OOXx|.X|| .|. O|||OXo|xX||Ox|oOOO\n" //
				+ "OOX---X|| --- O|||OX---X||O---OOO\n" //
				+ "OOXx|.X|| .|o O|||OXx|.X||Oo|.OOO\n" //
				+ "OOXXXXX||     O|||OXXXXX||OOOOOOO\n" //
				+ "OOOOOOOOOOOOOOO|||OOOOOOOOOOOOOOO\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h3s2_followConstraints() {

		final Board board = random_h3s2_followConstraintsBoard();
		final String expected = "" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "X              |||XXXXXXXXXXXXXXX\n" //
				+ "X      ||      |||XXXXXX||     XX\n" //
				+ "X  .|. || .|.  |||XXx|.X|| x|. XX\n" //
				+ "X  --- || ---  |||XX---X|| --- XX\n" //
				+ "X  .|. || .|.  |||XXx|oX|| .|. XX\n" //
				+ "X      ||      |||XXXXXX||     XX\n" //
				+ "X ------------ |||X------------XX\n" //
				+ "X ------------ |||X------------XX\n" //
				+ "X      ||      |||XOOOOO||XXXXXXX\n" //
				+ "X  .|. || .|.  |||XOo|oO||Xo|xXXX\n" //
				+ "X  --- || ---  |||XO---O||X---XXX\n" //
				+ "X  .|. || .|.  |||XO.|.O||Xx|.XXX\n" //
				+ "X      ||      |||XOOOOO||XXXXXXX\n" //
				+ "X              |||XXXXXXXXXXXXXXX\n" //
				+ "X-------------------------------X\n" //
				+ "X-------------------------------X\n" //
				+ "X-------------------------------X\n" //
				+ "X              |||XXXXXXXXXXXXXXX\n" //
				+ "X      ||      |||X     ||XXXXXXX\n" //
				+ "X  .|. || .|.  |||X .|o ||X.|xXXX\n" //
				+ "X  --- || ---  |||X --- ||X---XXX\n" //
				+ "X  .|. || .|.  |||X .|. ||X.|xXXX\n" //
				+ "X      ||      |||X     ||XXXXXXX\n" //
				+ "X ------------ |||X------------XX\n" //
				+ "X ------------ |||X------------XX\n" //
				+ "X      ||      |||XXXXXX||OOOOOXX\n" //
				+ "X  .|. || .|.  |||XXx|.X||O.|oOXX\n" //
				+ "X  --- || ---  |||XX---X||O---OXX\n" //
				+ "X  .|. || .|.  |||XXo|xX||Oo|.OXX\n" //
				+ "X      ||      |||XXXXXX||OOOOOXX\n" //
				+ "X              |||XXXXXXXXXXXXXXX\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h3s3_ignoreConstraints() {

		final Board board = random_h3s3_ignoreConstraintsBoard();
		final String expected = "" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOOOOOOOOOOOOOOX\n" //
				+ "XXOOOOOOO||XXXXXXX||XXXXXXXX|||XXXXXXXX||       ||XXXXXXXX|||O       ||XXXXXXX||OOOOOOOOX\n" //
				+ "XXOo|o|.O||Xo|.|.X||X.|o|xXX|||XXx|x|xX|| .|.|. ||Xx|.|.XX|||O x|.|. ||X.|x|.X||Oo|.|.OOX\n" //
				+ "XXO-----O||X-----X||X-----XX|||XX-----X|| ----- ||X-----XX|||O ----- ||X-----X||O-----OOX\n" //
				+ "XXOo|.|oO||Xo|x|xX||Xx|.|xXX|||XX.|.|oX|| x|.|. ||Xx|o|.XX|||O o|o|. ||X.|x|.X||Oo|x|.OOX\n" //
				+ "XXO-----O||X-----X||X-----XX|||XX-----X|| ----- ||X-----XX|||O ----- ||X-----X||O-----OOX\n" //
				+ "XXOo|o|.O||Xx|x|xX||Xx|o|xXX|||XX.|o|.X|| .|x|. ||Xx|.|xXX|||O x|.|x ||X.|x|.X||Oo|.|xOOX\n" //
				+ "XXOOOOOOO||XXXXXXX||XXXXXXXX|||XXXXXXXX||       ||XXXXXXXX|||O       ||XXXXXXX||OOOOOOOOX\n" //
				+ "XX-------------------------X|||X-------------------------X|||O-------------------------OX\n" //
				+ "XX-------------------------X|||X-------------------------X|||O-------------------------OX\n" //
				+ "XX       ||XXXXXXX||       X|||X       ||XXXXXXX||       X|||OXXXXXXX||OOOOOOO||OOOOOOOOX\n" //
				+ "XX .|o|o ||Xx|.|oX|| .|.|. X|||X .|o|. ||X.|o|xX|| o|x|o X|||OX.|x|.X||O.|.|.O||Oo|x|oOOX\n" //
				+ "XX ----- ||X-----X|| ----- X|||X ----- ||X-----X|| ----- X|||OX-----X||O-----O||O-----OOX\n" //
				+ "XX .|.|. ||Xx|x|xX|| o|x|. X|||X o|o|. ||Xo|x|.X|| .|o|. X|||OXx|x|.X||Oo|o|oO||O.|o|xOOX\n" //
				+ "XX ----- ||X-----X|| ----- X|||X ----- ||X-----X|| ----- X|||OX-----X||O-----O||O-----OOX\n" //
				+ "XX x|o|o ||Xo|o|.X|| .|.|o X|||X o|.|. ||Xx|x|.X|| .|x|x X|||OXo|x|xX||O.|x|oO||Oo|x|xOOX\n" //
				+ "XX       ||XXXXXXX||       X|||X       ||XXXXXXX||       X|||OXXXXXXX||OOOOOOO||OOOOOOOOX\n" //
				+ "XX-------------------------X|||X-------------------------X|||O-------------------------OX\n" //
				+ "XX-------------------------X|||X-------------------------X|||O-------------------------OX\n" //
				+ "XX???????||XXXXXXX||XXXXXXXX|||X       ||       ||XXXXXXXX|||OOOOOOOO||OOOOOOO||XXXXXXXOX\n" //
				+ "XX?o|x|x?||Xo|x|.X||Xx|x|oXX|||X .|.|x || x|.|x ||X.|.|xXX|||OOo|o|oO||Ox|o|xO||Xo|x|xXOX\n" //
				+ "XX?-----?||X-----X||X-----XX|||X ----- || ----- ||X-----XX|||OO-----O||O-----O||X-----XOX\n" //
				+ "XX?x|x|o?||Xo|.|xX||Xx|.|.XX|||X .|x|. || .|.|. ||X.|x|.XX|||OO.|x|.O||Oo|o|xO||Xx|x|oXOX\n" //
				+ "XX?-----?||X-----X||X-----XX|||X ----- || ----- ||X-----XX|||OO-----O||O-----O||X-----XOX\n" //
				+ "XX?o|o|x?||Xx|x|xX||Xx|.|.XX|||X o|x|. || o|.|. ||Xx|.|.XX|||OOo|x|oO||O.|o|oO||X.|x|oXOX\n" //
				+ "XX???????||XXXXXXX||XXXXXXXX|||X       ||       ||XXXXXXXX|||OOOOOOOO||OOOOOOO||XXXXXXXOX\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOOOOOOOOOOOOOOX\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOOOOOOOOOOOOOO|||                           X\n" //
				+ "XXXXXXXXX||OOOOOOO||XXXXXXXX|||OOOOOOOO||OOOOOOO||       O|||        ||       ||        X\n" //
				+ "XXXx|x|.X||O.|x|xO||Xo|.|oXX|||OOx|o|.O||Oo|.|oO|| x|.|. O|||  .|.|. || x|o|. || .|o|o  X\n" //
				+ "XXX-----X||O-----O||X-----XX|||OO-----O||O-----O|| ----- O|||  ----- || ----- || -----  X\n" //
				+ "XXXo|x|oX||Ox|o|xO||X.|.|.XX|||OOx|o|.O||O.|.|oO|| o|.|x O|||  .|o|o || .|.|x || o|x|o  X\n" //
				+ "XXX-----X||O-----O||X-----XX|||OO-----O||O-----O|| ----- O|||  ----- || ----- || -----  X\n" //
				+ "XXXo|x|oX||Oo|o|oO||Xx|x|xXX|||OOo|o|xO||O.|o|oO|| o|x|x O|||  .|x|o || x|.|. || .|.|.  X\n" //
				+ "XXXXXXXXX||OOOOOOO||XXXXXXXX|||OOOOOOOO||OOOOOOO||       O|||        ||       ||        X\n" //
				+ "XX-------------------------X|||O-------------------------O||| ------------------------- X\n" //
				+ "XX-------------------------X|||O-------------------------O||| ------------------------- X\n" //
				+ "XXXXXXXXX||OOOOOOO||       X|||O       ||OOOOOOO||       O|||        ||OOOOOOO||        X\n" //
				+ "XXXx|x|.X||Oo|.|.O|| o|o|x X|||O o|.|x ||Ox|x|oO|| .|o|. O|||  .|o|. ||Oo|x|oO|| o|x|o  X\n" //
				+ "XXX-----X||O-----O|| ----- X|||O ----- ||O-----O|| ----- O|||  ----- ||O-----O|| -----  X\n" //
				+ "XXXx|x|xX||Oo|.|oO|| o|.|o X|||O .|.|. ||Ox|o|.O|| .|o|x O|||  .|.|. ||Ox|o|.O|| .|.|x  X\n" //
				+ "XXX-----X||O-----O|| ----- X|||O ----- ||O-----O|| ----- O|||  ----- ||O-----O|| -----  X\n" //
				+ "XXX.|.|.X||Oo|.|.O|| x|o|x X|||O o|.|. ||Oo|x|oO|| o|x|o O|||  .|.|o ||Oo|.|xO|| .|x|x  X\n" //
				+ "XXXXXXXXX||OOOOOOO||       X|||O       ||OOOOOOO||       O|||        ||OOOOOOO||        X\n" //
				+ "XX-------------------------X|||O-------------------------O||| ------------------------- X\n" //
				+ "XX-------------------------X|||O-------------------------O||| ------------------------- X\n" //
				+ "XXXXXXXXX||       ||OOOOOOOX|||O       ||       ||OOOOOOOO|||        ||       ||XXXXXXX X\n" //
				+ "XXXo|.|xX|| .|.|. ||O.|.|.OX|||O o|x|x || .|x|o ||Oo|o|xOO|||  o|.|o || .|.|o ||X.|.|xX X\n" //
				+ "XXX-----X|| ----- ||O-----OX|||O ----- || ----- ||O-----OO|||  ----- || ----- ||X-----X X\n" //
				+ "XXXo|.|xX|| o|x|x ||Ox|o|.OX|||O o|o|. || .|o|x ||Ox|o|oOO|||  .|x|x || .|.|. ||Xo|x|.X X\n" //
				+ "XXX-----X|| ----- ||O-----OX|||O ----- || ----- ||O-----OO|||  ----- || ----- ||X-----X X\n" //
				+ "XXX.|x|xX|| o|x|o ||Oo|o|oOX|||O .|.|x || .|o|. ||Ox|o|xOO|||  x|.|x || x|.|. ||Xx|.|.X X\n" //
				+ "XXXXXXXXX||       ||OOOOOOOX|||O       ||       ||OOOOOOOO|||        ||       ||XXXXXXX X\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOOOOOOOOOOOOOO|||                           X\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOOOOOOOOOOOOOOX\n" //
				+ "XX       ||       ||XXXXXXXX|||X       ||XXXXXXX||XXXXXXXX|||OOOOOOOO||XXXXXXX||OOOOOOOOX\n" //
				+ "XX .|.|o || .|.|. ||X.|o|.XX|||X o|x|o ||Xo|o|xX||Xx|o|oXX|||OOo|.|.O||Xo|o|xX||Oo|x|oOOX\n" //
				+ "XX ----- || ----- ||X-----XX|||X ----- ||X-----X||X-----XX|||OO-----O||X-----X||O-----OOX\n" //
				+ "XX .|x|. || .|x|. ||Xx|x|xXX|||X .|x|. ||Xx|x|xX||Xx|.|oXX|||OO.|o|.O||X.|x|xX||Oo|o|oOOX\n" //
				+ "XX ----- || ----- ||X-----XX|||X ----- ||X-----X||X-----XX|||OO-----O||X-----X||O-----OOX\n" //
				+ "XX x|o|o || .|.|o ||X.|o|.XX|||X .|o|o ||X.|x|oX||Xx|x|.XX|||OO.|.|oO||Xo|x|xX||Ox|o|.OOX\n" //
				+ "XX       ||       ||XXXXXXXX|||X       ||XXXXXXX||XXXXXXXX|||OOOOOOOO||XXXXXXX||OOOOOOOOX\n" //
				+ "XX-------------------------X|||X-------------------------X|||O-------------------------OX\n" //
				+ "XX-------------------------X|||X-------------------------X|||O-------------------------OX\n" //
				+ "XX       ||       ||XXXXXXXX|||XXXXXXXX||XXXXXXX||OOOOOOOX|||OOOOOOOO||OOOOOOO||OOOOOOOOX\n" //
				+ "XX .|o|x || .|.|. ||Xx|o|.XX|||XX.|.|oX||Xx|x|oX||Oo|.|oOX|||OOx|x|oO||Ox|.|.O||Oo|x|xOOX\n" //
				+ "XX ----- || ----- ||X-----XX|||XX-----X||X-----X||O-----OX|||OO-----O||O-----O||O-----OOX\n" //
				+ "XX o|.|. || .|.|. ||Xx|.|.XX|||XXo|x|.X||Xo|x|xX||Ox|.|oOX|||OOo|o|xO||Ox|x|oO||Oo|o|.OOX\n" //
				+ "XX ----- || ----- ||X-----XX|||XX-----X||X-----X||O-----OX|||OO-----O||O-----O||O-----OOX\n" //
				+ "XX x|.|. || o|.|. ||Xx|x|.XX|||XXx|x|xX||Xo|x|oX||O.|.|oOX|||OOo|o|oO||Oo|o|oO||Ox|o|oOOX\n" //
				+ "XX       ||       ||XXXXXXXX|||XXXXXXXX||XXXXXXX||OOOOOOOX|||OOOOOOOO||OOOOOOO||OOOOOOOOX\n" //
				+ "XX-------------------------X|||X-------------------------X|||O-------------------------OX\n" //
				+ "XX-------------------------X|||X-------------------------X|||O-------------------------OX\n" //
				+ "XXOOOOOOO||       ||XXXXXXXX|||XXXXXXXX||OOOOOOO||       X|||OXXXXXXX||       ||XXXXXXXOX\n" //
				+ "XXO.|.|oO|| .|x|o ||Xo|x|oXX|||XXx|o|xX||O.|.|.O|| o|x|. X|||OX.|.|.X|| o|.|. ||Xx|x|oXOX\n" //
				+ "XXO-----O|| ----- ||X-----XX|||XX-----X||O-----O|| ----- X|||OX-----X|| ----- ||X-----XOX\n" //
				+ "XXO.|.|oO|| .|.|. ||Xx|.|xXX|||XXo|x|.X||O.|.|oO|| .|o|o X|||OX.|x|.X|| .|.|x ||Xo|x|.XOX\n" //
				+ "XXO-----O|| ----- ||X-----XX|||XX-----X||O-----O|| ----- X|||OX-----X|| ----- ||X-----XOX\n" //
				+ "XXO.|.|oO|| x|.|o ||Xx|x|xXX|||XXx|x|.X||Oo|o|oO|| x|o|x X|||OXx|x|xX|| o|.|. ||Xx|o|xXOX\n" //
				+ "XXOOOOOOO||       ||XXXXXXXX|||XXXXXXXX||OOOOOOO||       X|||OXXXXXXX||       ||XXXXXXXOX\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOOOOOOOOOOOOOOX\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h3s3_followConstraints() {

		final Board board = random_h3s3_followConstraintsBoard();
		final String expected = "" //
				+ "?????????????????????????????????????????????????????????????????????????????????????????\n" //
				+ "?XXXXXXXXXXXXXXXXXXXXXXXXXXX|||???????????????????????????|||XXXXXXXXXXXXXXXXXXXXXXXXXXX?\n" //
				+ "?XXXXXXXX||XXXXXXX||XXXXXXXX|||?OOOOOOO||XXXXXXX||XXXXXXX?|||XOOOOOOO||XXXXXXX||???????X?\n" //
				+ "?XX.|o|.X||Xx|x|xX||Xx|o|.XX|||?O.|x|oO||X.|o|.X||Xo|.|xX?|||XOx|o|xO||Xx|o|.X||?x|x|o?X?\n" //
				+ "?XX-----X||X-----X||X-----XX|||?O-----O||X-----X||X-----X?|||XO-----O||X-----X||?-----?X?\n" //
				+ "?XXx|x|xX||X.|.|oX||Xo|x|oXX|||?O.|o|xO||Xx|x|xX||Xx|.|xX?|||XOx|o|.O||Xx|o|oX||?o|x|x?X?\n" //
				+ "?XX-----X||X-----X||X-----XX|||?O-----O||X-----X||X-----X?|||XO-----O||X-----X||?-----?X?\n" //
				+ "?XX.|o|.X||Xx|x|oX||Xx|.|xXX|||?Oo|x|xO||Xx|x|oX||Xx|o|xX?|||XO.|o|oO||Xx|.|xX||?x|o|o?X?\n" //
				+ "?XXXXXXXX||XXXXXXX||XXXXXXXX|||?OOOOOOO||XXXXXXX||XXXXXXX?|||XOOOOOOO||XXXXXXX||???????X?\n" //
				+ "?X-------------------------X|||?-------------------------?|||X-------------------------X?\n" //
				+ "?X-------------------------X|||?-------------------------?|||X-------------------------X?\n" //
				+ "?XXXXXXXX||       ||OOOOOOOX|||????????||XXXXXXX||OOOOOOO?|||XXXXXXXX||OOOOOOO||XXXXXXXX?\n" //
				+ "?XX.|.|xX|| o|o|. ||O.|o|oOX|||??o|x|o?||Xx|o|xX||Ox|o|oO?|||XXx|x|oX||Oo|.|oO||Xo|.|oXX?\n" //
				+ "?XX-----X|| ----- ||O-----OX|||??-----?||X-----X||O-----O?|||XX-----X||O-----O||X-----XX?\n" //
				+ "?XXo|.|xX|| o|o|x ||O.|o|.OX|||??o|x|x?||Xx|.|oX||Ox|o|oO?|||XXo|x|xX||Oo|x|oO||Xx|x|xXX?\n" //
				+ "?XX-----X|| ----- ||O-----OX|||??-----?||X-----X||O-----O?|||XX-----X||O-----O||X-----XX?\n" //
				+ "?XX.|o|xX|| .|x|. ||Oo|.|oOX|||??x|o|o?||Xx|o|oX||Oo|x|xO?|||XX.|x|oX||Oo|x|xO||X.|o|oXX?\n" //
				+ "?XXXXXXXX||       ||OOOOOOOX|||????????||XXXXXXX||OOOOOOO?|||XXXXXXXX||OOOOOOO||XXXXXXXX?\n" //
				+ "?X-------------------------X|||?-------------------------?|||X-------------------------X?\n" //
				+ "?X-------------------------X|||?-------------------------?|||X-------------------------X?\n" //
				+ "?X       ||       ||       X|||?OOOOOOO||OOOOOOO||XXXXXXX?|||XXXXXXXX||XXXXXXX||XXXXXXXX?\n" //
				+ "?X o|x|o || .|.|. || x|x|. X|||?Ox|x|.O||Oo|o|oO||Xo|o|.X?|||XXx|o|xX||Xx|.|oX||X.|x|xXX?\n" //
				+ "?X ----- || ----- || ----- X|||?O-----O||O-----O||X-----X?|||XX-----X||X-----X||X-----XX?\n" //
				+ "?X .|x|o || .|x|x || x|.|x X|||?Oo|.|oO||Ox|.|xO||X.|.|oX?|||XXx|o|oX||Xo|x|xX||Xo|o|xXX?\n" //
				+ "?X ----- || ----- || ----- X|||?O-----O||O-----O||X-----X?|||XX-----X||X-----X||X-----XX?\n" //
				+ "?X .|.|. || x|o|o || o|.|o X|||?Oo|o|oO||Oo|x|.O||Xx|x|xX?|||XXx|x|oX||Xo|o|xX||Xo|o|xXX?\n" //
				+ "?X       ||       ||       X|||?OOOOOOO||OOOOOOO||XXXXXXX?|||XXXXXXXX||XXXXXXX||XXXXXXXX?\n" //
				+ "?XXXXXXXXXXXXXXXXXXXXXXXXXXX|||???????????????????????????|||XXXXXXXXXXXXXXXXXXXXXXXXXXX?\n" //
				+ "?---------------------------------------------------------------------------------------?\n" //
				+ "?---------------------------------------------------------------------------------------?\n" //
				+ "?---------------------------------------------------------------------------------------?\n" //
				+ "?OOOOOOOOOOOOOOOOOOOOOOOOOOO|||OOOOOOOOOOOOOOOOOOOOOOOOOOO|||????????????????????????????\n" //
				+ "?OOOOOOOO||XXXXXXX||XXXXXXXO|||O       ||       ||OOOOOOOO|||?XXXXXXX||OOOOOOO||XXXXXXX??\n" //
				+ "?OOx|o|xO||Xx|o|.X||Xx|x|oXO|||O .|o|x || x|.|x ||Ox|o|oOO|||?X.|x|oX||Oo|o|xO||X.|.|.X??\n" //
				+ "?OO-----O||X-----X||X-----XO|||O ----- || ----- ||O-----OO|||?X-----X||O-----O||X-----X??\n" //
				+ "?OOx|o|oO||X.|x|.X||Xx|o|xXO|||O o|.|o || x|o|o ||Oo|o|.OO|||?Xx|x|xX||Oo|x|xO||Xx|x|xX??\n" //
				+ "?OO-----O||X-----X||X-----XO|||O ----- || ----- ||O-----OO|||?X-----X||O-----O||X-----X??\n" //
				+ "?OO.|o|oO||X.|x|xX||Xx|o|oXO|||O x|o|x || o|.|o ||Oo|.|.OO|||?Xx|o|.X||Oo|x|oO||Xx|o|.X??\n" //
				+ "?OOOOOOOO||XXXXXXX||XXXXXXXO|||O       ||       ||OOOOOOOO|||?XXXXXXX||OOOOOOO||XXXXXXX??\n" //
				+ "?O-------------------------O|||O-------------------------O|||?-------------------------??\n" //
				+ "?O-------------------------O|||O-------------------------O|||?-------------------------??\n" //
				+ "?OOOOOOOO||OOOOOOO||       O|||O       ||XXXXXXX||OOOOOOOO|||????????||OOOOOOO||XXXXXXX??\n" //
				+ "?OOo|.|xO||O.|.|oO|| .|o|x O|||O o|o|x ||X.|x|xX||Ox|x|oOO|||??x|x|o?||Oo|o|oO||Xx|.|.X??\n" //
				+ "?OO-----O||O-----O|| ----- O|||O ----- ||X-----X||O-----OO|||??-----?||O-----O||X-----X??\n" //
				+ "?OO.|o|.O||O.|.|oO|| x|x|o O|||O o|.|x ||X.|.|xX||Ox|o|xOO|||??o|x|x?||Ox|o|oO||Xo|x|.X??\n" //
				+ "?OO-----O||O-----O|| ----- O|||O ----- ||X-----X||O-----OO|||??-----?||O-----O||X-----X??\n" //
				+ "?OO.|.|oO||O.|.|oO|| .|x|x O|||O x|x|. ||Xx|x|xX||Oo|x|oOO|||??x|o|o?||O.|x|xO||Xx|.|xX??\n" //
				+ "?OOOOOOOO||OOOOOOO||       O|||O       ||XXXXXXX||OOOOOOOO|||????????||OOOOOOO||XXXXXXX??\n" //
				+ "?O-------------------------O|||O-------------------------O|||?-------------------------??\n" //
				+ "?O-------------------------O|||O-------------------------O|||?-------------------------??\n" //
				+ "?OOOOOOOO||       ||XXXXXXXO|||O       ||XXXXXXX||OOOOOOOO|||?OOOOOOO||???????||OOOOOOO??\n" //
				+ "?OOo|o|.O|| .|o|o ||Xx|.|.XO|||O o|x|o ||Xo|.|xX||O.|x|xOO|||?Oo|.|oO||?o|x|o?||Oo|.|oO??\n" //
				+ "?OO-----O|| ----- ||X-----XO|||O ----- ||X-----X||O-----OO|||?O-----O||?-----?||O-----O??\n" //
				+ "?OO.|o|.O|| o|.|x ||Xx|.|.XO|||O x|o|o ||X.|o|xX||Oo|o|oOO|||?Oo|o|oO||?x|x|o?||O.|o|.O??\n" //
				+ "?OO-----O|| ----- ||X-----XO|||O ----- ||X-----X||O-----OO|||?O-----O||?-----?||O-----O??\n" //
				+ "?OO.|.|oO|| x|.|x ||Xx|.|.XO|||O x|x|. ||Xo|o|xX||O.|o|.OO|||?Ox|.|xO||?o|o|x?||Oo|.|xO??\n" //
				+ "?OOOOOOOO||       ||XXXXXXXO|||O       ||XXXXXXX||OOOOOOOO|||?OOOOOOO||???????||OOOOOOO??\n" //
				+ "?OOOOOOOOOOOOOOOOOOOOOOOOOOO|||OOOOOOOOOOOOOOOOOOOOOOOOOOO|||????????????????????????????\n" //
				+ "?---------------------------------------------------------------------------------------?\n" //
				+ "?---------------------------------------------------------------------------------------?\n" //
				+ "?---------------------------------------------------------------------------------------?\n" //
				+ "????????????????????????????|||OOOOOOOOOOOOOOOOOOOOOOOOOOO|||????????????????????????????\n" //
				+ "??XXXXXXX||XXXXXXX||OOOOOOO?|||OOOOOOOO||OOOOOOO||XXXXXXXO|||?OOOOOOO||OOOOOOO||?????????\n" //
				+ "??Xx|o|xX||X.|o|xX||Oo|o|oO?|||OO.|.|xO||O.|.|oO||Xo|x|oXO|||?O.|.|xO||Ox|x|oO||?x|o|x???\n" //
				+ "??X-----X||X-----X||O-----O?|||OO-----O||O-----O||X-----XO|||?O-----O||O-----O||?-----???\n" //
				+ "??Xx|x|xX||X.|.|xX||Oo|x|.O?|||OO.|x|oO||O.|o|oO||Xo|x|xXO|||?O.|.|.O||O.|o|.O||?x|o|x???\n" //
				+ "??X-----X||X-----X||O-----O?|||OO-----O||O-----O||X-----XO|||?O-----O||O-----O||?-----???\n" //
				+ "??X.|o|oX||Xx|.|xX||Oo|x|xO?|||OOo|o|oO||Oo|x|xO||Xx|x|oXO|||?Oo|o|oO||Oo|x|xO||?o|x|o???\n" //
				+ "??XXXXXXX||XXXXXXX||OOOOOOO?|||OOOOOOOO||OOOOOOO||XXXXXXXO|||?OOOOOOO||OOOOOOO||?????????\n" //
				+ "??-------------------------?|||O-------------------------O|||?-------------------------??\n" //
				+ "??-------------------------?|||O-------------------------O|||?-------------------------??\n" //
				+ "??OOOOOOO||OOOOOOO||XXXXXXX?|||OXXXXXXX||OOOOOOO||OOOOOOOO|||?XXXXXXX||XXXXXXX||OOOOOOO??\n" //
				+ "??Ox|.|oO||O.|x|oO||X.|o|xX?|||OXo|.|xX||Ox|x|oO||Ox|o|oOO|||?X.|x|.X||Xx|.|xX||Oo|o|.O??\n" //
				+ "??O-----O||O-----O||X-----X?|||OX-----X||O-----O||O-----OO|||?X-----X||X-----X||O-----O??\n" //
				+ "??Ox|o|.O||Ox|o|xO||Xo|x|.X?|||OX.|x|xX||Oo|o|xO||O.|o|xOO|||?X.|x|.X||Xo|x|.X||Oo|x|oO??\n" //
				+ "??O-----O||O-----O||X-----X?|||OX-----X||O-----O||O-----OO|||?X-----X||X-----X||O-----O??\n" //
				+ "??Oo|.|.O||Oo|o|.O||Xx|x|oX?|||OXx|.|.X||Oo|x|xO||O.|o|.OO|||?X.|x|.X||Xo|.|xX||Oo|.|xO??\n" //
				+ "??OOOOOOO||OOOOOOO||XXXXXXX?|||OXXXXXXX||OOOOOOO||OOOOOOOO|||?XXXXXXX||XXXXXXX||OOOOOOO??\n" //
				+ "??-------------------------?|||O-------------------------O|||?-------------------------??\n" //
				+ "??-------------------------?|||O-------------------------O|||?-------------------------??\n" //
				+ "??XXXXXXX||OOOOOOO||OOOOOOO?|||OXXXXXXX||XXXXXXX||OOOOOOOO|||?XXXXXXX||OOOOOOO||OOOOOOO??\n" //
				+ "??Xx|o|.X||Oo|x|xO||Oo|o|xO?|||OX.|o|xX||Xx|x|oX||Oo|.|xOO|||?X.|x|oX||Oo|o|oO||Oo|o|oO??\n" //
				+ "??X-----X||O-----O||O-----O?|||OX-----X||X-----X||O-----OO|||?X-----X||O-----O||O-----O??\n" //
				+ "??X.|o|.X||Oo|.|oO||Ox|o|.O?|||OXx|x|xX||Xo|x|oX||Ox|o|oOO|||?Xo|x|xX||Oo|o|xO||O.|o|xO??\n" //
				+ "??X-----X||O-----O||O-----O?|||OX-----X||X-----X||O-----OO|||?X-----X||O-----O||O-----O??\n" //
				+ "??Xx|x|xX||Oo|.|oO||Oo|x|oO?|||OXo|x|oX||Xo|o|xX||Ox|x|oOO|||?Xx|x|xX||O.|.|xO||Oo|x|.O??\n" //
				+ "??XXXXXXX||OOOOOOO||OOOOOOO?|||OXXXXXXX||XXXXXXX||OOOOOOOO|||?XXXXXXX||OOOOOOO||OOOOOOO??\n" //
				+ "????????????????????????????|||OOOOOOOOOOOOOOOOOOOOOOOOOOO|||????????????????????????????\n" //
				+ "?????????????????????????????????????????????????????????????????????????????????????????\n" //
				+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	public static Board random_h1s3Board() {
		final Logger log = StackFrameUtil.methodLogger();
		log.trace("-------------------------------------------------------------");

		Board  board     = new Board(1,3);
		Player playerAAA = PlayerPredictable.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerPredictable.create(Token.PLAYER_BBB);

		Player player = playerAAA;
		while (board.isPlayable()) {
			Move     move       = player.makeMove(log, board, null);
			Position position   = move.toPosition(board);
			Position constraint = position.place(player.getToken());

			assertNull(constraint);

			player = (player == playerAAA) ? playerBBB : playerAAA;

			log.trace("after placing:\n" + board.fieldAsPrintableString());
		}

		return board;
	}

	public static Board random_h2s3_ignoreConstraintsBoard() {
		final Logger log = StackFrameUtil.methodLogger();
		log.trace("-------------------------------------------------------------");

		Board  board     = new Board(2,3); // 81 token positions
		Player playerAAA = PlayerPredictable.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerPredictable.create(Token.PLAYER_BBB);

		Player player = playerAAA;
		for (int i = 0; board.isPlayable() && (i < 100); ++i) {
			log.trace("#"+i);

			Move     move       = player.makeMove(log, board, null);
			log.trace(move.toString() + " by " + player.getToken());

			Position position   = move.toPosition(board);
			Position constraint = position.place(player.getToken());
			log.trace("Next move constraint (ignored): " + constraint);

			log.trace("after placing:\n" + board.fieldAsPrintableString());

			player = (player == playerAAA) ? playerBBB : playerAAA;
		}

		return board;
	}

	public static Board random_h2s3_followConstraintsBoard() {

		final Logger log = StackFrameUtil.methodLogger();
		log.trace("-------------------------------------------------------------");

		Board  board     = new Board(2,3); // 81 token positions
		Player playerAAA = PlayerPredictable.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerPredictable.create(Token.PLAYER_BBB);

		Player player = playerAAA;
		Position constraint = null;
		for (int i = 0; board.isPlayable() && (i < 100); ++i) {
			log.trace("#"+i +": constraint=[" + constraint + "]");

			Move     move       = player.makeMove(log, board, constraint);
			Position position   = move.toPosition(board);
			log.trace(move.toString() + " by " + player.getToken());

			constraint = position.place(player.getToken());
			log.trace("Next move constraint: " + constraint);

			log.trace("after placing:\n" + board.fieldAsPrintableString());

			player = (player == playerAAA) ? playerBBB : playerAAA;
		}

		return board;
	}

	public static Board random_h3s2_ignoreConstraintsBoard() {

		final Logger log = StackFrameUtil.methodLogger();
		log.trace("-------------------------------------------------------------");

		Board  board     = new Board(3,2); // 64 token positions
		Player playerAAA = PlayerPredictable.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerPredictable.create(Token.PLAYER_BBB);

		Player player = playerAAA;
		for (int i = 0; board.isPlayable() && (i < 70); ++i) {
			log.trace("#" + i);

			Move     move       = player.makeMove(log, board, null);
			log.trace(move.toString() + " by " + player.getToken());

			Position position   = move.toPosition(board);
			Position constraint = position.place(player.getToken());

			log.trace("Next move constraint (ignored): " + constraint);
			log.trace("after placing:\n" + board.fieldAsPrintableString());

			player = (player == playerAAA) ? playerBBB : playerAAA;
		}

		return board;
	}

	public static Board random_h3s2_followConstraintsBoard() {

		final Logger log = StackFrameUtil.methodLogger();
		log.trace("-------------------------------------------------------------");

		Board  board     = new Board(3,2); // 64 token positions
		Player playerAAA = PlayerPredictable.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerPredictable.create(Token.PLAYER_BBB);

		Player player = playerAAA;
		Position constraint = null;
		for (int i = 0; board.isPlayable() && (i < 80); ++i) {
			log.trace("#" + i);

			Move     move       = player.makeMove(log, board, constraint);
			Position position   = move.toPosition(board);

			log.trace(move.toString() + " by " + player.getToken());

			constraint = position.place(player.getToken());

			log.trace("Next move constraint: " + constraint);
			log.trace("after placing:\n" + board.fieldAsPrintableString());

			player = (player == playerAAA) ? playerBBB : playerAAA;
		}

		return board;
	}

	public static Board random_h3s3_ignoreConstraintsBoard() {

		final Logger log = StackFrameUtil.methodLogger();
		log.trace("-------------------------------------------------------------");

		Board  board     = new Board(3,3); // 729 token locations
		Player playerAAA = PlayerPredictable.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerPredictable.create(Token.PLAYER_BBB);

		Player player = playerAAA;
		for (int i = 0; board.isPlayable() && (i < 800); ++i) {
			log.trace("#" + i);

			Move     move       = player.makeMove(log, board, null);
			log.trace(move.toString() + " by " + player.getToken());

			Position position   = move.toPosition(board);
			Position constraint = position.place(player.getToken());

			log.trace("Next move constraint (ignored): " + constraint);
			log.trace(board.fieldAsPrintableString());

			player = (player == playerAAA) ? playerBBB : playerAAA;
		}

		return board;
	}

	public static Board random_h3s3_followConstraintsBoard() {

		final Logger log = StackFrameUtil.methodLogger();
		log.trace("-------------------------------------------------------------");

		Board  board     = new Board(3,3); // 729 token locations
		Player playerAAA = PlayerPredictable.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerPredictable.create(Token.PLAYER_BBB);

		Player   player     = playerAAA;
		Position constraint = null;
		int      depth     = 0;
		for (int i = 0; board.isPlayable() && (i < 800); ++i) {
			depth = (constraint == null) ? 0 : constraint.depth();
			log.trace("#" + i);
			log.trace("Current constraint: [" + constraint + "]; depth=["+depth+"]");

			Move     move       = player.makeMove(log, board, constraint);
			Position position   = move.toPosition(board);

			log.trace(move.toString() + " by " + player.getToken());

			constraint = position.place(player.getToken());

			log.trace("Next move constraint: [" + constraint + "]");
			log.trace(board.fieldAsPrintableString());

			player = (player == playerAAA) ? playerBBB : playerAAA;
		}

		return board;
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		final Method[] methods = PlayerTest.class.getMethods();
		for (Method method : methods) {
			if (method.getReturnType().equals(Board.class)) {
			    final Object o = method.invoke(null);
			    final String[] fieldString = ((Board) o).fieldAsPrintableString().split("\n");
			    System.out.println("for " + method.getName());
			    System.out.println("final String expected = \"\" //");
			    for (String line : fieldString) {
			    	System.out.println(String.format("\t+ \"%s\\n\" //", line));
			    }
			    System.out.println(";");
			    System.out.println();
			}
		}
	}
}
