package com.uttt.core.player.impl.prebuilt;

import java.util.Random;

import com.uttt.core.board.Token;

public class PlayerUnpredictable extends PlayerRandom {

	public PlayerUnpredictable(Token token) {
		super(token, new Random());
	}
}
