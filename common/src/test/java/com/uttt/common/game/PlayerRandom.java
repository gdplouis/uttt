package com.uttt.common.game;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.uttt.common.Foreachable;
import com.uttt.common.board.Board;
import com.uttt.common.board.Node;
import com.uttt.common.board.Node.Status;
import com.uttt.common.board.Position;
import com.uttt.common.board.Token;
import com.uttt.common.testutil.RepeatableRandom;

public class PlayerRandom implements Player {

	private final Token            token;
	private final RepeatableRandom random;

	private PlayerRandom(Token token) {
		this.token  = token;
		this.random = RepeatableRandom.create(1, token);
	}

	public static Player create(Token token) {
		Player rval = new PlayerRandom(token);
		return rval;
	}

	@Override
	public Token getToken() {
		return token;
	}

	private Move randomMove(String pfx, Logger log, Board subBoard, Position constraint) {

		log.trace(pfx + "randomMove: " //
				+ "subBoard.getPosition()=[" + subBoard.getPosition() + "]; " //
				+ "constraint=["             + constraint             + "]; " //
				);

		final Move 	myMove;
		if (constraint != null) {
			Move subMove = randomMove(pfx + "|", log, constraint.derefBoard(), null);

			for(Position cursorConstraint = constraint; cursorConstraint != null; cursorConstraint = cursorConstraint.getBoard().getPosition()) {
				subMove = new Move(cursorConstraint.getRow(), cursorConstraint.getCol(), subMove);
			}
			myMove = subMove;
		} else {
			final List<Position> openPos = new LinkedList<>();
			for (final int row : Foreachable.until(subBoard.getSize())) {
				for (final int col : Foreachable.until(subBoard.getSize())) {
					Node node = subBoard.getSubNode(row, col);
					if (node.getStatus() == Status.OPEN) {
						openPos.add(subBoard.at(row, col));
					}
				}
			}

			int      myPlayIdx = random.nextInt(openPos.size());
			Position myPlayPos = openPos.get(myPlayIdx);

			log.trace(pfx //
					+ "openPos.size()=" + openPos.size() + "; " //
					+ "myPlayIdx="      + myPlayIdx      + "; " //
					+ "myPlayPos="      + myPlayPos      + "; " //
					);

			final Move subMove;
			if (subBoard.isBottom()) {
				subMove = null;
			} else {
				subMove = randomMove(pfx + "|", log, myPlayPos.derefBoard(), null);
			}
			myMove = new Move(myPlayPos.getRow(), myPlayPos.getCol(), subMove);
		}
		log.trace(pfx //
				+ "myMove="         + myMove         + "; " //
				);

		log.trace(pfx + "/"); //
		return myMove;
	}

	@Override
	public Move makeMove(Logger log, Board topBoard, Position constraint) {

		Move   rval = randomMove("|", log, topBoard, constraint);
		return rval;
	}
}
