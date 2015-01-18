package com.uttt.common.player;

import com.uttt.common.board.Board;
import com.uttt.common.board.Coordinates;
import com.uttt.common.board.Token;

public interface Player {
	Coordinates makeMove(Token iAm, Board board, Coordinates coords);
}
