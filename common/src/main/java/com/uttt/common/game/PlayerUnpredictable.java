package com.uttt.common.game;

import java.util.Random;

import com.uttt.common.board.Token;

public class PlayerUnpredictable extends PlayerRandom {

	public PlayerUnpredictable(Token token) {
		super(token, new Random());
	}
}
