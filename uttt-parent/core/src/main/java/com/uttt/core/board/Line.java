package com.uttt.core.board;

import java.util.LinkedList;
import java.util.List;

import com.uttt.common.ArgCheck;
import com.uttt.common.Foreachable;

public class Line {

	private final Board            board;
	private final List<Position>   positions;
	private final List<Position>[] byStatus;
	private final Node.Status      lineStatus;

	@SuppressWarnings("unchecked")
	protected Line(Board board, List<Position> positions) {
		this.board     = board;
		this.positions = positions;
		this.byStatus  = new List[Node.Status.values().length];

		for(Node.Status status : Node.Status.values()) {
			byStatus[status.ordinal()] = new LinkedList<>();
		}

		for (Position position : positions) {
			byStatus[position.deref(Node.class).getStatus().ordinal()].add(position);
		}

	    if (byStatus[Node.Status.OPEN.ordinal()].size() > 0) {
			lineStatus = Node.Status.OPEN;
		}
	    else if (positions.size() == byStatus[Node.Status.WINNER_AAA.ordinal()].size()) {
			lineStatus = Node.Status.WINNER_AAA;
		}
	    else if (positions.size() == byStatus[Node.Status.WINNER_BBB.ordinal()].size()) {
			lineStatus = Node.Status.WINNER_BBB;
		}
	    else {
			lineStatus = Node.Status.DRAW;
	    }
	}

	public final Board getBoard() {
		return board;
	}

	public final List<Position> getPositions() {
		return positions;
	}

	public final List<Position> getPositionsByStatus(Node.Status status) {
		return byStatus[status.ordinal()];
	}

	public final Node.Status getLineStatus() {
		return lineStatus;
	}

	//------------------------------------------------------------------------------------------------------------------

	public static class Row extends Line {

		private final int row;

		public Row(Board board, int row) {
			super(board, extraction(board, row));

			this.row = row;
		}

		public final int getRow() {
			return row;
		}

		private static List<Position> extraction(Board board, int row) {
			ArgCheck.index("row", row, board.getSize(), "board.size");

			List<Position> positions = new LinkedList<>();

			for(final int col : Foreachable.until(board.getSize())) {
				positions.add(new Position(board, row, col));
			}

			return positions;
		}
	}

	public static class Col extends Line {

		private final int col;

		public Col(Board board, int col) {
			super(board, extraction(board, col));

			this.col = col;
		}

		public final int getCol() {
			return col;
		}

		private static List<Position> extraction(Board board, int col) {
			ArgCheck.index("col", col, board.getSize(), "board.size");

			List<Position> positions = new LinkedList<>();

			for(final int row : Foreachable.until(board.getSize())) {
				positions.add(new Position(board, row, col));
			}

			return positions;
		}
	}

	public static class Dag extends Line {

		private final boolean forward;

		public Dag(Board board, boolean forward) {
			super(board, extraction(board, forward));

			this.forward = forward;
		}

		public final boolean isForward() {
			return forward;
		}

		private static List<Position> extraction(Board board, boolean forward) {
			final int size = board.getSize();

			List<Position> positions = new LinkedList<>();

			for(final int row : Foreachable.until(size)) {
				final int col = forward ? row : (size - 1 - row);

				positions.add(new Position(board, row, col));
			}

			return positions;
		}
	}

}
