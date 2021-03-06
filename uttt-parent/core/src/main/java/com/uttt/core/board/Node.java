package com.uttt.core.board;

import com.uttt.common.ExUtil;

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

	/**
	 * Accessor to retrieve the (row,col) element into this {@code Node}'s field grid.
	 *
	 * @param row The row index into the {@code Node}'s field grid.
	 * @param col The column index into the {@code Node}'s field grid.
	 * @param typeClass
	 *
	 * @throws RuntimeException If the concrete implementation doesn't have a field grid, i.e. is a {@code Token}, rather
	 * than a {@code Board}.
	 */
	default <T extends Node> T getSubNode(int row, int col, Class<T> typeClass) {
		throw ExUtil.create(RuntimeException.class)
			.ident("Node subclass", this.getClass())
			.append("this class doesn't have sub-nodes")
			.build();
	}

	default Node getSubNode(int x, int y) {
		return getSubNode(x, y, Node.class);
	}

	Node copyDeep();

}
