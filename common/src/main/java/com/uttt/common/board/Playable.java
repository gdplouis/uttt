package com.uttt.common.board;

public interface Playable {
	int      getHeight();
	Board    getTopBoard();
	Position getPosition();
	boolean  isTop();
	boolean  isBottom();
	boolean  isPlayable();
}
