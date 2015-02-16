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
				+ "XXXXXXX\n" //
				+ "Xx|o|oX\n" //
				+ "X-----X\n" //
				+ "X.|x|oX\n" //
				+ "X-----X\n" //
				+ "X.|x|xX\n" //
				+ "XXXXXXX\n" //
				+ "TOP.\n" //
		;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h2s3_ignoreConstraints() {

		final Board board = random_h2s3_ignoreConstraintsBoard();
		final String expected = "" //
				+ "???????????????????????????\n" //
				+ "?XXXXXXX||OOOOOOO||OOOOOOO?\n" //
				+ "?Xo|x|oX||Oo|o|xO||O.|o|.O?\n" //
				+ "?X-----X||O-----O||O-----O?\n" //
				+ "?Xx|.|oX||Ox|o|xO||Oo|o|oO?\n" //
				+ "?X-----X||O-----O||O-----O?\n" //
				+ "?Xx|x|xX||O.|x|oO||Oo|x|.O?\n" //
				+ "?XXXXXXX||OOOOOOO||OOOOOOO?\n" //
				+ "?-------------------------?\n" //
				+ "?-------------------------?\n" //
				+ "?XXXXXXX||XXXXXXX||????????\n" //
				+ "?Xx|x|xX||Xx|x|xX||?o|x|o??\n" //
				+ "?X-----X||X-----X||?-----??\n" //
				+ "?X.|o|oX||Xx|o|xX||?x|o|o??\n" //
				+ "?X-----X||X-----X||?-----??\n" //
				+ "?Xo|o|xX||Xo|x|oX||?x|o|x??\n" //
				+ "?XXXXXXX||XXXXXXX||????????\n" //
				+ "?-------------------------?\n" //
				+ "?-------------------------?\n" //
				+ "?OOOOOOO||XXXXXXX||OOOOOOO?\n" //
				+ "?O.|.|xO||Xo|x|xX||Oo|x|.O?\n" //
				+ "?O-----O||X-----X||O-----O?\n" //
				+ "?Oo|o|oO||Xx|x|xX||Ox|o|xO?\n" //
				+ "?O-----O||X-----X||O-----O?\n" //
				+ "?Ox|o|.O||Xo|.|.X||Ox|o|oO?\n" //
				+ "?OOOOOOO||XXXXXXX||OOOOOOO?\n" //
				+ "???????????????????????????\n" //
				+ "TOP.\n" //
		;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h2s3_followConstraints() {

		final Board board = random_h2s3_followConstraintsBoard();
		final String expected = "" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "XOOOOOOO||       ||OOOOOOOX\n" //
				+ "XOo|.|.O|| .|.|. ||O.|.|xOX\n" //
				+ "XO-----O|| ----- ||O-----OX\n" //
				+ "XOo|.|.O|| x|.|. ||Oo|o|oOX\n" //
				+ "XO-----O|| ----- ||O-----OX\n" //
				+ "XOo|.|oO|| .|o|o ||O.|.|oOX\n" //
				+ "XOOOOOOO||       ||OOOOOOOX\n" //
				+ "X-------------------------X\n" //
				+ "X-------------------------X\n" //
				+ "XXXXXXXX||XXXXXXX||XXXXXXXX\n" //
				+ "XXx|.|.X||Xx|x|xX||Xx|.|.XX\n" //
				+ "XX-----X||X-----X||X-----XX\n" //
				+ "XXx|x|xX||Xo|.|oX||X.|.|.XX\n" //
				+ "XX-----X||X-----X||X-----XX\n" //
				+ "XXo|.|.X||X.|o|.X||Xx|x|xXX\n" //
				+ "XXXXXXXX||XXXXXXX||XXXXXXXX\n" //
				+ "X-------------------------X\n" //
				+ "X-------------------------X\n" //
				+ "X       ||       ||XXXXXXXX\n" //
				+ "X .|x|. || x|x|o ||X.|.|xXX\n" //
				+ "X ----- || ----- ||X-----XX\n" //
				+ "X .|o|o || .|x|o ||Xo|x|oXX\n" //
				+ "X ----- || ----- ||X-----XX\n" //
				+ "X .|x|o || .|.|. ||Xx|.|xXX\n" //
				+ "X       ||       ||XXXXXXXX\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "TOP.\n" //
		;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h3s2_ignoreConstraints() {

		final Board board = random_h3s2_ignoreConstraintsBoard();
		final String expected = "" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "XOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXX\n" //
				+ "XO     ||     O|||X     ||XXXXXXX\n" //
				+ "XO .|. || o|. O|||X .|. ||Xx|xXXX\n" //
				+ "XO --- || --- O|||X --- ||X---XXX\n" //
				+ "XO .|x || x|. O|||X .|. ||X.|oXXX\n" //
				+ "XO     ||     O|||X     ||XXXXXXX\n" //
				+ "XO------------O|||X------------XX\n" //
				+ "XO------------O|||X------------XX\n" //
				+ "XOOOOOO||OOOOOO|||XXXXXX||     XX\n" //
				+ "XOO.|.O||O.|.OO|||XXx|oX|| .|. XX\n" //
				+ "XOO---O||O---OO|||XX---X|| --- XX\n" //
				+ "XOOo|oO||Oo|oOO|||XXx|.X|| .|. XX\n" //
				+ "XOOOOOO||OOOOOO|||XXXXXX||     XX\n" //
				+ "XOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXX\n" //
				+ "X-------------------------------X\n" //
				+ "X-------------------------------X\n" //
				+ "X-------------------------------X\n" //
				+ "XXXXXXXXXXXXXXX|||              X\n" //
				+ "XXXXXXX||OOOOOX|||      ||OOOOO X\n" //
				+ "XXXx|xX||Oo|oOX|||  .|. ||Oo|oO X\n" //
				+ "XXX---X||O---OX|||  --- ||O---O X\n" //
				+ "XXX.|.X||O.|.OX|||  x|. ||O.|xO X\n" //
				+ "XXXXXXX||OOOOOX|||      ||OOOOO X\n" //
				+ "XX------------X||| ------------ X\n" //
				+ "XX------------X||| ------------ X\n" //
				+ "XX     ||XXXXXX|||      ||      X\n" //
				+ "XX .|o ||X.|xXX|||  o|. || .|.  X\n" //
				+ "XX --- ||X---XX|||  --- || ---  X\n" //
				+ "XX x|. ||X.|xXX|||  .|. || .|x  X\n" //
				+ "XX     ||XXXXXX|||      ||      X\n" //
				+ "XXXXXXXXXXXXXXX|||              X\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "TOP.\n" //
		;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h3s2_followConstraints() {

		final Board board = random_h3s2_followConstraintsBoard();
		final String expected = "" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "O              |||OOOOOOOOOOOOOOO\n" //
				+ "O      ||      |||OXXXXX||     OO\n" //
				+ "O  .|. || .|.  |||OX.|xX|| o|. OO\n" //
				+ "O  --- || ---  |||OX---X|| --- OO\n" //
				+ "O  .|. || .|.  |||OX.|xX|| x|. OO\n" //
				+ "O      ||      |||OXXXXX||     OO\n" //
				+ "O ------------ |||O------------OO\n" //
				+ "O ------------ |||O------------OO\n" //
				+ "O      ||      |||OOOOOO||OOOOOOO\n" //
				+ "O  .|. || .|.  |||OO.|oO||Oo|.OOO\n" //
				+ "O  --- || ---  |||OO---O||O---OOO\n" //
				+ "O  .|. || .|.  |||OOx|oO||O.|oOOO\n" //
				+ "O      ||      |||OOOOOO||OOOOOOO\n" //
				+ "O              |||OOOOOOOOOOOOOOO\n" //
				+ "O-------------------------------O\n" //
				+ "O-------------------------------O\n" //
				+ "O-------------------------------O\n" //
				+ "OOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXO\n" //
				+ "OOOOOOO||OOOOOO|||X     ||OOOOOXO\n" //
				+ "OOOx|oO||Ox|oOO|||X .|x ||O.|oOXO\n" //
				+ "OOO---O||O---OO|||X --- ||O---OXO\n" //
				+ "OOO.|oO||Oo|.OO|||X o|. ||O.|oOXO\n" //
				+ "OOOOOOO||OOOOOO|||X     ||OOOOOXO\n" //
				+ "OO------------O|||X------------XO\n" //
				+ "OO------------O|||X------------XO\n" //
				+ "OOXXXXX||     O|||XXXXXX||XXXXXXO\n" //
				+ "OOX.|xX|| o|x O|||XX.|.X||X.|xXXO\n" //
				+ "OOX---X|| --- O|||XX---X||X---XXO\n" //
				+ "OOX.|xX|| .|. O|||XXx|xX||Xo|xXXO\n" //
				+ "OOXXXXX||     O|||XXXXXX||XXXXXXO\n" //
				+ "OOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXO\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "TOP.\n" //
		;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h3s3_ignoreConstraints() {

		final Board board = random_h3s3_ignoreConstraintsBoard();
		final String expected = "" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "XOOOOOOOOOOOOOOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "XOOOOOOOO||       ||       O|||XOOOOOOO||OOOOOOO||       X|||XXXXXXXX||XXXXXXX||XXXXXXXXX\n" //
				+ "XOOo|x|.O|| x|o|o || o|x|x O|||XO.|o|oO||Oo|o|oO|| .|.|o X|||XXx|.|.X||Xo|x|oX||Xx|o|.XXX\n" //
				+ "XOO-----O|| ----- || ----- O|||XO-----O||O-----O|| ----- X|||XX-----X||X-----X||X-----XXX\n" //
				+ "XOOx|o|.O|| x|.|o || .|.|o O|||XOx|x|oO||Oo|x|.O|| x|o|o X|||XXx|o|.X||Xo|x|oX||X.|x|oXXX\n" //
				+ "XOO-----O|| ----- || ----- O|||XO-----O||O-----O|| ----- X|||XX-----X||X-----X||X-----XXX\n" //
				+ "XOOx|.|oO|| .|x|. || .|o|o O|||XOo|.|oO||Ox|x|.O|| x|.|x X|||XXx|x|.X||Xx|x|.X||Xo|.|xXXX\n" //
				+ "XOOOOOOOO||       ||       O|||XOOOOOOO||OOOOOOO||       X|||XXXXXXXX||XXXXXXX||XXXXXXXXX\n" //
				+ "XO-------------------------O|||X-------------------------X|||X-------------------------XX\n" //
				+ "XO-------------------------O|||X-------------------------X|||X-------------------------XX\n" //
				+ "XOOOOOOOO||XXXXXXX||       O|||X       ||OOOOOOO||       X|||XXXXXXXX||       ||OOOOOOOXX\n" //
				+ "XOO.|o|.O||X.|.|xX|| x|x|. O|||X .|.|. ||Oo|.|.O|| o|x|. X|||XX.|o|.X|| .|.|. ||O.|.|oOXX\n" //
				+ "XOO-----O||X-----X|| ----- O|||X ----- ||O-----O|| ----- X|||XX-----X|| ----- ||O-----OXX\n" //
				+ "XOOo|o|oO||X.|.|xX|| o|.|o O|||X .|.|. ||Oo|.|xO|| x|o|x X|||XXx|x|xX|| o|o|x ||Oo|o|oOXX\n" //
				+ "XOO-----O||X-----X|| ----- O|||X ----- ||O-----O|| ----- X|||XX-----X|| ----- ||O-----OXX\n" //
				+ "XOO.|x|oO||X.|.|xX|| x|o|. O|||X o|.|. ||Oo|x|xO|| .|x|. X|||XX.|x|xX|| x|x|. ||O.|o|.OXX\n" //
				+ "XOOOOOOOO||XXXXXXX||       O|||X       ||OOOOOOO||       X|||XXXXXXXX||       ||OOOOOOOXX\n" //
				+ "XO-------------------------O|||X-------------------------X|||X-------------------------XX\n" //
				+ "XO-------------------------O|||X-------------------------X|||X-------------------------XX\n" //
				+ "XOOOOOOOO||XXXXXXX||       O|||XXXXXXXX||XXXXXXX||XXXXXXXX|||X       ||       ||       XX\n" //
				+ "XOO.|.|xO||Xx|.|.X|| x|.|x O|||XXx|.|xX||Xo|.|xX||X.|x|.XX|||X x|o|o || .|.|. || x|x|o XX\n" //
				+ "XOO-----O||X-----X|| ----- O|||XX-----X||X-----X||X-----XX|||X ----- || ----- || ----- XX\n" //
				+ "XOO.|o|xO||X.|x|oX|| o|x|. O|||XXx|x|xX||Xo|.|xX||Xx|.|xXX|||X x|.|o || x|o|o || .|.|o XX\n" //
				+ "XOO-----O||X-----X|| ----- O|||XX-----X||X-----X||X-----XX|||X ----- || ----- || ----- XX\n" //
				+ "XOOo|o|oO||Xx|.|xX|| o|.|. O|||XXo|.|.X||X.|.|xX||Xx|x|xXX|||X .|.|. || o|.|. || o|x|x XX\n" //
				+ "XOOOOOOOO||XXXXXXX||       O|||XXXXXXXX||XXXXXXX||XXXXXXXX|||X       ||       ||       XX\n" //
				+ "XOOOOOOOOOOOOOOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "XOOOOOOOOOOOOOOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOOOOOOOOOOOOOOX\n" //
				+ "XO       ||XXXXXXX||OOOOOOOO|||XXXXXXXX||XXXXXXX||XXXXXXXX|||OOOOOOOO||XXXXXXX||OOOOOOOOX\n" //
				+ "XO .|x|x ||Xo|x|xX||Oo|o|.OO|||XX.|o|.X||Xo|.|xX||X.|.|xXX|||OOx|o|xO||Xx|x|xX||Ox|.|.OOX\n" //
				+ "XO ----- ||X-----X||O-----OO|||XX-----X||X-----X||X-----XX|||OO-----O||X-----X||O-----OOX\n" //
				+ "XO .|o|. ||Xo|x|.X||O.|o|oOO|||XXx|x|xX||Xx|x|.X||Xx|.|xXX|||OOo|o|oO||X.|o|.X||Oo|o|oOOX\n" //
				+ "XO ----- ||X-----X||O-----OO|||XX-----X||X-----X||X-----XX|||OO-----O||X-----X||O-----OOX\n" //
				+ "XO x|.|o ||Xx|.|xX||Oo|o|xOO|||XX.|x|xX||Xx|.|oX||Xo|o|xXX|||OOx|.|.O||X.|.|oX||O.|.|xOOX\n" //
				+ "XO       ||XXXXXXX||OOOOOOOO|||XXXXXXXX||XXXXXXX||XXXXXXXX|||OOOOOOOO||XXXXXXX||OOOOOOOOX\n" //
				+ "XO-------------------------O|||X-------------------------X|||O-------------------------OX\n" //
				+ "XO-------------------------O|||X-------------------------X|||O-------------------------OX\n" //
				+ "XO       ||OOOOOOO||OOOOOOOO|||XOOOOOOO||OOOOOOO||       X|||OOOOOOOO||       ||       OX\n" //
				+ "XO .|o|x ||O.|.|oO||O.|x|oOO|||XOo|x|oO||O.|.|.O|| .|.|o X|||OO.|.|oO|| o|.|x || .|.|. OX\n" //
				+ "XO ----- ||O-----O||O-----OO|||XO-----O||O-----O|| ----- X|||OO-----O|| ----- || ----- OX\n" //
				+ "XO o|.|o ||Ox|o|.O||O.|.|oOO|||XOo|x|xO||Oo|o|oO|| x|x|. X|||OOo|o|oO|| .|.|x || .|.|x OX\n" //
				+ "XO ----- ||O-----O||O-----OO|||XO-----O||O-----O|| ----- X|||OO-----O|| ----- || ----- OX\n" //
				+ "XO x|o|. ||Oo|x|xO||Ox|.|oOO|||XOo|o|xO||Ox|x|.O|| .|o|x X|||OOo|x|xO|| o|.|. || .|x|. OX\n" //
				+ "XO       ||OOOOOOO||OOOOOOOO|||XOOOOOOO||OOOOOOO||       X|||OOOOOOOO||       ||       OX\n" //
				+ "XO-------------------------O|||X-------------------------X|||O-------------------------OX\n" //
				+ "XO-------------------------O|||X-------------------------X|||O-------------------------OX\n" //
				+ "XOOOOOOOO||OOOOOOO||XXXXXXXO|||XOOOOOOO||XXXXXXX||OOOOOOOX|||OOOOOOOO||OOOOOOO||XXXXXXXOX\n" //
				+ "XOOo|o|oO||Oo|o|oO||Xx|.|xXO|||XOo|.|.O||X.|.|xX||Ox|.|oOX|||OO.|o|oO||Oo|.|.O||Xx|x|xXOX\n" //
				+ "XOO-----O||O-----O||X-----XO|||XO-----O||X-----X||O-----OX|||OO-----O||O-----O||X-----XOX\n" //
				+ "XOOx|x|.O||Oo|.|xO||Xo|.|xXO|||XOo|.|.O||Xo|x|xX||Ox|o|oOX|||OO.|o|.O||O.|o|.O||Xo|x|oXOX\n" //
				+ "XOO-----O||O-----O||X-----XO|||XO-----O||X-----X||O-----OX|||OO-----O||O-----O||X-----XOX\n" //
				+ "XOOx|x|.O||O.|x|oO||X.|x|xXO|||XOo|x|.O||X.|x|xX||Oo|o|.OX|||OOx|o|.O||O.|x|oO||Xo|o|xXOX\n" //
				+ "XOOOOOOOO||OOOOOOO||XXXXXXXO|||XOOOOOOO||XXXXXXX||OOOOOOOX|||OOOOOOOO||OOOOOOO||XXXXXXXOX\n" //
				+ "XOOOOOOOOOOOOOOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOOOOOOOOOOOOOOX\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "X                           |||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||???????????????????????????X\n" //
				+ "X        ||OOOOOOO||OOOOOOO |||X???????||XXXXXXX||XXXXXXXX|||?OOOOOOO||OOOOOOO||XXXXXXX?X\n" //
				+ "X  o|.|x ||Oo|o|xO||Oo|x|xO |||X?o|x|o?||Xx|x|oX||X.|x|oXX|||?Oo|.|oO||Ox|o|xO||Xo|o|xX?X\n" //
				+ "X  ----- ||O-----O||O-----O |||X?-----?||X-----X||X-----XX|||?O-----O||O-----O||X-----X?X\n" //
				+ "X  x|.|x ||O.|.|xO||Oo|.|oO |||X?x|o|x?||Xx|o|oX||Xo|x|.XX|||?Ox|o|.O||Ox|.|xO||Xx|o|oX?X\n" //
				+ "X  ----- ||O-----O||O-----O |||X?-----?||X-----X||X-----XX|||?O-----O||O-----O||X-----X?X\n" //
				+ "X  .|x|. ||Oo|o|oO||Oo|o|xO |||X?x|o|x?||Xx|o|xX||X.|x|oXX|||?Oo|.|.O||Oo|o|oO||Xx|x|xX?X\n" //
				+ "X        ||OOOOOOO||OOOOOOO |||X???????||XXXXXXX||XXXXXXXX|||?OOOOOOO||OOOOOOO||XXXXXXX?X\n" //
				+ "X ------------------------- |||X-------------------------X|||?-------------------------?X\n" //
				+ "X ------------------------- |||X-------------------------X|||?-------------------------?X\n" //
				+ "X OOOOOOO||       ||        |||X       ||XXXXXXX||       X|||?XXXXXXX||XXXXXXX||????????X\n" //
				+ "X Oo|x|oO|| .|.|o || o|o|.  |||X .|.|. ||Xx|x|xX|| o|o|x X|||?Xo|x|oX||Xx|.|.X||?x|o|x??X\n" //
				+ "X O-----O|| ----- || -----  |||X ----- ||X-----X|| ----- X|||?X-----X||X-----X||?-----??X\n" //
				+ "X Oo|o|oO|| o|x|. || x|x|o  |||X .|.|x ||Xx|.|.X|| o|x|. X|||?Xx|x|xX||Xx|.|.X||?x|o|o??X\n" //
				+ "X O-----O|| ----- || -----  |||X ----- ||X-----X|| ----- X|||?X-----X||X-----X||?-----??X\n" //
				+ "X Ox|o|xO|| .|o|o || x|o|x  |||X o|o|. ||Xo|o|xX|| .|x|o X|||?Xo|x|oX||Xx|x|xX||?o|x|o??X\n" //
				+ "X OOOOOOO||       ||        |||X       ||XXXXXXX||       X|||?XXXXXXX||XXXXXXX||????????X\n" //
				+ "X ------------------------- |||X-------------------------X|||?-------------------------?X\n" //
				+ "X ------------------------- |||X-------------------------X|||?-------------------------?X\n" //
				+ "X OOOOOOO||       ||        |||XOOOOOOO||XXXXXXX||XXXXXXXX|||?OOOOOOO||XXXXXXX||XXXXXXX?X\n" //
				+ "X O.|.|.O|| x|x|o || .|x|x  |||XOo|.|.O||Xx|o|xX||Xx|o|oXX|||?Oo|.|.O||Xx|o|oX||Xx|x|xX?X\n" //
				+ "X O-----O|| ----- || -----  |||XO-----O||X-----X||X-----XX|||?O-----O||X-----X||X-----X?X\n" //
				+ "X Oo|o|oO|| x|x|. || .|o|o  |||XOo|x|oO||Xo|o|xX||Xo|x|xXX|||?O.|o|xO||Xx|x|.X||Xo|.|oX?X\n" //
				+ "X O-----O|| ----- || -----  |||XO-----O||X-----X||X-----XX|||?O-----O||X-----X||X-----X?X\n" //
				+ "X O.|o|.O|| .|o|. || o|x|o  |||XOo|.|.O||Xx|.|xX||Xo|.|xXX|||?Oo|o|oO||X.|.|xX||X.|x|xX?X\n" //
				+ "X OOOOOOO||       ||        |||XOOOOOOO||XXXXXXX||XXXXXXXX|||?OOOOOOO||XXXXXXX||XXXXXXX?X\n" //
				+ "X                           |||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||???????????????????????????X\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "TOP.\n" //
		;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h3s3_followConstraints() {

		final Board board = random_h3s3_followConstraintsBoard();
		final String expected = "" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "OXXXXXXXXXXXXXXXXXXXXXXXXXXX|||                           |||OOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "OX       ||XXXXXXX||       X|||        ||       ||        |||OOOOOOOO||OOOOOOO||XXXXXXXOO\n" //
				+ "OX o|.|. ||X.|o|.X|| .|o|o X|||  .|.|. || .|.|. || .|.|.  |||OOo|x|.O||Ox|o|oO||X.|x|.XOO\n" //
				+ "OX ----- ||X-----X|| ----- X|||  ----- || ----- || -----  |||OO-----O||O-----O||X-----XOO\n" //
				+ "OX o|o|. ||Xx|x|.X|| .|.|. X|||  .|.|. || .|.|. || .|.|.  |||OO.|o|oO||Oo|o|oO||Xx|x|xXOO\n" //
				+ "OX ----- ||X-----X|| ----- X|||  ----- || ----- || -----  |||OO-----O||O-----O||X-----XOO\n" //
				+ "OX x|o|x ||Xx|x|xX|| .|.|x X|||  .|.|. || .|.|. || .|.|.  |||OOx|x|oO||O.|x|xO||X.|.|.XOO\n" //
				+ "OX       ||XXXXXXX||       X|||        ||       ||        |||OOOOOOOO||OOOOOOO||XXXXXXXOO\n" //
				+ "OX-------------------------X||| ------------------------- |||O-------------------------OO\n" //
				+ "OX-------------------------X||| ------------------------- |||O-------------------------OO\n" //
				+ "OX       ||XXXXXXX||       X|||        ||       ||        |||OXXXXXXX||OOOOOOO||XXXXXXXOO\n" //
				+ "OX x|x|. ||Xx|.|xX|| .|.|. X|||  .|.|. || .|.|. || .|.|.  |||OXo|x|xX||Ox|x|oO||Xo|.|.XOO\n" //
				+ "OX ----- ||X-----X|| ----- X|||  ----- || ----- || -----  |||OX-----X||O-----O||X-----XOO\n" //
				+ "OX x|o|. ||Xo|o|xX|| .|o|. X|||  .|.|. || .|.|. || .|.|.  |||OX.|x|oX||Ox|o|xO||Xx|.|.XOO\n" //
				+ "OX ----- ||X-----X|| ----- X|||  ----- || ----- || -----  |||OX-----X||O-----O||X-----XOO\n" //
				+ "OX o|x|o ||Xx|o|xX|| o|.|. X|||  .|.|. || .|.|. || .|.|.  |||OXx|o|oX||Oo|o|xO||Xx|x|xXOO\n" //
				+ "OX       ||XXXXXXX||       X|||        ||       ||        |||OXXXXXXX||OOOOOOO||XXXXXXXOO\n" //
				+ "OX-------------------------X||| ------------------------- |||O-------------------------OO\n" //
				+ "OX-------------------------X||| ------------------------- |||O-------------------------OO\n" //
				+ "OXOOOOOOO||XXXXXXX||OOOOOOOX|||        ||       ||        |||O       ||XXXXXXX||OOOOOOOOO\n" //
				+ "OXOx|.|.O||Xo|.|xX||Ox|o|.OX|||  .|.|. || .|.|. || .|.|.  |||O o|o|x ||Xx|o|oX||Ox|o|oOOO\n" //
				+ "OXO-----O||X-----X||O-----OX|||  ----- || ----- || -----  |||O ----- ||X-----X||O-----OOO\n" //
				+ "OXOo|x|.O||X.|x|.X||Oo|o|xOX|||  .|.|. || .|.|. || .|.|.  |||O o|.|o ||Xx|x|oX||Oo|o|xOOO\n" //
				+ "OXO-----O||X-----X||O-----OX|||  ----- || ----- || -----  |||O ----- ||X-----X||O-----OOO\n" //
				+ "OXOo|o|oO||Xx|.|.X||Oo|o|xOX|||  .|.|. || .|.|. || .|.|.  |||O x|o|x ||X.|o|xX||O.|o|.OOO\n" //
				+ "OXOOOOOOO||XXXXXXX||OOOOOOOX|||        ||       ||        |||O       ||XXXXXXX||OOOOOOOOO\n" //
				+ "OXXXXXXXXXXXXXXXXXXXXXXXXXXX|||                           |||OOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "O---------------------------------------------------------------------------------------O\n" //
				+ "O---------------------------------------------------------------------------------------O\n" //
				+ "O---------------------------------------------------------------------------------------O\n" //
				+ "OXXXXXXXXXXXXXXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOOOOOOOOOOOOOO|||OOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "OXXXXXXXX||OOOOOOO||XXXXXXXX|||OOOOOOOO||OOOOOOO||OOOOOOOO|||OOOOOOOO||???????||OOOOOOOOO\n" //
				+ "OXXx|o|oX||O.|x|xO||Xo|x|oXX|||OO.|x|xO||Ox|o|oO||Ox|.|oOO|||OOo|o|xO||?x|o|o?||Ox|.|xOOO\n" //
				+ "OXX-----X||O-----O||X-----XX|||OO-----O||O-----O||O-----OO|||OO-----O||?-----?||O-----OOO\n" //
				+ "OXXx|x|.X||Oo|o|.O||X.|x|.XX|||OOo|o|.O||Oo|o|oO||Ox|.|oOO|||OOx|o|oO||?o|x|x?||Oo|o|oOOO\n" //
				+ "OXX-----X||O-----O||X-----XX|||OO-----O||O-----O||O-----OO|||OO-----O||?-----?||O-----OOO\n" //
				+ "OXX.|x|xX||Oo|o|oO||Xx|x|.XX|||OOo|o|oO||Oo|o|.O||Oo|x|oOO|||OOx|o|xO||?x|x|o?||O.|o|oOOO\n" //
				+ "OXXXXXXXX||OOOOOOO||XXXXXXXX|||OOOOOOOO||OOOOOOO||OOOOOOOO|||OOOOOOOO||???????||OOOOOOOOO\n" //
				+ "OX-------------------------X|||O-------------------------O|||O-------------------------OO\n" //
				+ "OX-------------------------X|||O-------------------------O|||O-------------------------OO\n" //
				+ "OXOOOOOOO||XXXXXXX||       X|||OOOOOOOO||       ||XXXXXXXO|||OXXXXXXX||OOOOOOO||XXXXXXXOO\n" //
				+ "OXOo|o|.O||Xx|x|xX|| o|x|. X|||OOo|x|xO|| x|x|. ||X.|x|xXO|||OXx|x|.X||Ox|o|xO||Xx|x|xXOO\n" //
				+ "OXO-----O||X-----X|| ----- X|||OO-----O|| ----- ||X-----XO|||OX-----X||O-----O||X-----XOO\n" //
				+ "OXOx|o|oO||Xx|o|xX|| .|.|. X|||OOo|.|.O|| x|o|o ||X.|x|.XO|||OXo|x|xX||Oo|o|oO||Xo|x|xXOO\n" //
				+ "OXO-----O||X-----X|| ----- X|||OO-----O|| ----- ||X-----XO|||OX-----X||O-----O||X-----XOO\n" //
				+ "OXO.|o|.O||Xo|o|xX|| x|x|. X|||OOo|o|.O|| o|x|. ||Xx|o|.XO|||OX.|x|xX||Oo|x|xO||Xx|o|oXOO\n" //
				+ "OXOOOOOOO||XXXXXXX||       X|||OOOOOOOO||       ||XXXXXXXO|||OXXXXXXX||OOOOOOO||XXXXXXXOO\n" //
				+ "OX-------------------------X|||O-------------------------O|||O-------------------------OO\n" //
				+ "OX-------------------------X|||O-------------------------O|||O-------------------------OO\n" //
				+ "OXXXXXXXX||???????||       X|||OXXXXXXX||XXXXXXX||       O|||OOOOOOOO||XXXXXXX||XXXXXXXOO\n" //
				+ "OXXx|o|.X||?o|x|x?|| o|o|. X|||OXx|.|.X||Xx|.|oX|| o|x|x O|||OOo|o|xO||Xo|x|.X||Xo|o|.XOO\n" //
				+ "OXX-----X||?-----?|| ----- X|||OX-----X||X-----X|| ----- O|||OO-----O||X-----X||X-----XOO\n" //
				+ "OXXx|x|oX||?x|o|o?|| .|o|. X|||OXx|x|xX||Xx|x|oX|| .|.|. O|||OOo|o|oO||X.|.|oX||Xo|.|oXOO\n" //
				+ "OXX-----X||?-----?|| ----- X|||OX-----X||X-----X|| ----- O|||OO-----O||X-----X||X-----XOO\n" //
				+ "OXXo|o|xX||?x|o|x?|| o|x|. X|||OXo|x|.X||X.|o|xX|| .|x|. O|||OO.|o|.O||Xx|x|xX||Xx|x|xXOO\n" //
				+ "OXXXXXXXX||???????||       X|||OXXXXXXX||XXXXXXX||       O|||OOOOOOOO||XXXXXXX||XXXXXXXOO\n" //
				+ "OXXXXXXXXXXXXXXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOOOOOOOOOOOOOO|||OOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "O---------------------------------------------------------------------------------------O\n" //
				+ "O---------------------------------------------------------------------------------------O\n" //
				+ "O---------------------------------------------------------------------------------------O\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXXO\n" //
				+ "OOOOOOOOO||OOOOOOO||XXXXXXXO|||X       ||XXXXXXX||XXXXXXXX|||X       ||       ||XXXXXXXXO\n" //
				+ "OOOo|o|xO||Oo|.|xO||Xx|.|.XO|||X x|.|o ||X.|.|.X||Xx|x|xXX|||X .|.|. || o|.|o ||Xx|o|.XXO\n" //
				+ "OOO-----O||O-----O||X-----XO|||X ----- ||X-----X||X-----XX|||X ----- || ----- ||X-----XXO\n" //
				+ "OOOo|o|.O||Ox|o|xO||Xx|x|.XO|||X .|o|x ||Xx|x|xX||Xx|o|oXX|||X o|x|o || o|o|x ||Xx|.|oXXO\n" //
				+ "OOO-----O||O-----O||X-----XO|||X ----- ||X-----X||X-----XX|||X ----- || ----- ||X-----XXO\n" //
				+ "OOOx|x|oO||Ox|x|oO||Xx|.|oXO|||X x|.|x ||Xx|.|oX||Xo|.|xXX|||X o|x|x || x|o|. ||Xx|o|.XXO\n" //
				+ "OOOOOOOOO||OOOOOOO||XXXXXXXO|||X       ||XXXXXXX||XXXXXXXX|||X       ||       ||XXXXXXXXO\n" //
				+ "OO-------------------------O|||X-------------------------X|||X-------------------------XO\n" //
				+ "OO-------------------------O|||X-------------------------X|||X-------------------------XO\n" //
				+ "OOOOOOOOO||XXXXXXX||       O|||XOOOOOOO||XXXXXXX||OOOOOOOX|||X       ||XXXXXXX||XXXXXXXXO\n" //
				+ "OOOx|o|oO||Xx|x|.X|| o|x|. O|||XOo|.|oO||X.|.|oX||O.|o|.OX|||X x|x|o ||X.|x|xX||Xx|x|xXXO\n" //
				+ "OOO-----O||X-----X|| ----- O|||XO-----O||X-----X||O-----OX|||X ----- ||X-----X||X-----XXO\n" //
				+ "OOOo|o|xO||Xx|o|.X|| .|x|o O|||XO.|o|oO||Xx|x|xX||Oo|o|oOX|||X o|.|x ||Xo|x|oX||X.|.|oXXO\n" //
				+ "OOO-----O||X-----X|| ----- O|||XO-----O||X-----X||O-----OX|||X ----- ||X-----X||X-----XXO\n" //
				+ "OOOx|o|xO||Xx|.|oX|| o|o|x O|||XOo|o|xO||Xx|o|.X||Ox|o|xOX|||X x|x|o ||Xx|o|oX||Xx|.|xXXO\n" //
				+ "OOOOOOOOO||XXXXXXX||       O|||XOOOOOOO||XXXXXXX||OOOOOOOX|||X       ||XXXXXXX||XXXXXXXXO\n" //
				+ "OO-------------------------O|||X-------------------------X|||X-------------------------XO\n" //
				+ "OO-------------------------O|||X-------------------------X|||X-------------------------XO\n" //
				+ "OOOOOOOOO||XXXXXXX||XXXXXXXO|||XXXXXXXX||OOOOOOO||OOOOOOOX|||XOOOOOOO||OOOOOOO||XXXXXXXXO\n" //
				+ "OOOo|.|oO||Xx|o|oX||Xx|o|oXO|||XXo|o|xX||O.|o|xO||Oo|o|oOX|||XOo|.|.O||O.|x|.O||Xx|.|xXXO\n" //
				+ "OOO-----O||X-----X||X-----XO|||XX-----X||O-----O||O-----OX|||XO-----O||O-----O||X-----XXO\n" //
				+ "OOOo|o|xO||Xo|x|oX||X.|o|xXO|||XXx|o|xX||Ox|o|xO||O.|.|.OX|||XO.|x|.O||O.|o|xO||Xx|.|oXXO\n" //
				+ "OOO-----O||X-----X||X-----XO|||XX-----X||O-----O||O-----OX|||XO-----O||O-----O||X-----XXO\n" //
				+ "OOO.|o|oO||Xx|o|xX||Xx|x|xXO|||XXo|x|xX||O.|o|.O||O.|x|xOX|||XOo|o|oO||Oo|o|oO||Xx|.|oXXO\n" //
				+ "OOOOOOOOO||XXXXXXX||XXXXXXXO|||XXXXXXXX||OOOOOOO||OOOOOOOX|||XOOOOOOO||OOOOOOO||XXXXXXXXO\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXXO\n" //
				+ "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
				+ "TOP.\n" //
		;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	public static Board random_h1s3Board() {
		final Logger log = StackFrameUtil.methodLogger();
		log.trace("-------------------------------------------------------------");

		Board board = new Board(1, 3);
		Player playerAAA = Player.create(PlayerPredictable.class, Token.PLAYER_AAA);
		Player playerBBB = Player.create(PlayerPredictable.class, Token.PLAYER_BBB);

		Player player = playerAAA;
		while (board.isPlayable()) {
			Move move = player.makeMove(log, board, null);
			Position position = move.toPosition(board);
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

		Board board = new Board(2, 3); // 81 token positions
		Player playerAAA = Player.create(PlayerPredictable.class, Token.PLAYER_AAA);
		Player playerBBB = Player.create(PlayerPredictable.class, Token.PLAYER_BBB);

		Player player = playerAAA;
		for (int i = 0; board.isPlayable() && (i < 100); ++i) {
			log.trace("#" + i);

			Move move = player.makeMove(log, board, null);
			log.trace(move.toString() + " by " + player.getToken());

			Position position = move.toPosition(board);
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

		Board board = new Board(2, 3); // 81 token positions
		Player playerAAA = Player.create(PlayerPredictable.class, Token.PLAYER_AAA);
		Player playerBBB = Player.create(PlayerPredictable.class, Token.PLAYER_BBB);

		Player player = playerAAA;
		Position constraint = null;
		for (int i = 0; board.isPlayable() && (i < 100); ++i) {
			log.trace("#" + i + ": constraint=[" + constraint + "]");

			Move move = player.makeMove(log, board, constraint);
			Position position = move.toPosition(board);
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

		Board board = new Board(3, 2); // 64 token positions
		Player playerAAA = Player.create(PlayerPredictable.class, Token.PLAYER_AAA);
		Player playerBBB = Player.create(PlayerPredictable.class, Token.PLAYER_BBB);

		Player player = playerAAA;
		for (int i = 0; board.isPlayable() && (i < 70); ++i) {
			log.trace("#" + i);

			Move move = player.makeMove(log, board, null);
			log.trace(move.toString() + " by " + player.getToken());

			Position position = move.toPosition(board);
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

		Board board = new Board(3, 2); // 64 token positions
		Player playerAAA = Player.create(PlayerPredictable.class, Token.PLAYER_AAA);
		Player playerBBB = Player.create(PlayerPredictable.class, Token.PLAYER_BBB);

		Player player = playerAAA;
		Position constraint = null;
		for (int i = 0; board.isPlayable() && (i < 80); ++i) {
			log.trace("#" + i);

			Move move = player.makeMove(log, board, constraint);
			Position position = move.toPosition(board);

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

		Board board = new Board(3, 3); // 729 token locations
		Player playerAAA = Player.create(PlayerPredictable.class, Token.PLAYER_AAA);
		Player playerBBB = Player.create(PlayerPredictable.class, Token.PLAYER_BBB);

		Player player = playerAAA;
		for (int i = 0; board.isPlayable() && (i < 800); ++i) {
			log.trace("#" + i);

			Move move = player.makeMove(log, board, null);
			log.trace(move.toString() + " by " + player.getToken());

			Position position = move.toPosition(board);
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

		Board board = new Board(3, 3); // 729 token locations
		Player playerAAA = Player.create(PlayerPredictable.class, Token.PLAYER_AAA);
		Player playerBBB = Player.create(PlayerPredictable.class, Token.PLAYER_BBB);

		Player player = playerAAA;
		Position constraint = null;
		int depth = 0;
		for (int i = 0; board.isPlayable() && (i < 800); ++i) {
			depth = (constraint == null) ? 0 : constraint.depth();
			log.trace("#" + i);
			log.trace("Current constraint: [" + constraint + "]; depth=[" + depth + "]");

			Move move = player.makeMove(log, board, constraint);
			Position position = move.toPosition(board);

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
