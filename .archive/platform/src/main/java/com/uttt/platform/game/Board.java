package com.uttt.platform.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Board {

	public enum SpaceState {
		X(1),
		O(-1),
		BLANK(0);

		private final int value;

		private SpaceState(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private final List<SpaceState> states;
	private final List<SpaceState> moves;

	Board() {
		states = new ArrayList<>(9);
		moves = new ArrayList<>(9);
		for (int i = 0; i < 9; i++) {
			states.add(SpaceState.BLANK);
		}
	}

	void addMove(int spot, SpaceState move) {
		states.set(spot, move);
		moves.add(move);
	}

	List<SpaceState> getMoves() {
		return Collections.unmodifiableList(moves);
	}

	List<SpaceState> getBoardState() {
		return Collections.unmodifiableList(states);
	}

	List<Integer> getRawBoardState() {
		final List<Integer> stateValues = new ArrayList<>(states.size());
		for (SpaceState state : states) {
			stateValues.add(state.getValue());
		}
		return stateValues;
	}
}
