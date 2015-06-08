package com.uttt.platform.game;

import com.uttt.platform.game.Board.SpaceState;

class Player {

	private final SpaceState piece;

	public Player(SpaceState piece) {
		this.piece = piece;
	}

	public SpaceState getPiece() {
		return piece;
	}
}
