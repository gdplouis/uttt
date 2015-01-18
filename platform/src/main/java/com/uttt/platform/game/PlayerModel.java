package com.uttt.platform.game;

import com.uttt.common.board.Token;

class PlayerModel {

	private final Token token;

	public PlayerModel(Token token) {
		this.token = token;
	}

	public Token getToken() {
		return token;
	}
}
