package com.uttt.common.board;

import java.util.LinkedList;
import java.util.List;

import com.uttt.common.ArgCheck;
import com.uttt.common.Foreachable;

public final class Board implements Node {

	private final Position    position;
	private final int         height;
	private final int         size;
	private final Node[][]    field;

	private Node.Status status = Node.Status.OPEN;

	private Board(Position position, int height, int size) {
		ArgCheck.rangeClosed("height", height, 1, 3);
		ArgCheck.rangeClosed("size"  , size  , 2, 5);

		this.position  = position;
		this.height    = height;
		this.size      = size;

		this.field     = createField(this, height, size);
	}

	public Board(int height, int size) {
		this((Position) null, height,  size);
	}

	public Position getPosition() {
		return position;
	}

	public Board getParent() {
		return (position == null ? null : position.getBoard());
	}

	public boolean isTop() {
		return (position == null);
	}

	@Override
	public int getHeight() {
		return height;
	}

	public int getSize() {
		return size;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	protected void setStatus(Node.Status status) {
		this.status = status;
	}

	public boolean isPlayable() {
		return (status == Node.Status.OPEN) && ((position == null) || (position.getBoard().isPlayable()));
	}

	/**
	 * Returns the underlying field data structure. Only visible at package level for testing.
	 *
	 * @return
	 */
	/* pkg */ Node[][] getField() {
		return field;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Node> T getSubNode(int row, int col, Class<T> typeClass) {
		ArgCheck.rangeClosedOpen("x", row, 0, size);
		ArgCheck.rangeClosedOpen("y", col, 0, size);

		return (T) field[row][col];
	}

	public Board getSubBoard(int row, int col) {
		if (height <= 1) {
			throw new IllegalArgumentException("no sub-boards, at bottom board");
		}

		return getSubNode(row, col, Board.class);
	}

	public Token getSubToken(int row, int col) {
		if (height > 1) {
			throw new IllegalArgumentException("no sub-tokens, above bottom board");
		}

		return getSubNode(row, col, Token.class);
	}

	// ====================================================================================================

	private static Node[][] createField(Board board, int height, int size) {

		// create array of rows

		Node[][] rval = new Node[size][];
		for (int i  : Foreachable.until(size)) {
			rval[i] = new Node[size];
		}

		// either a field of tokens, or a field of sub-boards

		if (height == 1) {
			for (int row : Foreachable.until(size)) {
				for (int col : Foreachable.until(size)) {
					rval[row][col] = Token.EMPTY;
				}
			}
		} else {
			for (int row : Foreachable.until(size)) {
				for (int col : Foreachable.until(size)) {
					Position subPos   = new Position(board, row, col);
					Board    subBoard = new Board(subPos, (height - 1), size);

					rval[row][col] = subBoard;
				}
			}
		}

		return rval;
	}

	private boolean isWinner(Token token, int placedRow, int placedCol) {

		for (int col : Foreachable.to(size)) {
			if (col == size) {
				return true;
			}
			if (field[placedRow][col].getStatus() != token.getStatus())
				break;
		}

		for (int row : Foreachable.to(size)) {
			if (row == size) {
				return true;
			}
			if (field[row][placedCol].getStatus() != token.getStatus())
				break;
		}

		if (placedRow == placedCol) {
			for (int diag : Foreachable.to(size)) {
				if (diag == size) {
					return true;
				}
				if (field[diag][diag].getStatus() != token.getStatus())
					break;
			}
		}

		if (placedCol == (size - 1 - placedRow)) {
			for (int diag : Foreachable.to(size)) {
				if (diag == size) {
					return true;
				}
				if (field[(size - 1 - diag)][diag].getStatus() != token.getStatus())
					break;
			}
		}

		return false;
	}

	/* pkg */ void updatePosition(Token token, int myRow, int myCol) {
		if (height > 1) {
			throw new IllegalArgumentException("board height [" + height + "] too high (>1) to place a token");
		}

		// make sure this board lineage is playable

		for (Board lineage = this; lineage != null; lineage = lineage.getParent()) {
			if (!lineage.isPlayable()) {
				throw new IllegalArgumentException("board lineage is not playable");
			}
		}

		// make sure token location is available

		Token inPlace = getSubToken(myRow, myCol);
		if (inPlace != Token.EMPTY) {
			throw new IllegalArgumentException("token position already filled");
		}

		field[myRow][myCol] = token;

		// check win conditions, percolating towards top

		Board boardCheck = this;
		int   rowCheck   = myRow;
		int   colCheck   = myCol;

		while (boardCheck != null) {
			if (!boardCheck.isWinner(token, rowCheck, colCheck)) {
				break;
			}
			boardCheck.setStatus(token.getStatus());

			if (boardCheck.isTop()) {
				break;
			}

			rowCheck = boardCheck.getPosition().getRow();
			colCheck = boardCheck.getPosition().getCol();

			boardCheck = boardCheck.getParent();
		}
	}

	private char getPadChar() {
		switch (status) {
			case OPEN:       return ' ';
			case DRAW:       return '?';
			case WINNER_AAA: return 'X';
			case WINNER_BBB: return 'O';
			default:         return '*';
		}
	}
	private List<StringBuilder> fieldAsListOfStringBuilderForHeightOne() {
		final char padChar = getPadChar();

		// horizontal rule between rows is as wide as size plus vertical separators (2 * size - 1), with left/right padding

		final String hrule;
		{
			StringBuilder sb = new StringBuilder();
			sb.append(padChar);

			for (@SuppressWarnings("unused") int i : Foreachable.until(2 * size - 1))
				sb.append('-');

			sb.append(padChar);

			hrule = sb.toString();
		}

		// top/bottom padding is as wide as the hrule, but is all spaces

		final String topBotPad = hrule.toString().replace('-', padChar);

		// stringize board by rows by tokens, with left/right padding

		List<StringBuilder> myBuilders = new LinkedList<>();
		myBuilders.add((new StringBuilder()).append(topBotPad));

		for (int row : Foreachable.until(size)) {
			if (row > 0)
				myBuilders.add((new StringBuilder()).append(hrule));

			final StringBuilder rowSb = new StringBuilder();
			myBuilders.add(rowSb);

			rowSb.append(padChar);
			for (int col : Foreachable.until(size)) {
				if (col > 0)
					rowSb.append('|');

				Token token = getSubToken(row, col);
				rowSb.append(token.mark);
			}
			rowSb.append(padChar);
		}
		myBuilders.add((new StringBuilder()).append(topBotPad));

		return myBuilders;
	}

	private List<StringBuilder> fieldAsListOfStringBuilder() {
		if (height == 1) {
			return fieldAsListOfStringBuilderForHeightOne();
		}

		final char padChar = getPadChar();
		final List<StringBuilder> myBuilders = new LinkedList<>();

		String hrule = null; // deferred build out until first needed

		// build by row

		for (int row : Foreachable.until(size)) {
			if (row > 0) {
				if (hrule == null) {
					StringBuilder sb = new StringBuilder();

					sb.append(padChar); // left padding

					int width = myBuilders.get(0).length();
					for (@SuppressWarnings("unused") int i : Foreachable.until(1, (width - 1)))
						sb.append('-');

					sb.append(padChar); // right padding

					hrule = sb.toString();
				}

				for (@SuppressWarnings("unused") int i : Foreachable.until(height))
					myBuilders.add((new StringBuilder()).append(hrule));
			}

			List<StringBuilder> rowBuilders = getSubBoard(row, 0).fieldAsListOfStringBuilder();
			for (int col : Foreachable.until(1, size)) {
				List<StringBuilder> subBuilders = getSubBoard(row, col).fieldAsListOfStringBuilder();

				for (int line : Foreachable.until(rowBuilders.size())) {
					StringBuilder lineBuilder = rowBuilders.get(line);

					for (@SuppressWarnings("unused") int i : Foreachable.until(height))
						lineBuilder.append('|');

					lineBuilder.append(subBuilders.get(line));
				}
			}
			for (StringBuilder lineBuilder : rowBuilders)
				lineBuilder.insert(0, padChar).append(padChar); // left/right padding

			myBuilders.addAll(rowBuilders);
		}

		// top/bottom padding is as wide as the hrule, but is all spaces

		@SuppressWarnings("null")
		final String topBotPad = hrule.toString().replace('-', padChar);
		myBuilders.add(0, (new StringBuilder()).append(topBotPad));
		myBuilders.add((new StringBuilder()).append(topBotPad));

		return myBuilders;
	}

	public String fieldAsPrintableString() {

		final StringBuilder totalSb = new StringBuilder();
		final List<StringBuilder> sbs = fieldAsListOfStringBuilder();
		for (StringBuilder line : sbs) {
			totalSb.append(line).append('\n');
		}

		StringBuilder boardId = new StringBuilder();
		for (Position pos = getPosition(); pos != null; pos = pos.getBoard().getPosition()) {
			boardId.insert(0, "-(" + pos.getRow() + "," + pos.getCol() + ")");
		}
		boardId.insert(0, "TOP");

		totalSb.append(boardId).append(".\n");

		return totalSb.toString();
	}
}
