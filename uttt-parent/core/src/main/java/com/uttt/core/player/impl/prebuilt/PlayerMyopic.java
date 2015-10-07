package com.uttt.core.player.impl.prebuilt;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.uttt.common.Foreachable;
import com.uttt.core.board.Board;
import com.uttt.core.board.Line;
import com.uttt.core.board.Node;
import com.uttt.core.board.Position;
import com.uttt.core.board.Token;
import com.uttt.core.game.Move;
import com.uttt.core.player.Player;

public class PlayerMyopic extends Player {

	public PlayerMyopic(Token token) {
		super(token);
	}

	@Override
	public Move makeMove(Logger log, Board topBoard, Position constraint) {
		Move rval = myopicMove("|", log, topBoard, constraint);
		return rval;
	}

	private Move myopicMove(String pfx, Logger log, Board subBoard, Position constraint) {

		log.trace(pfx + "myopicMove: " //
				+ "subBoard.getPosition()=[" + subBoard.getPosition() + "]; " //
				+ "constraint=["             + constraint             + "]; " //
				);

		final Move 	myMove;
		if (constraint != null) {
			Move subMove = myopicMove(pfx + "|", log, constraint.derefBoard(), null);

			for(Position cursorConstraint = constraint; cursorConstraint != null; cursorConstraint = cursorConstraint.getBoard().getPosition()) {
			    subMove = subMove.within(cursorConstraint.getRow(), cursorConstraint.getCol());
			}
			myMove = subMove;
		}
		else {
			final int boardSize = subBoard.getSize();

			// get all lines in the board

			List<Line> lines = new ArrayList<>(2+2*boardSize);

			for (final int i : Foreachable.until(boardSize)) {
				lines.add(new Line.Row(subBoard, i));
				lines.add(new Line.Col(subBoard, i));
			}
			lines.add(new Line.Dag(subBoard, true ));
			lines.add(new Line.Dag(subBoard, false));

			// evaluate each line, placing positions into overall buckets

			final Set<Position> bucketWinning = new LinkedHashSet<>();
			final Set<Position> bucketBlocker = new LinkedHashSet<>();
			final Set<Position> bucketNeutral = new LinkedHashSet<>();

			@SuppressWarnings("unchecked")
			final List<Position>[] bucketOpen = new LinkedList[boardSize + 1];
			for(int i : Foreachable.until(bucketOpen.length)){
				bucketOpen[i] = new LinkedList<>();
			}

			for (final Line line : lines) {

				if (line.getLineStatus() != Node.Status.OPEN) {
					continue;
				}
				// ++CONSTRAINT: line has at least one open position to play

				final List<Position> lineOpen = line.getPositionsByStatus(Node.Status.OPEN);
				final List<Position> lineDraw = line.getPositionsByStatus(Node.Status.DRAW);
				final List<Position> lineMine = line.getPositionsByStatus(token.getStatus());
				final List<Position> lineOppt = line.getPositionsByStatus(token.opponent().getStatus());

				final boolean hasDraw = !(lineDraw.isEmpty());
				final boolean hasMine = !(lineMine.isEmpty());
				final boolean hasOppt = !(lineOppt.isEmpty());

				if (hasDraw || (hasMine && hasOppt)) {
					bucketNeutral.addAll(lineOpen);
					continue;
				}
				// ++CONSTRAINT: line is still viable as winner for at least one player

				else if (lineOpen.size() == 1) {
					// one open position, either a win-next or must-block
					if (hasMine) {
						bucketWinning.addAll(lineOpen);
					} else {
						bucketBlocker.addAll(lineOpen);
					}
					continue;
				}
				// ++CONSTRAINT: line has at least two open positions, all others by a single player

				bucketOpen[lineOpen.size()].addAll(lineOpen);
			}

			// select from buckets, best choice first

			final Position myPlayPos;

			if (!bucketWinning.isEmpty()) {
				myPlayPos = bucketWinning.iterator().next();
			} else if (!bucketBlocker.isEmpty()){
				myPlayPos = bucketBlocker.iterator().next();
			} else {
				Position bestPlay = null;
				for (int i : Foreachable.until(bucketOpen.length)) {
					if (!bucketOpen[i].isEmpty()){
						bestPlay  = bucketOpen[i].iterator().next();
						break;
					}
				}

				myPlayPos = (bestPlay != null) ? bestPlay : bucketNeutral.iterator().next();
			}

			if (subBoard.isBottom()) {
			    myMove = Move.newMove(myPlayPos.getRow(), myPlayPos.getCol());
			} else {
			    myMove = myopicMove(pfx + "|", log, myPlayPos.derefBoard(), null).within(myPlayPos.getRow(), myPlayPos.getCol());
			}
		}
		log.trace(pfx //
				+ "myMove="         + myMove         + "; " //
				);

		log.trace(pfx + "/"); //
		return myMove;
	}
}