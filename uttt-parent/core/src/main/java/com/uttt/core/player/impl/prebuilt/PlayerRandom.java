package com.uttt.core.player.impl.prebuilt;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.uttt.common.Foreachable;
import com.uttt.core.board.Board;
import com.uttt.core.board.Node;
import com.uttt.core.board.Position;
import com.uttt.core.board.Token;
import com.uttt.core.board.Node.Status;
import com.uttt.core.game.Move;
import com.uttt.core.player.Player;

public abstract class PlayerRandom extends Player {

	private final Random random;

	protected PlayerRandom(Token token, Random random) {
		super(token);
		this.random = random;
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
			    subMove = subMove.within(cursorConstraint.getRow(), cursorConstraint.getCol());
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

			if (subBoard.isBottom()) {
			    myMove = Move.newMove(myPlayPos.getRow(), myPlayPos.getCol());
			} else {
			    myMove = randomMove(pfx + "|", log, myPlayPos.derefBoard(), null).within(myPlayPos.getRow(), myPlayPos.getCol());
			}
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
