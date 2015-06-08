package com.uttt.core.game;

public class Rules {

	/**
	 * TODO:SLINTZ+JAVADOC: com.uttt.common.game.Rules<HR><P><P>
	 * 
	 * In the following definitions, a "LINEAGE" is a
	 * sequence of positions from the board's top level all the way to one of it's sub-boards.
	 * The lineage of all positions
	 * cont. Thus, a height=1 board never has any lineage for any
	 * played spot. For a height=2 board, the lineage is always only a single
	 * top-board position referencing a specific bottom board.
	 * <P>
	 * For taller boards, the lineage would include a top-board position
	 * representing a mid-board, then a position in that mid-board representing
	 * a deeper mid-board, etc. until reaching a mid-board just above the bottom-
	 * boards.
	 * <P>
	 *
	 * <UL>
	 * <LI>{@code NONE} - A move places no constraint on the next move.
	 * <LI>{@code LINEAGE_GRAFT} - The constraint is.
	 * <LI>{@code LINEAGE_FLOAT} - .
	 * <LI>{@code DRILL_DOWN} - Use the move's row/col within the top-board to determine the
	 * constrained mid-board. Then, within that mid-board, further narrow the constraint by
	 * selecting the sub-board with the move's row/col. Continue narrowing the constraint until
	 * reaching a bottom-board.
	 * </UL>
	 *
	 * @author daddy
	 *
	 */
	enum Constraint {
		NONE,
		LINEAGE_GRAFT,
		LINEAGE_FLOAT,
		DRILL_DOWN,
	}

}
