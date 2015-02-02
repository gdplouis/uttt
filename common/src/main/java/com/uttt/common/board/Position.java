package com.uttt.common.board;

import com.uttt.common.ArgCheck;

/**
 * A {@code Position} represents a (row,col) location within a specific board.
 *
 * <P>
 *
 * {@code Postion}'s are <B>immutable</B>, and are fully validated at construction (e.g. exception if null board,
 * row/col out of range).
 */
public final class Position implements Playable {

	private final Board board;
	private final int   row;
	private final int   col;

	public Position(Board board, int row, int col) {
		super();

		ArgCheck.notNull("board", board);
		ArgCheck.index("row", row, board.getSize());
		ArgCheck.index("col", col, board.getSize());

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

	/**
	 * Calculates how many levels deep the position is from the top-board. Positions within the top-board itself are
	 * always depth=1, while the bottom-boards are always depth=(top-board-height).
	 */
	public int depth() {
		Position parentPos = board.getPosition();
		return (parentPos == null) ? 1 : (1 + parentPos.depth());
	}

	/**
	 * Boilerplate reduction, allowing:
	 *
	 * <PRE>Type t = pos.deref(Type.class)</PRE>
	 *
	 * rather than:
	 *
	 * <PRE>Type t = pos.getBoard().getSubNode(pos.getRow(), pos.getCow(), Type.class)</PRE>
	 *
	 * @see #derefBoard
	 * @see #derefToken
	 */
	public <T extends Node> T deref(Class<T> typeClass) {
		return board.getSubNode(row, col, typeClass);
	}

	/**
	 * A bit of sugar, allowing:
	 *
	 * <PRE>Board b = pos.derefBoard()</PRE>
	 *
	 * rather than
	 *
	 * <PRE>Board b = pos.deref(Board.class)</PRE>
	 */
	public Board derefBoard() {
		return deref(Board.class);
	}

	/**
	 * A bit of sugar, allowing:
	 *
	 * <PRE>Token t = pos.derefToken()</PRE>
	 *
	 * rather than
	 *
	 * <PRE>Token t = pos.deref(Token.class)</PRE>
	 */
	public Token derefToken() {
		return deref(Token.class);
	}

	//  ====================================================================================================

	/**
	 * Return a newly constructed {@code Position} instance that refers to the (row,col) <I>within</I> the board this
	 * position refers to.
	 *
	 * @throws IllegalArgumentException
	 *             If this position is already at a bottom-board; If (row,col) fails range checking;
	 */
	public Position at(int subRow, int subCol) throws IllegalArgumentException {

		if (getHeight() <= 1) {
			throw new IllegalArgumentException("Position already at a bottom board, can't delve further: [" + this.toString() + "]");
		}

		return derefBoard().at(subRow, subCol);
	}

	/**
	 * Place the given {@code Token} on the location this position references. Such board must be a bottom-board (i.e.
	 * of height=1), and the board must be in a playable state (i.e. neither in a "win" state for either player, nor in
	 * a draw state).
	 *
	 * <P>
	 *
	 * Placing a token will result in a next-move-constraint, meaning some board position within the game which limits
	 * the freedom of the next play. The derivation of the next-move-constaint is influenced by the game's
	 * rules-variant, but generally follows a two phase process: first, the current move and current board state are
	 * combined to yeild a tentative bottom-board as constraint. Second, if that board is not playable (i.e. is already
	 * in a win or draw state), the constraint shifts up to the parent board. This
	 * "if not playable, then shift to parent" logic continues until a playable board is found, or until reaching the
	 * top-board. If at the top-board, then the constraint is considered "null" (and is, in fact, represented by a
	 * {@code null} value).
	 *
	 * @param t
	 *            The player token to place.
	 *
	 * @return The next-move-constraint, as a {@code Postion}.
	 *
	 * @throws IllegalArgumentException
	 *             When the board is has a height greater than 1 (i.e. is <I>not</I> a bottom-board); The position is
	 *             not actually playable, because either the token-spot is already taken, or the spot's lineage is not
	 *             playable.
	 */
	public Position place(Token t) throws IllegalArgumentException {

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

	/**
	 * Append a readable text representation of the position to the supplied {@code StringBuilder}.
	 *
	 * @param sb
	 *            The target of the text append
	 *
	 * @return The incoming {@code StringBuilder} is returned, almost like fluent style...
	 */
	public StringBuilder appendTo(StringBuilder sb) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : System.identityHashCode(board)); // consistent with object-instance equality
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this ==  obj) return true;
		if (obj  == null) return false;

		if (!(obj instanceof Position)) return false;
		Position that = (Position) obj;

		if (this.board != that.board)  return false; // YES - object instance comparison
		if (this.col   != that.col)    return false;
		if (this.row   != that.row)    return false;

		return true;
	}


}
