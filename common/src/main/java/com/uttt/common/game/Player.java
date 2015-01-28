package com.uttt.common.game;

import com.uttt.common.board.Board;
import com.uttt.common.board.Token;

public interface Player {

	Token  getToken();
	Move   makeMove(Board board);
}
