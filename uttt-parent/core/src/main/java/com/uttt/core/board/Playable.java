package com.uttt.core.board;

/**
 * Within a UTTT {@code Board}, objects that implement {@code Playable} represent parts of the game board where
 * a player move <I>may</I> be made. As such, {@code Playable} objects either are {@code Board} objects, or are
 * objects that reference a (single) {@code Board}. Also, since a {@code Playable} always has a {@code Board}
 * associated with it, that {@code Board} must always have a {@code Position} (where a {@code null} position is
 * defined to mean the top-board of the game.
 *
 * <P>
 *
 * In general, a {cod Playable} object either is a {@code Board} or refers to one, <B>and</B> either is a {@code
 * Position} or has one. These are called the "associated board" and "associate position," respectively.
 *
 */
public interface Playable {
	int      getHeight();
	Board    getTopBoard();
	Position getPosition();
	boolean  isTop();
	boolean  isBottom();

	/**
	 * Return {@code true} iff the associated {@code Position} refers to an open token spot, or an open board.
	 * A board, in turn, is open iff no win has been acheived at its own field level <B>and</B> there is at least
	 * one (resursively) open position in its field.
	 */
	boolean  isPlayable();
}
