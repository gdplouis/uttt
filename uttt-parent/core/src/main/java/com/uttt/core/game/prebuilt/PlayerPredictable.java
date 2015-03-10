package com.uttt.core.game.prebuilt;

import com.uttt.common.RepeatableRandom;
import com.uttt.core.board.Token;

public class PlayerPredictable extends PlayerRandom {

	public PlayerPredictable(Token token) {
		super(token, RepeatableRandom.create("NativeMethodAccessorImpl", "invoke0", token));
	}
}
