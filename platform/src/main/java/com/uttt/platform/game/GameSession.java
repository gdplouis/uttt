package com.uttt.platform.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;

import com.uttt.platform.game.Board.SpaceState;

public class GameSession {

	public enum GameState {
		EMPTY,
		WAITING_FOR_OTHER_PLAYER_TO_SIT,
		WAITING_FOR_PLAYER_TO_MOVE,
	}

	public enum MoveResult {
		NOT_YOUR_TURN,
		INVALID_MOVE,
		VALID_MOVE,
		WIN_1,
		WIN_2,
		TIE;
	}

	private final String id;
	private GameState state;

	private Map<String, Player> players;

	private Board board;
	private SpaceState turn;

	public GameSession() {
		id = UUID.randomUUID().toString();
		state = GameState.EMPTY;
		players = new HashMap<>();
	}

	public String getId() {
		return id;
	}

	public GameState getState() {
		return state;
	}

	public String getCurrentTurn() {
		return getPlayer(turn);
	}

	public SpaceState getCurrentPlayerPiece() {
		return turn;
	}

	public SpaceState getPlayerPiece(String appId) {
		return players.get(appId).getPiece();
	}

	public String getPlayer(SpaceState piece) {
		for (Entry<String, Player> entry : players.entrySet()) {
			if (entry.getValue().getPiece() == piece) {
				return entry.getKey();
			}
		}
		throw new RuntimeException("No bueno...");
	}

	public List<Integer> getRawBoardState() {
		return board.getRawBoardState();
	}

	public List<SpaceState> getMoves() {
		return board.getMoves();
	}

	public GameState addPlayer(String appId) {
		switch(state) {
		case EMPTY:
			players.put(appId, new Player(SpaceState.X));
			state = GameState.WAITING_FOR_OTHER_PLAYER_TO_SIT;
			return state;
		case WAITING_FOR_OTHER_PLAYER_TO_SIT:
			players.put(appId, new Player(SpaceState.O));
			state = GameState.WAITING_FOR_PLAYER_TO_MOVE;
			board = new Board();
			turn = SpaceState.X;
			return state;
		default:
			throw new RuntimeException("Already seating two players");
		}
	}

	public MoveResult addMove(String playerId, int spot) {
		if (!getCurrentTurn().equals(playerId)) {
			return MoveResult.NOT_YOUR_TURN;
		}
		if (board.getBoardState().get(spot) != SpaceState.BLANK) {
			return MoveResult.INVALID_MOVE;
		}

		board.addMove(spot, players.get(playerId).getPiece());

		if (board.getMoves().size() < 9) {
			if (turn == SpaceState.X) {
				turn = SpaceState.O;
			} else {
				turn = SpaceState.X;
			}
			return MoveResult.VALID_MOVE;
		}
		if (turn == SpaceState.X) {
			return MoveResult.WIN_1;
		}
		return MoveResult.WIN_2;
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
