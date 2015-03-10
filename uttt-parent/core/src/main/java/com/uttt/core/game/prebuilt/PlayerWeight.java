package com.uttt.core.game.prebuilt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import com.uttt.common.Foreachable;
import com.uttt.core.board.Board;
import com.uttt.core.board.Position;
import com.uttt.core.board.Token;
import com.uttt.core.board.Node.Status;
import com.uttt.core.game.Move;
import com.uttt.core.player.Player;

public class PlayerWeight extends Player {

	private static Map<Integer, WeightsContainer> weights;

	static {
		weights = new HashMap<>();
		weights.put(2, WeightsContainer.create(2));
		weights.put(3, WeightsContainer.create(3));
		weights.put(4, WeightsContainer.create(4));
		weights.put(5, WeightsContainer.create(5));
	}

	public PlayerWeight(Token token) {
		super(token);
	}

	@Override
	public Move makeMove(Logger log, Board topBoard, Position constraint) {
		Move rval = weightedMove("|", log, topBoard, constraint);
		return rval;
	}

	private Move weightedMove(String pfx, Logger log, Board subBoard, Position constraint) {

		log.trace(pfx + "weightedMove: " //
				+ "subBoard.getPosition()=[" + subBoard.getPosition() + "]; " //
				+ "constraint=[" + constraint + "]; " //
		);

		final Move myMove;
		if (constraint != null) {
			Move subMove = weightedMove(pfx + "|", log, constraint.derefBoard(), null);

			for (Position cursorConstraint = constraint; cursorConstraint != null; cursorConstraint = cursorConstraint.getBoard().getPosition()) {
                subMove = subMove.within(cursorConstraint.getRow(), cursorConstraint.getCol());
			}
			myMove = subMove;
		} else {
			final List<Position> canditates = new ArrayList<>();
			for (final int row : Foreachable.until(subBoard.getSize())) {
				for (final int col : Foreachable.until(subBoard.getSize())) {
					final Position pos = subBoard.getSubNode(row, col).getStatus() == Status.OPEN ? subBoard.at(row, col) : null;
					if (pos == null) {
						continue;
					}
					final int[][] theseWeights = weights.get(subBoard.getSize()).weights;
					if (canditates.isEmpty() || theseWeights[pos.getRow()][pos.getCol()] == theseWeights[canditates.get(0).getRow()][canditates.get(0).getCol()]) {
						canditates.add(pos);
						continue;
					}
					if (theseWeights[pos.getRow()][pos.getCol()] > theseWeights[canditates.get(0).getRow()][canditates.get(0).getCol()]) {
						canditates.clear();
						canditates.add(pos);
						continue;
					}
				}
			}

			final Position myPlayPos = canditates.get(new Random().nextInt(canditates.size()));

			log.trace(pfx //
					+ "myPlayPos=" + myPlayPos + "; " //
			);

			if (subBoard.isBottom()) {
                myMove = Move.newMove(myPlayPos.getRow(), myPlayPos.getCol());
			} else {
                myMove = weightedMove(pfx + "|", log, myPlayPos.derefBoard(), null).within(myPlayPos.getRow(), myPlayPos.getCol());
			}
		}
		log.trace(pfx //
				+ "myMove=" + myMove + "; " //
		);

		log.trace(pfx + "/"); //
		return myMove;
	}

	static class WeightsContainer {
		int[][] weights;

		static WeightsContainer create(int size) {
			final WeightsContainer w = new WeightsContainer();
			w.weights = getWeights(size);
			return w;
		}

		static int[][] getWeights(int size) {
			// return Matrix.convolution2D(PlayerWeight1.Pascal.getMatX(size+2),
			switch (size) {
			case 2:
				return new int[][] { { 1, 1 }, { 1, 1 } };
			case 3:
				return new int[][] { { 2, 1, 2 }, { 1, 3, 1 }, { 2, 1, 2 } };
			case 4:
				return new int[][] { { 2, 1, 1, 2 }, { 1, 3, 3, 1 }, { 1, 3, 3, 1 }, { 2, 1, 1, 2 } };
			case 5:
				return new int[][] { { 3, 2, 1, 2, 3 }, { 2, 4, 2, 4, 2 }, { 1, 2, 5, 2, 1 }, { 2, 4, 2, 4, 2 }, { 3, 2, 1, 2, 3 } };
			default:
				throw new NotImplementedException();
			}
		}
	}

	public static void main(String[] args) {
		final int size = 5;
		final int[][] weights = WeightsContainer.getWeights(size);
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++) {
				System.out.print(weights[i][j] + " ");
			}
			System.out.println();
		}

	}
}