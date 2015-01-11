package com.uttt.player.game;

import java.util.HashSet;
import java.util.Set;

public abstract class Player {

	private final Set<String> games = new HashSet<>();


	public void addNewGame(String gameId) {
		games.add(gameId);
	}

	public void removeGame(String gameId) {
		games.remove(gameId);
	}

	public abstract int makeMove(String gameId);
}
