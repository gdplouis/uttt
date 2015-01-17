package com.uttt.common.board;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenTest extends NodeTest {

	@Test(expected=RuntimeException.class)
	@Override
	public void accessors_getSubNode() {
		Token.EMPTY.getSubNode(99, 99);
	}

	@Override
	public void accessors_getHeight() {
		for (Token t : Token.values()) {
			assertEquals("Token." + t + ".getHeight()", 0, t.getHeight());
		}
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	@Test()
	public void allMarksHaveLengthOne() {
		for (Token t : Token.values()) {
			assertEquals("Token." + t + ".mark.length()", 1, t.mark.length());
		}
	}

	@Test()
	public void status_playersWinSelf() {
		assertEquals("Token.PLAYER_AAA.getStatus(): ", Node.Status.WINNER_AAA, Token.PLAYER_AAA.getStatus());
		assertEquals("Token.PLAYER_BBB.getStatus(): ", Node.Status.WINNER_BBB, Token.PLAYER_BBB.getStatus());
	}

	@Test()
	public void status_emptyIsOpen() {
		assertEquals("Token.EMPTY.getStatus(): ", Node.Status.OPEN, Token.EMPTY.getStatus());
	}
}
