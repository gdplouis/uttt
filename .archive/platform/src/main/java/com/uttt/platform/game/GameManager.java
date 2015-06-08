package com.uttt.platform.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.uttt.platform.game.Board.SpaceState;
import com.uttt.platform.game.GameSession.GameState;
import com.uttt.platform.game.GameSession.MoveResult;

public class GameManager {

	private final static Logger log = Logger.getLogger(GameManager.class);

	private final Map<String, GameSession> games = new HashMap<>(10);

	public String addStartNewGame() {
		final GameSession session = new GameSession();
		log.info("Starting a new game: " + session.getId());
		games.put(session.getId(), session);
		return session.getId();
	}

	public GameState addPlayerToGame(String gameId, String appId) {
		log.info("Adding player " + appId + " to game " + gameId);
		try {
			return games.get(gameId).addPlayer(appId);
		} catch (Exception e) {
			log.error("Couldn't", e);
		}
		return games.get(gameId).getState();
	}

	public Set<String> currentWaitingGames() {
		final Set<String> currentWaitingGames = new HashSet<>();
		for (Entry<String, GameSession> entry : games.entrySet()) {
			if (entry.getValue().getState() == GameState.WAITING_FOR_OTHER_PLAYER_TO_SIT) {
				currentWaitingGames.add(entry.getKey());
			}
		}
		return currentWaitingGames;
	}

	public String getTurn(String gameId) {
		return games.get(gameId).getCurrentTurn();
	}

	public SpaceState getTurnPiece(String gameId) {
		return games.get(gameId).getCurrentPlayerPiece();
	}

	public MoveResult addMove(String gameId, String appId, int spot) {
		return games.get(gameId).addMove(appId, spot);
	}

	public List<Integer> getRawBoardState(String gameId) {
		return games.get(gameId).getRawBoardState();
	}

	public List<SpaceState> getMoves(String gameId) {
		return games.get(gameId).getMoves();
	}

	public String getFirstPlayer(String gameId) {
		return games.get(gameId).getPlayer(SpaceState.X);
	}

	public String getSecondPlayer(String gameId) {
		return games.get(gameId).getPlayer(SpaceState.O);
	}
}
