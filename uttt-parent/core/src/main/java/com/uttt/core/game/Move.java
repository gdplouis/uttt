package com.uttt.core.game;

import com.uttt.common.ExUtil;
import com.uttt.common.UtttException;
import com.uttt.core.board.Board;
import com.uttt.core.board.Position;

/**
 * The {@code Move} class represents actions taken by {@code Player} agents. Because the official game board and
 * the player agent don't directly share the same board object, the move indication must be made without direct reference
 * to any board object instance.
 */
public class Move {

	public final int  row;
	public final int  col;
	public final Move subMove;

	Move(int row, int col, Move subMove) {
		this.row     = row;
		this.col     = col;
		this.subMove = subMove;
	}

	Move(int row, int col) {
		this(row, col, null);
	}

	public Move within(int thenRow, int thenCol) {
		return new Move(thenRow, thenCol, this);
	}

    public static Move newMove(int row, int col) {
        return new Move(row, col);
    }

	// ====================================================================================================

	/**
	 * Convert a {@code Move} sequence into a {@code Position}, relative to some nominal root-board.
	 *
	 * @param rootBoard
	 * @return
	 */
	public Position toPosition(final Board rootBoard) {

		Position rval = null;

		Board cursorBoard = rootBoard;
		for (Move cursor = this; cursor != null; cursor = cursor.subMove) {
			rval = cursorBoard.at(cursor.row, cursor.col);

			if (!cursorBoard.isBottom()) {
				cursorBoard = rval.derefBoard();
			} else {
				if (cursor.subMove != null) {
					throw ExUtil.create(UtttException.AlreadyAtBottom.class)
						.ident("<this>", this)
						.append("move too long for board")
						.build();
				}
			}
		}

		return rval;
	}

	// ====================================================================================================
	// Object overrides...

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("M");
		for (Move cursor = this; cursor != null; cursor = cursor.subMove) {
			sb.append('~');
			sb.append('(');
			sb.append(cursor.row);
			sb.append(',');
			sb.append(cursor.col);
			sb.append(')');
		}

		return sb.toString();
	}
}
