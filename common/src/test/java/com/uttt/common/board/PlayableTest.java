package com.uttt.common.board;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.uttt.common.ArgCheck;
import com.uttt.common.Foreachable;

public interface PlayableTest {

	@Test public abstract void accessor_getHeight_h1s3();
	@Test public abstract void accessor_getHeight_h2s3();
	@Test public abstract void accessor_getHeight_h3s4();

	@Test public abstract void accessor_getTopBoard_h1s3();
	@Test public abstract void accessor_getTopBoard_h2s3();
	@Test public abstract void accessor_getTopBoard_h3s4();

	@Test public abstract void accessor_getPosition_h1s3();
	@Test public abstract void accessor_getPosition_h2s3();
	@Test public abstract void accessor_getPosition_h3s4();

	@Test public abstract void accessor_isTop_h1s3();
	@Test public abstract void accessor_isTop_h2s3();
	@Test public abstract void accessor_isTop_h3s4();

	@Test public abstract void accessor_isBottom_h1s3();
	@Test public abstract void accessor_isBottom_h2s3();
	@Test public abstract void accessor_isBottom_h3s4();

	@Test public abstract void accessor_isPlayable_h1s3_win();
	@Test public abstract void accessor_isPlayable_h2s3_win();
	@Test public abstract void accessor_isPlayable_h3s4_win();

	@Test public abstract void accessor_isPlayable_h1s3_draw();
	@Test public abstract void accessor_isPlayable_h2s3_draw();
	@Test public abstract void accessor_isPlayable_h3s4_draw();

	// ====================================================================================================

	public static class Validator {

		private final Set<Playable> open   = new LinkedHashSet<>();
		private final Set<Playable> closed = new LinkedHashSet<>();

		private final Board topBoard;

		Validator(Board board) {
			ArgCheck.notNull("topBoard", board);

			this.topBoard = board;
			this.open.add(board);

			validate("initialization: ", board, false);
		}

		public <T extends Playable> T  add(T item) {
			validate("add: ", item, false);
			open.add(item);
			return item;
		}

		public LinkedHashMap<String, Board> addAllFromTop() {
			final LinkedHashMap<String, Board> map = new LinkedHashMap<>();

			addRecurse(map, "t", topBoard);
			map.put("top", topBoard); // *** Add this AFTER recursive

			return map;
		}

		public void  checkClosures(String pfx, Playable... closures) {
			if ((closures != null) && (closures.length > 0)) {
				open  .removeAll(Arrays.asList(closures));
				closed.addAll   (Arrays.asList(closures));
				closed.remove   (null); // in case there's a null in [newlyClosed]
			}

			for (Playable item : open) {
				validate(pfx, item, false);
			}

			for (Playable item1 : closed) {
				validate(pfx, item1, true);
			}
		}

		public void checkAllClosed() {
			assertEquals("at end-of-game: playable.size(): ", 0, open.size());
		}

		public void place(Position position, Token token, Playable... closures) {
			final String pfx = token + " at " + position.asPrintable() + ": ";
			position.place(token);

			checkClosures(pfx, closures);
		}

		private <T extends Playable> T validate(String pfx, T item, boolean inverted) {
			if (item.isPlayable() ^ !inverted) {
				Position position = item.getPosition();
				String posString  = (position == null ? "TOP." : position.asPrintable());
				String msg = pfx + "item is " + (inverted ? "still" : "not") + " playable: " + posString + "\n"
						+ topBoard.fieldAsPrintableString();
				org.junit.Assert.fail(msg);
			}

			return item;
		}

		private void addRecurse(LinkedHashMap<String, Board> map, String pfx, Board board) {
			if (board.getHeight() <= 1)
				return;

			for(final int row : Foreachable.until(board.getSize())) {
				for(final int col : Foreachable.until(board.getSize())) {
					String id       = pfx + row + col;
					Board  subBoard = board.getSubBoard(row, col);

					addRecurse(map, (id + "s"), subBoard);
					map.put(id, subBoard); // *** Add parent board AFTER recursively adding sub-boards
				}
			}
		}
	}
}
