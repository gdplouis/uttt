package com.uttt.common.board;

import com.uttt.common.ArgCheck;

public final class Position implements Playable {

	private final Board board;
	private final int   row;
	private final int   col;

	public Position(Board board, int row, int col) {
		super();

		ArgCheck.notNull("board", board);
		ArgCheck.rangeClosedOpen("row", row, 0, board.getSize());
		ArgCheck.rangeClosedOpen("col", col, 0, board.getSize());

		this.row   = row;
		this.col   = col;
		this.board = board;
	}

	public Board getBoard() {
		return board;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override
	public int getHeight() {
		return board.getHeight();
	}

	@Override
	public boolean isTop() {
		return board.isTop();
	}

	@Override
	public boolean isBottom() {
		return board.isBottom();
	}
	@Override
	public Board getTopBoard() {
		return board.getTopBoard();
	}

	@Override
	public Position getPosition() {
		return this;
	}

	@Override
	public boolean isPlayable() {
		// first check own position

		Node.Status status = board.getSubNode(row, col).getStatus();
		if (!status.isPlayable()) {
			return false;
		}

		// now check all boards in lineage (own board, and all parents)

		for (Board lineage = board; lineage != null; lineage = lineage.getParent()) {
			if (!board.getStatus().isPlayable()) {
				return false;
			}
		}

		return true;
	}

	public int length() {
		Position parentPos = board.getPosition();
		return (parentPos == null) ? 1 : (1 + parentPos.length());
	}

	public <T extends Node> T deref(Class<T> typeClass) {
		return board.getSubNode(row, col, typeClass);
	}

	public Board derefBoard() {
		return deref(Board.class);
	}

	public Token derefToken() {
		return deref(Token.class);
	}

	//  ====================================================================================================

	public Position at(int subRow, int subCol) {

		if (getHeight() <= 1) {
			throw new IllegalArgumentException("Position already at a bottom board, can't delve further: [" + this.toString() + "]");
		}

		return derefBoard().at(subRow, subCol);
	}

	public Position place(Token t) {

		if (board.getHeight() > 1) {
			throw new IllegalArgumentException("can't place token: position [" + this.toString() + "]: remaining height = [" + board.getHeight() + "]");
		}

		if (!isPlayable()) {
			throw new IllegalArgumentException("not playable lineage: position [" + this.toString() + "]");
		}

		board.updatePosition(t, row, col);

		// determine constraint on next play (by opponent)

		final Position constraint;
		Board parentBoard = board.getParent();
		if (parentBoard == null) {
			constraint = null;
		} else {
			Position parentPos = new Position(parentBoard, row, col);

			while((parentPos != null) && !parentPos.isPlayable()) {
				parentPos = parentPos.getBoard().getPosition();
			}

			constraint = parentPos;
		}

		return constraint;
	}

	// ====================================================================================================
	// Object overrides...

	private StringBuilder appendTo(StringBuilder sb) {
		final Position upperPos = board.getPosition();
		if (upperPos == null) {
			sb.append("TOP").append("/h").append(board.getHeight()).append(":");
		} else {
			upperPos.appendTo(sb);
		}
		sb.append("~(").append(row).append(",").append(col).append(")");

		return sb;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		return appendTo(sb).toString();
	}
}
