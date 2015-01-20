package com.uttt.common.board;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.uttt.common.ArgCheck;

public interface PlayableTest {

	@Test public abstract void accessor_getTopBoard_h1s3();
	@Test public abstract void accessor_getTopBoard_h2s3();
	@Test public abstract void accessor_getTopBoard_h3s4();

	@Test public abstract void accessor_getPosition_h1s3();
	@Test public abstract void accessor_getPosition_h2s3();
	@Test public abstract void accessor_getPosition_h3s4();

	@Test public abstract void isPlayable_h1s3_win();
	@Test public abstract void isPlayable_h2s3_win();
	@Test public abstract void isPlayable_h3s4_win();

	@Test public abstract void isPlayable_h1s3_draw();
	@Test public abstract void isPlayable_h2s3_draw();
	@Test public abstract void isPlayable_h3s4_draw();

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

		public void checkAllClosed() {
			assertEquals("at end-of-game: playable.size(): ", 0, open.size());
		}

		public void place(Position position, Token token, Playable... newlyClosed) {
			final String pfx = token + " at " + position.asPrintable() + ": ";
			position.place(token);

			if ((newlyClosed != null) && (newlyClosed.length > 0)) {
				open  .removeAll(Arrays.asList(newlyClosed));
				closed.addAll   (Arrays.asList(newlyClosed));
				closed.remove   (null); // incase there's a null in [newlyClosed]
			}

			for (Playable item : open) {
				validate(pfx, item, false);
			}

			for (Playable item1 : closed) {
				validate(pfx, item1, true);
			}
		}
	}
}
