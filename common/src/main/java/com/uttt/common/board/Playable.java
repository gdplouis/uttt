package com.uttt.common.board;

public interface Playable {
	Board    getTopBoard();
	Position getPosition();
	boolean  isPlayable();
}
