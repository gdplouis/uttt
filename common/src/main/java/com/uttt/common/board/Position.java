package com.uttt.common.board;

import com.uttt.common.ArgCheck;

public final class Position {

	private final Board board;
	private final int   row;
	private final int   col;

	public Position(Board board, int row, int col) {
		super();

		ArgCheck.notNull("board", board);
		ArgCheck.rangeClosedOpen("row", row, 0, board.getSize());
		ArgCheck.rangeClosedOpen("col", col, 0, board.getSize());

		this.row = row;
		this.col = col;
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

	public Position placeToken(Token t) {

		if (board.getHeight() > 1) {
			throw new IllegalArgumentException("position [" + asPrintable() + "]: not playable, height = [" + board.getHeight() + "]");
		}

		if (!isPlayable()) {
			throw new IllegalArgumentException("position [" + asPrintable() + "]: not playable, status = [" + board.getSubNode(row, col).getStatus() + "]");
		}

		board.updatePosition(t, row, col);

		return null;
	}

	//  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	private StringBuilder appendTo(StringBuilder sb) {
		final Position upperPos = board.getPosition();
		if (upperPos == null) {
			sb.append("TOP").append("(h").append(board.getHeight()).append(")");
		} else {
			upperPos.appendTo(sb);
		}
		sb.append("~(").append(row).append(",").append(col).append(")");

		return sb;
	}

	public String asPrintable() {
		StringBuilder sb = new StringBuilder();

		return appendTo(sb).toString();
	}
}
