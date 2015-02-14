package com.uttt.common.game;

import com.uttt.common.RepeatableRandom;
import com.uttt.common.board.Token;

public class PlayerPredictable extends PlayerRandom {

	private PlayerPredictable(Token token) {
		super(token, RepeatableRandom.create(2, token));
	}
	
	public static Player create(Token token) {
		return new PlayerPredictable(token);
	}
}
