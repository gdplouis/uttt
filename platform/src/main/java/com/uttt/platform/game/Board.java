package com.uttt.platform.game;

import java.util.HashMap;
import java.util.Map;

class Board {

	public enum Result {
		VALID_MOVE,
		INVALID_MOVE,
		WIN_1,
		WIN_2,
		TIE;
	}

	private final Map<Integer, String> board;
	private final Map<Integer, String> moves;

	public Board() {
		board = new HashMap<>(81);
		moves = new HashMap<>(81);
		for (int i = 0; i < 81; i++) {
			board.put(i, "");
		}
	}

	public Result addMove(String playerId, int spot) {
		if (!board.get(spot).isEmpty()) {
			return Result.INVALID_MOVE;
		}
		board.put(spot, playerId);
		moves.put(moves.size(), playerId);
				
		if (moves.size() < 81) {
			return Result.VALID_MOVE;
		}
		return Result.WIN_1;
	}
}
