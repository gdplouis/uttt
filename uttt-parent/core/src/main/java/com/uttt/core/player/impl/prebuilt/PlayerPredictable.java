package com.uttt.core.player.impl.prebuilt;

import com.uttt.common.RepeatableRandom;
import com.uttt.core.board.Token;

public class PlayerPredictable extends PlayerRandom {

	public PlayerPredictable(Token token) {
		super(token, RepeatableRandom.create("PlayerPredictable", "<init>", token));
	}
}
