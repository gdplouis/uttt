package com.uttt.common.board;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.uttt.common.ArgCheck;
import com.uttt.common.Foreachable;

/**
 * A board repesents an independent field of play, where that field is (A) square, and (B) where each position in that
 * field is either a token-spot, or itself a (subordinate) board. This creates a recursive parent<-*>child relationship,
 * with tokens at the base (the final child-level).
 *
 * <P>
 *
 * Further, some invariants are guaranteed for all valid boards: <UL>
 *
 * <LI>All boards reachable through this relationship must have the same (square) field size.
 *
 * <LI>Within each board field, all positions are either all {@code Token} values, or are all {@code Board} instances of
 * mutually equal height.
 *
 * </UL>
 *
 */
public final class Board implements Node, Playable {

	private final Position    position;
	private final int         height;
	private final int         size;

	private final int         playCountMax;
	private final Node[][]    field;

	private Node.Status status    = Node.Status.OPEN;
	private int         playCount = 0;

	private Board(Position position, int height, int size) {
		ArgCheck.rangeClosed("height", height, 1, 3);
		ArgCheck.rangeClosed("size"  , size  , 2, 5);

		this.position  = position;
		this.height    = height;
		this.size      = size;

		this.playCountMax = (size * size);
		this.field        = createField(this, height, size);
	}

	private Board(Board source, Board copyParent, boolean isShallow) {

		this.height        = source.height;
		this.size          = source.size;
		this.playCountMax  = source.playCountMax;

		if (isShallow) {
			this.position      = source.position;
			this.field         = source.field;
		} else {
			Position copyPos = null;
			if (copyParent != null) {
				copyPos = new Position(copyParent, source.position.getRow(), source.position.getCol());
			}

			Node[][] copyField = createFieldNulls(size);

			for(final int row : Foreachable.until(size)) {
				for (final int col : Foreachable.until(size)) {
					copyField[row][col] = source.field[row][col].copyDeep();
				}
			}

			this.position      = copyPos;
			this.field         = copyField;
		}

		this.playCount     = source.playCount;
		this.status        = source.status;
	}

	public Board(int height, int size) {
		this((Position) null, height,  size);
	}

	/**
	 * Get the {@link Position} of the board within its parent. If the board is the top-board, then
	 * return {@code null}.
	 */
	@Override
	public Position getPosition() {
		return position;
	}

	/**
	 * Get this board's parent board, or {@code null} if this is the top-board.
	 * @return
	 */
	public Board getParent() {
		return (position == null ? null : position.getBoard());
	}

	/**
	 * Return {@code true} iff this board has no parent, i.e. is the top-board.
	 */
	@Override
	public boolean isTop() {
		return (position == null);
	}

	/**
	 * Return {@code true} iff this board's positions are token-spots (not sub-boards), i.e. is a
	 * bottom-board where {@code Token} values can be placed.
	 */
	@Override
	public boolean isBottom() {
		return (height == 1);
	}

	/**
	 * Returns the height of a board, where a board-of-tokens is defined to have a height of 1,
	 * and where the height of all other boards is one-more-than the (uniform) height of the sub-boards in
	 * its field.
	 */
	@Override
	public int getHeight() {
		return height;
	}

	public int getSize() {
		return size;
	}

	public int getPlayCount() {
		return playCount;
	}

	public int getPlayCountMax() {
		return playCountMax;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	protected void setStatus(Node.Status status) {
		this.status = status;
	}

	@Override
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
		ArgCheck.index("row", row, size);
		ArgCheck.index("col", col, size);

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

	@Override
	public Board getTopBoard() {
		Board topBoard = this;
		while (!topBoard.isTop()) {
			topBoard = topBoard.getParent();
		}

		return topBoard;
	}

	/**
	 * A simple {@link Position} factory returning a position within this board, at the given row/col.
	 */
	public Position at(int row, int col) {
		return new Position(this, row, col);
	}

	/**
	 * Return a deep-copy (recursive clone) of this board.
	 */
	@Override
	public Board copyDeep() {
		return new Board(this, getParent(), false);
	}

	// ====================================================================================================

	private static Node[][] createFieldNulls(int size) {

		Node[][] rval = new Node[size][];
		for (int i  : Foreachable.until(size)) {
			rval[i] = new Node[size];
		}

		return rval;
	}

	private static Node[][] createField(Board board, int height, int size) {

		// create array of rows

		Node[][] rval = createFieldNulls(size);

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
			throw new IllegalArgumentException("board height=[" + height + "] too high (>1) to place a token");
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
		++playCount;

		// update status, percolating towards top

		Board boardCheck = this;
		int   rowCheck   = myRow;
		int   colCheck   = myCol;

		while (true) {
			final boolean isWon  = boardCheck.isWinner(token, rowCheck, colCheck);
			final boolean isDraw = (!isWon) && (boardCheck.playCount >= boardCheck.playCountMax);

			final Node.Status updatedStatus;

			if (isWon) {
				updatedStatus = token.getStatus();
			} else if (isDraw) {
				updatedStatus = Node.Status.DRAW;
			} else {
				updatedStatus = null;
			}

			if (updatedStatus == null) {
				break;
			}

			boardCheck.setStatus(updatedStatus);

			if (boardCheck.isTop()) {
				break;
			}

			rowCheck = boardCheck.getPosition().getRow();
			colCheck = boardCheck.getPosition().getCol();

			boardCheck = boardCheck.getParent();
			boardCheck.playCount += 1;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + playCount;
		result = prime * result + size;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + Arrays.hashCode(field);
		return result;
	}

	/**
	 * Return {@code true} iff value-wise equal. <B>NOTE:</B> The {@code position} of the boards within their parents
	 * is NOT considered part of the equality test, although any sub-boards of the two boards under comparison
	 * are compared in a same-position manner. Essentially, this means that the equality test is only sensitive to
	 * the {@code Token} layout of the bottom boards.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj ) return true;
		if (obj  == null) return false;

		if (!(obj instanceof Board)) return false;

		Board other = (Board) obj;
		if (height    != other.height   ) return false;
		if (playCount != other.playCount) return false;
		if (size      != other.size     ) return false;
		if (status    != other.status   ) return false;

		if (!Arrays.deepEquals(field, other.field)) // expensive predicate, keep last
			return false;

		return true;
	}
}
