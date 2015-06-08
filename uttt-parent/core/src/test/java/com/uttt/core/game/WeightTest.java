package com.uttt.core.game;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.uttt.common.StackFrameUtil;
import com.uttt.core.board.Board;
import com.uttt.core.board.Token;
import com.uttt.core.game.Move;
import com.uttt.core.player.Player;
import com.uttt.core.player.impl.prebuilt.PlayerWeight;

public class WeightTest {

	@Test
	public void firstMove() {
		final Board board = new Board(1, 3);
		final Player p = Player.create(PlayerWeight.class, Token.PLAYER_AAA);
		p.makeMove(StackFrameUtil.methodLogger(), board, null).toPosition(board).place(Token.PLAYER_AAA);

		final String expected = "" //
				+ "       \n" //
				+ " .|.|. \n" //
				+ " ----- \n" //
				+ " .|x|. \n" //
				+ " ----- \n" //
				+ " .|.|. \n" //
				+ "       \n" //
				+ "TOP.\n" //
		;
		assertEquals(expected, board.fieldAsPrintableString());
	}

	@Test
	public void lastCornerForTheWin() {
		final Board board = new Board(1, 3);
		new Move(1, 1).toPosition(board).place(Token.PLAYER_AAA);
		new Move(0, 0).toPosition(board).place(Token.PLAYER_AAA);
		new Move(0, 2).toPosition(board).place(Token.PLAYER_BBB);
		new Move(2, 0).toPosition(board).place(Token.PLAYER_BBB);
		final Player p = Player.create(PlayerWeight.class, Token.PLAYER_AAA);
		p.makeMove(StackFrameUtil.methodLogger(), board, null).toPosition(board).place(Token.PLAYER_AAA);

		final String expected = "" //
				+ "XXXXXXX\n" //
				+ "Xx|.|oX\n" //
				+ "X-----X\n" //
				+ "X.|x|.X\n" //
				+ "X-----X\n" //
				+ "Xo|.|xX\n" //
				+ "XXXXXXX\n" //
				+ "TOP.\n" //
		;
		assertEquals(expected, board.fieldAsPrintableString());
	}
}
