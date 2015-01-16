package com.uttt.common.board;

public interface Node {

	enum Status {
		OPEN,
		DRAW,
		WINNER_AAA(Token.PLAYER_AAA),
		WINNER_BBB(Token.PLAYER_BBB),
		;

		public final Token winner;

		Status(Token winner) {
			this.winner = winner;
		}

		Status() {
			this(null);
		}

		public boolean isPlayable() {
			return (this == OPEN);
		}
	}

	int      getHeight();
	Status   getStatus();

	default <T extends Node> T getSubNode(int x, int y, Class<T> typeClass) {
		throw new RuntimeException(typeClass.getName() + " nodes don't have sub-nodes");
	}

	default Node getSubNode(int x, int y) {
		return getSubNode(x, y, Node.class);
	}
}
