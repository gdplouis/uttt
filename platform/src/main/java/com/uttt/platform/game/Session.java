package com.uttt.platform.game;

import java.util.Objects;
import java.util.UUID;

import com.uttt.platform.game.Board.Result;

class Session {

	public enum State {
		EMPTY,
		WAITING_FOR_OTHER_PLAYER_TO_SIT,
		WAITING_FOR_PLAYER_TO_MOVE,
	}

	private final String id;
	private State state;

	private Player player1;
	private Player player2;
	private Board board;

	public Session() {
		id = UUID.randomUUID().toString();
		state = State.EMPTY;
	}

	public String getId() {
		return id;
	}

	public State getState() {
		return state;
	}

	public State addPlayer(Player player) {
		switch(state) {
		case EMPTY:
			player1 = player;
			state = State.WAITING_FOR_OTHER_PLAYER_TO_SIT;
			return state;
		case WAITING_FOR_OTHER_PLAYER_TO_SIT:
			player2 = player;
			state = State.WAITING_FOR_PLAYER_TO_MOVE;
			board = new Board();
			return state;
		default:
			throw new RuntimeException("Already seating two players");
		}
	}
	
	public Result addMove(String playerId, int spot) {
		return board.addMove(playerId, spot);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Session) {
			final Session s = (Session) o;
			return s.getId().equals(getId());
		}
		return false;
	}
}
