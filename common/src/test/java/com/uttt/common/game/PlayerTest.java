package com.uttt.common.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.uttt.common.board.Board;
import com.uttt.common.board.Position;
import com.uttt.common.board.Token;
import com.uttt.common.testutil.StackFrameUtil;

public class PlayerTest {

	@Test
	public void random_h1s3() {

		final Logger log = StackFrameUtil.methodLogger();
		log.trace("-------------------------------------------------------------");

		Board  board     = new Board(1,3);
		Player playerAAA = PlayerRandom.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerRandom.create(Token.PLAYER_BBB);

		Player player = playerAAA;
		while (board.isPlayable()) {
			Move     move       = player.makeMove(log, board, null);
			Position position   = move.toPosition(board);
			Position constraint = position.place(player.getToken());

			assertNull(constraint);

			player = (player == playerAAA) ? playerBBB : playerAAA;

			log.trace("after placing:\n" + board.fieldAsPrintableString());
		}

		String expected = "" //
			+ "XXXXXXX\n" //
			+ "Xx|.|oX\n" //
			+ "X-----X\n" //
			+ "X.|x|oX\n" //
			+ "X-----X\n" //
			+ "X.|.|xX\n" //
			+ "XXXXXXX\n" //
			+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h2s3_ignoreConstraints() {

		final Logger log = StackFrameUtil.methodLogger();
		log.trace("-------------------------------------------------------------");

		Board  board     = new Board(2,3); // 81 token positions
		Player playerAAA = PlayerRandom.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerRandom.create(Token.PLAYER_BBB);

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

		String expected = "" //
			+ "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
			+ "XXXXXXXX||OOOOOOO||OOOOOOOX\n" //
			+ "XX.|.|xX||Ox|.|oO||Oo|o|xOX\n" //
			+ "XX-----X||O-----O||O-----OX\n" //
			+ "XXo|x|oX||Ox|.|xO||Oo|o|.OX\n" //
			+ "XX-----X||O-----O||O-----OX\n" //
			+ "XXx|o|.X||Oo|o|oO||Oo|.|xOX\n" //
			+ "XXXXXXXX||OOOOOOO||OOOOOOOX\n" //
			+ "X-------------------------X\n" //
			+ "X-------------------------X\n" //
			+ "XXXXXXXX||XXXXXXX||OOOOOOOX\n" //
			+ "XXx|x|xX||Xx|o|xX||Oo|o|oOX\n" //
			+ "XX-----X||X-----X||O-----OX\n" //
			+ "XXo|.|xX||Xx|.|oX||O.|.|.OX\n" //
			+ "XX-----X||X-----X||O-----OX\n" //
			+ "XXo|x|.X||Xx|x|.X||O.|.|.OX\n" //
			+ "XXXXXXXX||XXXXXXX||OOOOOOOX\n" //
			+ "X-------------------------X\n" //
			+ "X-------------------------X\n" //
			+ "XXXXXXXX||       ||XXXXXXXX\n" //
			+ "XXx|.|xX|| x|o|o ||Xx|.|xXX\n" //
			+ "XX-----X|| ----- ||X-----XX\n" //
			+ "XX.|.|xX|| o|.|o ||Xo|x|oXX\n" //
			+ "XX-----X|| ----- ||X-----XX\n" //
			+ "XXo|o|xX|| x|.|. ||Xo|x|xXX\n" //
			+ "XXXXXXXX||       ||XXXXXXXX\n" //
			+ "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
			+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h2s3_followConstraints() {

		final Logger log = StackFrameUtil.methodLogger();
		log.trace("-------------------------------------------------------------");

		Board  board     = new Board(2,3); // 81 token positions
		Player playerAAA = PlayerRandom.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerRandom.create(Token.PLAYER_BBB);

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

		String expected = "" //
			+ "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
			+ "X       ||XXXXXXX||XXXXXXXX\n" //
			+ "X .|o|. ||Xx|x|oX||X.|x|xXX\n" //
			+ "X ----- ||X-----X||X-----XX\n" //
			+ "X o|x|x ||Xx|o|.X||Xo|x|oXX\n" //
			+ "X ----- ||X-----X||X-----XX\n" //
			+ "X o|.|x ||Xx|.|oX||X.|x|oXX\n" //
			+ "X       ||XXXXXXX||XXXXXXXX\n" //
			+ "X-------------------------X\n" //
			+ "X-------------------------X\n" //
			+ "XOOOOOOO||XXXXXXX||       X\n" //
			+ "XOo|.|oO||X.|.|xX|| o|.|o X\n" //
			+ "XO-----O||X-----X|| ----- X\n" //
			+ "XOo|x|xO||Xo|o|xX|| x|x|o X\n" //
			+ "XO-----O||X-----X|| ----- X\n" //
			+ "XOo|.|.O||X.|o|xX|| .|x|x X\n" //
			+ "XOOOOOOO||XXXXXXX||       X\n" //
			+ "X-------------------------X\n" //
			+ "X-------------------------X\n" //
			+ "XXXXXXXX||       ||OOOOOOOX\n" //
			+ "XXx|x|oX|| x|.|x ||O.|.|oOX\n" //
			+ "XX-----X|| ----- ||O-----OX\n" //
			+ "XXx|o|.X|| .|o|o ||Ox|x|oOX\n" //
			+ "XX-----X|| ----- ||O-----OX\n" //
			+ "XXx|.|xX|| .|.|. ||Oo|o|oOX\n" //
			+ "XXXXXXXX||       ||OOOOOOOX\n" //
			+ "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
			+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h3s2_ignoreConstraints() {

		final Logger log = StackFrameUtil.methodLogger();
		log.trace("-------------------------------------------------------------");

		Board  board     = new Board(3,2); // 64 token positions
		Player playerAAA = PlayerRandom.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerRandom.create(Token.PLAYER_BBB);

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

		String expected = "" //
			+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
			+ "X              |||XXXXXXXXXXXXXXX\n" //
			+ "X      ||      |||X     ||XXXXXXX\n" //
			+ "X  .|. || .|.  |||X .|. ||Xx|oXXX\n" //
			+ "X  --- || ---  |||X --- ||X---XXX\n" //
			+ "X  x|. || .|x  |||X .|o ||Xx|.XXX\n" //
			+ "X      ||      |||X     ||XXXXXXX\n" //
			+ "X ------------ |||X------------XX\n" //
			+ "X ------------ |||X------------XX\n" //
			+ "X      ||OOOOO |||XXXXXX||OOOOOXX\n" //
			+ "X  o|. ||O.|oO |||XX.|oX||O.|oOXX\n" //
			+ "X  --- ||O---O |||XX---X||O---OXX\n" //
			+ "X  x|. ||O.|oO |||XXx|xX||Oo|.OXX\n" //
			+ "X      ||OOOOO |||XXXXXX||OOOOOXX\n" //
			+ "X              |||XXXXXXXXXXXXXXX\n" //
			+ "X-------------------------------X\n" //
			+ "X-------------------------------X\n" //
			+ "X-------------------------------X\n" //
			+ "X              |||XXXXXXXXXXXXXXX\n" //
			+ "X      ||      |||XOOOOO||XXXXXXX\n" //
			+ "X  .|. || .|o  |||XO.|xO||Xx|.XXX\n" //
			+ "X  --- || ---  |||XO---O||X---XXX\n" //
			+ "X  x|. || .|.  |||XOo|oO||X.|xXXX\n" //
			+ "X      ||      |||XOOOOO||XXXXXXX\n" //
			+ "X ------------ |||X------------XX\n" //
			+ "X ------------ |||X------------XX\n" //
			+ "X OOOOO||      |||X     ||XXXXXXX\n" //
			+ "X Oo|.O|| o|x  |||X .|. ||X.|.XXX\n" //
			+ "X O---O|| ---  |||X --- ||X---XXX\n" //
			+ "X Ox|oO|| .|.  |||X .|. ||Xx|xXXX\n" //
			+ "X OOOOO||      |||X     ||XXXXXXX\n" //
			+ "X              |||XXXXXXXXXXXXXXX\n" //
			+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
			+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h3s2_followConstraints() {

		final Logger log = StackFrameUtil.methodLogger();
		log.trace("-------------------------------------------------------------");

		Board  board     = new Board(3,2); // 64 token positions
		Player playerAAA = PlayerRandom.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerRandom.create(Token.PLAYER_BBB);

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

		String expected = "" //
			+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
			+ "XOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXX\n" //
			+ "XOOOOOO||     O|||XOOOOO||     XX\n" //
			+ "XOOx|.O|| .|. O|||XO.|.O|| .|. XX\n" //
			+ "XOO---O|| --- O|||XO---O|| --- XX\n" //
			+ "XOOo|oO|| o|x O|||XOo|oO|| .|. XX\n" //
			+ "XOOOOOO||     O|||XOOOOO||     XX\n" //
			+ "XO------------O|||X------------XX\n" //
			+ "XO------------O|||X------------XX\n" //
			+ "XOXXXXX||OOOOOO|||XXXXXX||XXXXXXX\n" //
			+ "XOXx|oX||Oo|xOO|||XXx|.X||Xx|.XXX\n" //
			+ "XOX---X||O---OO|||XX---X||X---XXX\n" //
			+ "XOX.|xX||O.|oOO|||XXx|oX||X.|xXXX\n" //
			+ "XOXXXXX||OOOOOO|||XXXXXX||XXXXXXX\n" //
			+ "XOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXX\n" //
			+ "X-------------------------------X\n" //
			+ "X-------------------------------X\n" //
			+ "X-------------------------------X\n" //
			+ "XXXXXXXXXXXXXXX|||              X\n" //
			+ "XXXXXXX||XXXXXX|||      ||      X\n" //
			+ "XXXx|.X||Xo|xXX|||  .|. || .|.  X\n" //
			+ "XXX---X||X---XX|||  --- || ---  X\n" //
			+ "XXXx|.X||X.|xXX|||  .|. || .|.  X\n" //
			+ "XXXXXXX||XXXXXX|||      ||      X\n" //
			+ "XX------------X||| ------------ X\n" //
			+ "XX------------X||| ------------ X\n" //
			+ "XX     ||     X|||      ||      X\n" //
			+ "XX .|o || .|o X|||  .|. || .|.  X\n" //
			+ "XX --- || --- X|||  --- || ---  X\n" //
			+ "XX .|. || .|. X|||  .|. || .|.  X\n" //
			+ "XX     ||     X|||      ||      X\n" //
			+ "XXXXXXXXXXXXXXX|||              X\n" //
			+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
			+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void random_h3s3_ignoreConstraints() {

		final Logger log = StackFrameUtil.methodLogger();
		log.trace("-------------------------------------------------------------");

		Board  board     = new Board(3,3); // 729 token locations
		Player playerAAA = PlayerRandom.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerRandom.create(Token.PLAYER_BBB);

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

		String expected = "" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "XXOOOOOOO||       ||OOOOOOOX|||X       ||???????||       X|||X       ||OOOOOOO||XXXXXXXXX\n" //
				+ "XXO.|o|.O|| .|.|. ||Oo|o|oOX|||X .|.|x ||?o|x|o?|| x|.|o X|||X o|x|x ||O.|o|oO||Xo|x|.XXX\n" //
				+ "XXO-----O|| ----- ||O-----OX|||X ----- ||?-----?|| ----- X|||X ----- ||O-----O||X-----XXX\n" //
				+ "XXOo|o|oO|| .|o|o ||Ox|.|xOX|||X .|.|o ||?o|o|x?|| .|.|. X|||X .|.|o ||O.|x|oO||Xx|x|xXXX\n" //
				+ "XXO-----O|| ----- ||O-----OX|||X ----- ||?-----?|| ----- X|||X ----- ||O-----O||X-----XXX\n" //
				+ "XXOx|.|.O|| x|o|. ||O.|o|oOX|||X o|o|. ||?x|o|x?|| x|.|o X|||X .|.|. ||O.|x|oO||Xx|.|xXXX\n" //
				+ "XXOOOOOOO||       ||OOOOOOOX|||X       ||???????||       X|||X       ||OOOOOOO||XXXXXXXXX\n" //
				+ "XX-------------------------X|||X-------------------------X|||X-------------------------XX\n" //
				+ "XX-------------------------X|||X-------------------------X|||X-------------------------XX\n" //
				+ "XXXXXXXXX||XXXXXXX||XXXXXXXX|||X       ||       ||XXXXXXXX|||XXXXXXXX||XXXXXXX||XXXXXXXXX\n" //
				+ "XXXx|x|xX||Xx|o|oX||Xo|o|xXX|||X .|x|. || o|o|x ||X.|.|xXX|||XXx|x|oX||Xx|o|xX||X.|.|xXXX\n" //
				+ "XXX-----X||X-----X||X-----XX|||X ----- || ----- ||X-----XX|||XX-----X||X-----X||X-----XXX\n" //
				+ "XXX.|o|oX||Xx|x|oX||Xx|x|oXX|||X o|o|x || x|.|o ||Xx|o|.XX|||XXo|x|xX||Xo|x|xX||Xx|x|xXXX\n" //
				+ "XXX-----X||X-----X||X-----XX|||X ----- || ----- ||X-----XX|||XX-----X||X-----X||X-----XXX\n" //
				+ "XXX.|x|xX||Xo|o|xX||Xx|x|.XX|||X x|x|o || .|.|x ||Xx|x|xXX|||XXx|o|xX||Xx|.|oX||X.|x|.XXX\n" //
				+ "XXXXXXXXX||XXXXXXX||XXXXXXXX|||X       ||       ||XXXXXXXX|||XXXXXXXX||XXXXXXX||XXXXXXXXX\n" //
				+ "XX-------------------------X|||X-------------------------X|||X-------------------------XX\n" //
				+ "XX-------------------------X|||X-------------------------X|||X-------------------------XX\n" //
				+ "XX       ||       ||       X|||XXXXXXXX||XXXXXXX||XXXXXXXX|||X???????||XXXXXXX||OOOOOOOXX\n" //
				+ "XX .|.|o || o|o|x || o|x|o X|||XXx|x|oX||Xx|.|.X||Xo|o|xXX|||X?x|o|x?||Xx|.|oX||O.|.|oOXX\n" //
				+ "XX ----- || ----- || ----- X|||XX-----X||X-----X||X-----XX|||X?-----?||X-----X||O-----OXX\n" //
				+ "XX o|.|o || .|.|o || x|x|. X|||XXx|.|.X||Xx|.|oX||X.|x|oXX|||X?x|x|o?||Xx|.|.X||O.|o|.OXX\n" //
				+ "XX ----- || ----- || ----- X|||XX-----X||X-----X||X-----XX|||X?-----?||X-----X||O-----OXX\n" //
				+ "XX o|.|. || o|o|x || x|o|x X|||XXx|x|oX||Xx|.|xX||Xx|x|xXX|||X?o|x|o?||Xx|.|.X||Oo|.|.OXX\n" //
				+ "XX       ||       ||       X|||XXXXXXXX||XXXXXXX||XXXXXXXX|||X???????||XXXXXXX||OOOOOOOXX\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "X???????????????????????????|||                           |||                           X\n" //
				+ "X?OOOOOOO||OOOOOOO||XXXXXXX?||| OOOOOOO||XXXXXXX||OOOOOOO ||| XXXXXXX||       ||OOOOOOO X\n" //
				+ "X?Oo|.|oO||O.|.|oO||Xx|.|oX?||| Oo|o|oO||X.|x|xX||Oo|x|.O ||| Xo|.|oX|| x|.|x ||Ox|.|.O X\n" //
				+ "X?O-----O||O-----O||X-----X?||| O-----O||X-----X||O-----O ||| X-----X|| ----- ||O-----O X\n" //
				+ "X?Ox|o|.O||Ox|o|oO||X.|x|xX?||| Oo|.|.O||Xx|x|oX||Ox|o|xO ||| Xx|x|xX|| o|.|. ||Ox|o|oO X\n" //
				+ "X?O-----O||O-----O||X-----X?||| O-----O||X-----X||O-----O ||| X-----X|| ----- ||O-----O X\n" //
				+ "X?Oo|.|.O||Oo|.|xO||Xo|o|xX?||| O.|.|.O||Xo|x|oX||Oo|x|oO ||| Xx|.|oX|| o|o|. ||Oo|o|oO X\n" //
				+ "X?OOOOOOO||OOOOOOO||XXXXXXX?||| OOOOOOO||XXXXXXX||OOOOOOO ||| XXXXXXX||       ||OOOOOOO X\n" //
				+ "X?-------------------------?||| ------------------------- ||| ------------------------- X\n" //
				+ "X?-------------------------?||| ------------------------- ||| ------------------------- X\n" //
				+ "X?OOOOOOO||XXXXXXX||XXXXXXX?|||        ||OOOOOOO||        ||| OOOOOOO||       ||OOOOOOO X\n" //
				+ "X?Oo|.|xO||Xo|o|xX||Xx|.|oX?|||  .|.|x ||Oo|o|xO|| o|x|.  ||| Ox|o|oO|| .|.|o ||Oo|.|.O X\n" //
				+ "X?O-----O||X-----X||X-----X?|||  ----- ||O-----O|| -----  ||| O-----O|| ----- ||O-----O X\n" //
				+ "X?Oo|o|.O||Xx|x|oX||Xo|x|oX?|||  o|.|o ||Oo|o|oO|| .|.|x  ||| Ox|.|oO|| o|x|x ||Oo|.|oO X\n" //
				+ "X?O-----O||X-----X||X-----X?|||  ----- ||O-----O|| -----  ||| O-----O|| ----- ||O-----O X\n" //
				+ "X?Oo|o|.O||Xx|o|xX||Xx|.|xX?|||  .|o|. ||Ox|.|xO|| o|x|.  ||| O.|.|oO|| x|o|o ||Oo|x|.O X\n" //
				+ "X?OOOOOOO||XXXXXXX||XXXXXXX?|||        ||OOOOOOO||        ||| OOOOOOO||       ||OOOOOOO X\n" //
				+ "X?-------------------------?||| ------------------------- ||| ------------------------- X\n" //
				+ "X?-------------------------?||| ------------------------- ||| ------------------------- X\n" //
				+ "X????????||XXXXXXX||OOOOOOO?||| XXXXXXX||       ||        ||| OOOOOOO||XXXXXXX||        X\n" //
				+ "X??x|x|o?||Xx|.|xX||Ox|o|.O?||| Xo|o|xX|| .|x|. || .|x|o  ||| O.|.|.O||X.|.|xX|| x|o|.  X\n" //
				+ "X??-----?||X-----X||O-----O?||| X-----X|| ----- || -----  ||| O-----O||X-----X|| -----  X\n" //
				+ "X??o|x|x?||X.|x|.X||Ox|o|.O?||| Xx|.|xX|| o|.|. || o|.|o  ||| O.|o|.O||X.|o|.X|| o|.|x  X\n" //
				+ "X??-----?||X-----X||O-----O?||| X-----X|| ----- || -----  ||| O-----O||X-----X|| -----  X\n" //
				+ "X??x|o|o?||Xx|.|.X||Oo|o|xO?||| Xx|o|xX|| o|o|. || o|o|x  ||| Oo|o|oO||Xx|x|xX|| o|x|x  X\n" //
				+ "X????????||XXXXXXX||OOOOOOO?||| XXXXXXX||       ||        ||| OOOOOOO||XXXXXXX||        X\n" //
				+ "X???????????????????????????|||                           |||                           X\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "X---------------------------------------------------------------------------------------X\n" //
				+ "X                           |||                           |||XXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "X        ||XXXXXXX||XXXXXXX |||        ||XXXXXXX||        |||XXXXXXXX||       ||XXXXXXXXX\n" //
				+ "X  x|o|x ||Xo|o|.X||Xo|x|xX |||  x|.|o ||Xx|o|oX|| .|.|x  |||XXx|.|.X|| o|x|. ||Xo|.|xXXX\n" //
				+ "X  ----- ||X-----X||X-----X |||  ----- ||X-----X|| -----  |||XX-----X|| ----- ||X-----XXX\n" //
				+ "X  o|.|. ||Xx|x|xX||X.|x|xX |||  o|x|. ||Xx|x|xX|| .|o|x  |||XXo|x|.X|| x|.|o ||X.|.|xXXX\n" //
				+ "X  ----- ||X-----X||X-----X |||  ----- ||X-----X|| -----  |||XX-----X|| ----- ||X-----XXX\n" //
				+ "X  x|.|o ||X.|x|oX||Xx|o|.X |||  x|o|o ||X.|o|oX|| x|x|o  |||XX.|.|xX|| .|x|x ||Xo|.|xXXX\n" //
				+ "X        ||XXXXXXX||XXXXXXX |||        ||XXXXXXX||        |||XXXXXXXX||       ||XXXXXXXXX\n" //
				+ "X ------------------------- ||| ------------------------- |||X-------------------------XX\n" //
				+ "X ------------------------- ||| ------------------------- |||X-------------------------XX\n" //
				+ "X OOOOOOO||       ||        |||        ||       ||        |||XOOOOOOO||       ||XXXXXXXXX\n" //
				+ "X Oo|x|oO|| o|x|o || o|.|x  |||  .|x|. || .|o|o || .|o|x  |||XOx|x|.O|| o|x|o ||Xo|x|oXXX\n" //
				+ "X O-----O|| ----- || -----  |||  ----- || ----- || -----  |||XO-----O|| ----- ||X-----XXX\n" //
				+ "X Oo|o|xO|| .|x|. || o|.|.  |||  .|.|. || .|x|x || o|x|x  |||XO.|.|xO|| .|o|x ||X.|.|oXXX\n" //
				+ "X O-----O|| ----- || -----  |||  ----- || ----- || -----  |||XO-----O|| ----- ||X-----XXX\n" //
				+ "X Ox|x|oO|| .|o|. || .|x|o  |||  .|x|o || .|o|o || .|o|.  |||XOo|o|oO|| x|.|x ||Xx|x|xXXX\n" //
				+ "X OOOOOOO||       ||        |||        ||       ||        |||XOOOOOOO||       ||XXXXXXXXX\n" //
				+ "X ------------------------- ||| ------------------------- |||X-------------------------XX\n" //
				+ "X ------------------------- ||| ------------------------- |||X-------------------------XX\n" //
				+ "X OOOOOOO||OOOOOOO||XXXXXXX ||| OOOOOOO||       ||        |||X       ||       ||XXXXXXXXX\n" //
				+ "X O.|x|oO||O.|.|.O||X.|x|xX ||| Oo|.|xO|| x|o|x || .|.|x  |||X .|.|x || .|.|x ||Xo|.|.XXX\n" //
				+ "X O-----O||O-----O||X-----X ||| O-----O|| ----- || -----  |||X ----- || ----- ||X-----XXX\n" //
				+ "X Ox|o|oO||Oo|o|oO||Xx|x|xX ||| Oo|o|xO|| o|.|x || .|x|o  |||X o|.|o || .|o|. ||Xo|.|oXXX\n" //
				+ "X O-----O||O-----O||X-----X ||| O-----O|| ----- || -----  |||X ----- || ----- ||X-----XXX\n" //
				+ "X Oo|o|xO||Ox|o|.O||Xx|x|oX ||| Ox|x|oO|| .|o|. || o|o|.  |||X o|x|. || .|o|. ||Xx|x|xXXX\n" //
				+ "X OOOOOOO||OOOOOOO||XXXXXXX ||| OOOOOOO||       ||        |||X       ||       ||XXXXXXXXX\n" //
				+ "X                           |||                           |||XXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
				+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}
	@Test
	public void random_h3s3_followConstraints() {

		final Logger log = StackFrameUtil.methodLogger();
		log.trace("-------------------------------------------------------------");

		Board  board     = new Board(3,3); // 729 token locations
		Player playerAAA = PlayerRandom.create(Token.PLAYER_AAA);
		Player playerBBB = PlayerRandom.create(Token.PLAYER_BBB);

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

		String expected = "" //
				+ "?????????????????????????????????????????????????????????????????????????????????????????\n" //
				+ "?XXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||????????????????????????????\n" //
				+ "?XXXXXXXX||XXXXXXX||XXXXXXXX|||XOOOOOOO||XXXXXXX||OOOOOOOX|||?OOOOOOO||XXXXXXX||XXXXXXX??\n" //
				+ "?XXo|o|xX||Xx|o|oX||X.|.|.XX|||XOo|.|.O||Xx|o|xX||Oo|o|xOX|||?Ox|o|.O||Xo|x|oX||Xx|.|xX??\n" //
				+ "?XX-----X||X-----X||X-----XX|||XO-----O||X-----X||O-----OX|||?O-----O||X-----X||X-----X??\n" //
				+ "?XX.|o|xX||Xo|x|oX||Xx|x|xXX|||XOx|o|oO||X.|x|xX||O.|.|.OX|||?Oo|o|oO||Xx|x|.X||Xo|o|.X??\n" //
				+ "?XX-----X||X-----X||X-----XX|||XO-----O||X-----X||O-----OX|||?O-----O||X-----X||X-----X??\n" //
				+ "?XXo|x|xX||Xo|x|xX||X.|x|.XX|||XO.|x|oO||Xx|x|oX||Oo|o|oOX|||?O.|o|.O||Xx|x|oX||Xx|x|xX??\n" //
				+ "?XXXXXXXX||XXXXXXX||XXXXXXXX|||XOOOOOOO||XXXXXXX||OOOOOOOX|||?OOOOOOO||XXXXXXX||XXXXXXX??\n" //
				+ "?X-------------------------X|||X-------------------------X|||?-------------------------??\n" //
				+ "?X-------------------------X|||X-------------------------X|||?-------------------------??\n" //
				+ "?X???????||OOOOOOO||XXXXXXXX|||XOOOOOOO||OOOOOOO||XXXXXXXX|||?XXXXXXX||XXXXXXX||OOOOOOO??\n" //
				+ "?X?o|x|x?||O.|.|oO||Xx|x|xXX|||XO.|x|xO||O.|.|oO||X.|.|xXX|||?Xx|.|oX||X.|x|oX||Ox|.|oO??\n" //
				+ "?X?-----?||O-----O||X-----XX|||XO-----O||O-----O||X-----XX|||?X-----X||X-----X||O-----O??\n" //
				+ "?X?x|o|o?||Oo|x|oO||Xx|o|.XX|||XOo|x|oO||Oo|.|oO||Xx|x|xXX|||?Xo|x|.X||X.|x|oX||Oo|x|oO??\n" //
				+ "?X?-----?||O-----O||X-----XX|||XO-----O||O-----O||X-----XX|||?X-----X||X-----X||O-----O??\n" //
				+ "?X?o|o|x?||Oo|x|oO||X.|x|.XX|||XOo|o|oO||O.|o|oO||Xo|.|oXX|||?Xo|.|xX||Xx|x|.X||Ox|x|oO??\n" //
				+ "?X???????||OOOOOOO||XXXXXXXX|||XOOOOOOO||OOOOOOO||XXXXXXXX|||?XXXXXXX||XXXXXXX||OOOOOOO??\n" //
				+ "?X-------------------------X|||X-------------------------X|||?-------------------------??\n" //
				+ "?X-------------------------X|||X-------------------------X|||?-------------------------??\n" //
				+ "?XXXXXXXX||OOOOOOO||OOOOOOOX|||XXXXXXXX||XXXXXXX||XXXXXXXX|||?OOOOOOO||OOOOOOO||XXXXXXX??\n" //
				+ "?XXx|x|.X||Oo|x|oO||O.|o|oOX|||XXx|o|xX||X.|o|xX||Xx|x|xXX|||?Ox|o|oO||Ox|o|.O||Xx|.|xX??\n" //
				+ "?XX-----X||O-----O||O-----OX|||XX-----X||X-----X||X-----XX|||?O-----O||O-----O||X-----X??\n" //
				+ "?XX.|x|.X||Oo|x|oO||O.|x|oOX|||XXx|x|.X||Xo|x|oX||X.|.|.XX|||?O.|.|xO||Oo|o|.O||Xx|o|.X??\n" //
				+ "?XX-----X||O-----O||O-----OX|||XX-----X||X-----X||X-----XX|||?O-----O||O-----O||X-----X??\n" //
				+ "?XX.|.|xX||Oo|o|xO||Ox|o|oOX|||XXo|x|xX||Xx|.|xX||X.|o|xXX|||?Oo|o|oO||O.|o|xO||Xx|o|oX??\n" //
				+ "?XXXXXXXX||OOOOOOO||OOOOOOOX|||XXXXXXXX||XXXXXXX||XXXXXXXX|||?OOOOOOO||OOOOOOO||XXXXXXX??\n" //
				+ "?XXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||????????????????????????????\n" //
				+ "?---------------------------------------------------------------------------------------?\n" //
				+ "?---------------------------------------------------------------------------------------?\n" //
				+ "?---------------------------------------------------------------------------------------?\n" //
				+ "????????????????????????????|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||????????????????????????????\n" //
				+ "??OOOOOOO||XXXXXXX||XXXXXXX?|||XXXXXXXX||XXXXXXX||XXXXXXXX|||????????||???????||OOOOOOO??\n" //
				+ "??O.|.|oO||X.|o|xX||Xx|o|xX?|||XX.|.|oX||X.|.|oX||Xx|x|oXX|||??o|x|x?||?o|x|x?||Oo|o|oO??\n" //
				+ "??O-----O||X-----X||X-----X?|||XX-----X||X-----X||X-----XX|||??-----?||?-----?||O-----O??\n" //
				+ "??Oo|x|oO||Xo|o|xX||Xo|x|xX?|||XX.|.|oX||Xx|x|xX||Xx|x|xXX|||??x|x|o?||?x|x|o?||Ox|.|.O??\n" //
				+ "??O-----O||X-----X||X-----X?|||XX-----X||X-----X||X-----XX|||??-----?||?-----?||O-----O??\n" //
				+ "??Oo|.|oO||Xo|.|xX||Xx|x|oX?|||XXx|x|xX||Xo|o|.X||X.|.|xXX|||??o|o|x?||?o|o|x?||Oo|x|.O??\n" //
				+ "??OOOOOOO||XXXXXXX||XXXXXXX?|||XXXXXXXX||XXXXXXX||XXXXXXXX|||????????||???????||OOOOOOO??\n" //
				+ "??-------------------------?|||X-------------------------X|||?-------------------------??\n" //
				+ "??-------------------------?|||X-------------------------X|||?-------------------------??\n" //
				+ "??XXXXXXX||OOOOOOO||????????|||X       ||       ||       X|||?OOOOOOO||OOOOOOO||?????????\n" //
				+ "??Xx|x|xX||Ox|x|oO||?x|o|o??|||X x|x|o || .|o|. || o|x|. X|||?Oo|x|oO||Oo|o|oO||?x|o|x???\n" //
				+ "??X-----X||O-----O||?-----??|||X ----- || ----- || ----- X|||?O-----O||O-----O||?-----???\n" //
				+ "??Xx|o|xX||Oo|x|oO||?o|o|x??|||X o|.|o || x|.|. || o|.|. X|||?Ox|.|xO||O.|x|oO||?o|x|x???\n" //
				+ "??X-----X||O-----O||?-----??|||X ----- || ----- || ----- X|||?O-----O||O-----O||?-----???\n" //
				+ "??Xx|x|oX||Ox|o|oO||?x|x|o??|||X .|x|x || o|.|. || x|x|. X|||?Oo|o|oO||Oo|o|.O||?o|x|o???\n" //
				+ "??XXXXXXX||OOOOOOO||????????|||X       ||       ||       X|||?OOOOOOO||OOOOOOO||?????????\n" //
				+ "??-------------------------?|||X-------------------------X|||?-------------------------??\n" //
				+ "??-------------------------?|||X-------------------------X|||?-------------------------??\n" //
				+ "??OOOOOOO||OOOOOOO||XXXXXXX?|||X       ||       ||       X|||?XXXXXXX||XXXXXXX||?????????\n" //
				+ "??O.|.|oO||Oo|.|oO||X.|.|xX?|||X o|.|o || x|o|x || o|o|. X|||?Xx|.|xX||Xo|x|xX||?x|o|x???\n" //
				+ "??O-----O||O-----O||X-----X?|||X ----- || ----- || ----- X|||?X-----X||X-----X||?-----???\n" //
				+ "??O.|.|oO||O.|x|oO||Xx|x|.X?|||X .|.|o || o|o|. || o|.|. X|||?Xo|x|xX||Xo|x|oX||?x|o|x???\n" //
				+ "??O-----O||O-----O||X-----X?|||X ----- || ----- || ----- X|||?X-----X||X-----X||?-----???\n" //
				+ "??Ox|.|oO||Oo|.|oO||Xx|.|.X?|||X x|.|x || .|.|. || .|o|. X|||?Xx|x|oX||Xx|o|xX||?o|x|o???\n" //
				+ "??OOOOOOO||OOOOOOO||XXXXXXX?|||X       ||       ||       X|||?XXXXXXX||XXXXXXX||?????????\n" //
				+ "????????????????????????????|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||????????????????????????????\n" //
				+ "?---------------------------------------------------------------------------------------?\n" //
				+ "?---------------------------------------------------------------------------------------?\n" //
				+ "?---------------------------------------------------------------------------------------?\n" //
				+ "?OOOOOOOOOOOOOOOOOOOOOOOOOOO|||OOOOOOOOOOOOOOOOOOOOOOOOOOO|||????????????????????????????\n" //
				+ "?OOOOOOOO||XXXXXXX||XXXXXXXO|||OOOOOOOO||XXXXXXX||OOOOOOOO|||?XXXXXXX||OOOOOOO||OOOOOOO??\n" //
				+ "?OOo|o|oO||Xx|o|xX||Xx|x|oXO|||OO.|.|oO||Xx|o|.X||Oo|o|xOO|||?Xx|o|xX||Ox|x|oO||O.|o|oO??\n" //
				+ "?OO-----O||X-----X||X-----XO|||OO-----O||X-----X||O-----OO|||?X-----X||O-----O||O-----O??\n" //
				+ "?OOo|x|.O||X.|x|xX||Xo|.|xXO|||OO.|x|oO||X.|x|xX||Oo|x|oOO|||?Xo|o|xX||Ox|o|oO||Ox|o|oO??\n" //
				+ "?OO-----O||X-----X||X-----XO|||OO-----O||X-----X||O-----OO|||?X-----X||O-----O||O-----O??\n" //
				+ "?OO.|x|xO||Xx|x|oX||Xx|x|xXO|||OO.|.|oO||Xx|.|xX||Oo|.|oOO|||?Xo|x|xX||Oo|o|xO||Oo|x|oO??\n" //
				+ "?OOOOOOOO||XXXXXXX||XXXXXXXO|||OOOOOOOO||XXXXXXX||OOOOOOOO|||?XXXXXXX||OOOOOOO||OOOOOOO??\n" //
				+ "?O-------------------------O|||O-------------------------O|||?-------------------------??\n" //
				+ "?O-------------------------O|||O-------------------------O|||?-------------------------??\n" //
				+ "?OXXXXXXX||OOOOOOO||OOOOOOOO|||O       ||OOOOOOO||XXXXXXXO|||????????||XXXXXXX||XXXXXXX??\n" //
				+ "?OXx|.|.X||Oo|o|oO||Oo|.|xOO|||O o|.|x ||Ox|.|.O||X.|.|xXO|||??o|o|x?||Xx|x|xX||X.|.|xX??\n" //
				+ "?OX-----X||O-----O||O-----OO|||O ----- ||O-----O||X-----XO|||??-----?||X-----X||X-----X??\n" //
				+ "?OX.|.|xX||Oo|x|oO||Oo|o|.OO|||O .|o|o ||O.|.|oO||X.|x|oXO|||??x|x|o?||Xx|o|.X||Xo|.|xX??\n" //
				+ "?OX-----X||O-----O||O-----OO|||O ----- ||O-----O||X-----XO|||??-----?||X-----X||X-----X??\n" //
				+ "?OXx|x|xX||Ox|x|.O||Oo|.|xOO|||O .|.|. ||Oo|o|oO||Xx|.|xXO|||??o|o|x?||Xx|.|.X||Xo|o|xX??\n" //
				+ "?OXXXXXXX||OOOOOOO||OOOOOOOO|||O       ||OOOOOOO||XXXXXXXO|||????????||XXXXXXX||XXXXXXX??\n" //
				+ "?O-------------------------O|||O-------------------------O|||?-------------------------??\n" //
				+ "?O-------------------------O|||O-------------------------O|||?-------------------------??\n" //
				+ "?OOOOOOOO||OOOOOOO||OOOOOOOO|||OOOOOOOO||XXXXXXX||XXXXXXXO|||?XXXXXXX||OOOOOOO||OOOOOOO??\n" //
				+ "?OO.|.|oO||O.|o|oO||Oo|x|oOO|||OOx|o|xO||X.|.|xX||Xx|x|.XO|||?Xo|x|xX||Ox|x|oO||Oo|o|.O??\n" //
				+ "?OO-----O||O-----O||O-----OO|||OO-----O||X-----X||X-----XO|||?X-----X||O-----O||O-----O??\n" //
				+ "?OOo|o|oO||Oo|o|.O||Oo|o|xOO|||OO.|o|oO||X.|x|xX||Xx|x|oXO|||?Xx|o|xX||Oo|o|xO||Ox|o|oO??\n" //
				+ "?OO-----O||O-----O||O-----OO|||OO-----O||X-----X||X-----XO|||?X-----X||O-----O||O-----O??\n" //
				+ "?OO.|.|.O||Oo|x|.O||Ox|x|oOO|||OO.|o|.O||Xx|.|.X||Xx|o|oXO|||?Xx|o|xX||Oo|x|xO||Ox|.|oO??\n" //
				+ "?OOOOOOOO||OOOOOOO||OOOOOOOO|||OOOOOOOO||XXXXXXX||XXXXXXXO|||?XXXXXXX||OOOOOOO||OOOOOOO??\n" //
				+ "?OOOOOOOOOOOOOOOOOOOOOOOOOOO|||OOOOOOOOOOOOOOOOOOOOOOOOOOO|||????????????????????????????\n" //
				+ "?????????????????????????????????????????????????????????????????????????????????????????\n" //
				+ "TOP.\n" //
			;

		assertEquals(expected, board.fieldAsPrintableString());
	}
}
