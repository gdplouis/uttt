package com.uttt.common.game;

import java.util.Random;

import com.uttt.common.board.Token;

public class PlayerUnpredictable extends PlayerRandom {

	private PlayerUnpredictable(Token token) {
		super(token, new Random());
	}
	
	public static Player create(Token token) {
		return new PlayerUnpredictable(token);
	}
}
