package com.uttt.platform.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;

import com.uttt.common.board.Board;
import com.uttt.common.board.Coordinates;
import com.uttt.common.board.Token;

public class GameSession {

	public enum GameState {
		EMPTY,
		WAITING_FOR_OTHER_PLAYER_TO_SIT,
		WAITING_FOR_PLAYER_TO_MOVE,
	}

	private final String id;
	private GameState state;

	private Map<String, PlayerModel> players;

	private Board board;
	private Token turn;

	public GameSession() {
		id = UUID.randomUUID().toString();
		state = GameState.EMPTY;
		players = new HashMap<>();
	}

	public String getId() {
		return id;
	}
	
	public Board getBoard() {
		return board;
	}

	public GameState getState() {
		return state;
	}

	public String getCurrentTurn() {
		return getPlayer(turn);
	}

	public Token getCurrentPlayerPiece() {
		return turn;
	}

	public Token getPlayerPiece(String appId) {
		return players.get(appId).getToken();
	}

	public String getPlayer(Token piece) {
		for (Entry<String, PlayerModel> entry : players.entrySet()) {
			if (entry.getValue().getToken() == piece) {
				return entry.getKey();
			}
		}
		throw new RuntimeException("No bueno...");
	}

	public GameState addPlayer(String appId) {
		switch(state) {
		case EMPTY:
			players.put(appId, new PlayerModel(Token.PLAYER_AAA));
			state = GameState.WAITING_FOR_OTHER_PLAYER_TO_SIT;
			return state;
		case WAITING_FOR_OTHER_PLAYER_TO_SIT:
			players.put(appId, new PlayerModel(Token.PLAYER_BBB));
			state = GameState.WAITING_FOR_PLAYER_TO_MOVE;
			board = new Board(2, 3);
			turn = Token.PLAYER_AAA;
			return state;
		default:
			throw new RuntimeException("Already seating two players");
		}
	}

	public Coordinates placeToken(String playerId, Coordinates coords) {
		return board.placeToken(players.get(playerId).getToken(), coords);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof GameSession) {
			final GameSession s = (GameSession) o;
			return s.getId().equals(getId());
		}
		return false;
	}
}
