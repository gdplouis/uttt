package com.uttt.common.game;

import java.util.LinkedList;
import java.util.List;

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
		this.random = RepeatableRandom.create(token);
	}

	public static Player create(Token token) {
		Player rval = new PlayerRandom(token);
		return rval;
	}

	@Override
	public Token getToken() {
		return token;
	}

	private Move randomMove(String pfx, Board subBoard) {

//		System.out.println(pfx + "randomMove: subBoard.getPosition()=[" + subBoard.getPosition() + "]"); // TODO:DBG:

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

//		System.out.println(pfx // TODO:DBG:
//				+ "openPos.size()=" + openPos.size() + "; " //
//				+ "myPlayIdx="      + myPlayIdx      + "; " //
//				+ "myPlayPos="      + myPlayPos      + "; " //
//				);

		final Move subMove;
		if (subBoard.isBottom()) {
			subMove = null;
		} else {
			subMove = randomMove(pfx + "|", myPlayPos.derefBoard());
		}
		final Move 	myMove = new Move(myPlayPos.getRow(), myPlayPos.getCol(), subMove);
//		System.out.println(pfx // TODO:DBG:
//				+ "myMove="         + myMove         + "; " //
//				);

//		System.out.println(pfx + "/"); // TODO:DBG:
		return myMove;
	}

	@Override
	public Move makeMove(Board board) {
		Move   rval = randomMove("|", board);
		return rval;
	}
}
