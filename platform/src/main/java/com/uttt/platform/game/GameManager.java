package com.uttt.platform.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.uttt.platform.game.Session.State;

public class GameManager {

	private final static Logger log = Logger.getLogger(GameManager.class);

	private final Map<String, Session> games = new HashMap<>(10);

	public String addStartNewGame() {
		final Session session = new Session();
		log.info("Starting a new game: " + session.getId());
		games.put(session.getId(), session);
		return session.getId();
	}

	public State addPlayerToGame(String gameId, String playerId) {
		log.info("Adding player " + playerId + "to game " + gameId);
		try {
			return games.get(gameId).addPlayer(new Player(playerId));
		} catch (Exception e) {
			log.error("Couldn't", e);
		}
		return games.get(gameId).getState();
	}

	public Set<String> currentWaitingGames() {
		final Set<String> currentWaitingGames = new HashSet<>();
		for (Entry<String, Session> entry : games.entrySet()) {
			if (entry.getValue().getState() == State.WAITING_FOR_OTHER_PLAYER_TO_SIT) {
				currentWaitingGames.add(entry.getKey());
			}
		}
		return currentWaitingGames;
	}
}
