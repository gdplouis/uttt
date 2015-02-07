package com.uttt.common.game;

import com.uttt.common.board.Board;

public interface Game {

	public enum GameStatus {
		PLAYER_AAA_TURN,
		PLAYER_BBB_TURN,
		GAME_ENDED_TIE,
		GAME_ENDED_PLAYER_AAA,
		GAME_ENDED_PLAYER_BBB,
	}
	
	Game initGame(Player playerAAA, Player playerBBB, int height, int size);
	void play();
	GameStatus getStatus();
	Board getBoard();
}
