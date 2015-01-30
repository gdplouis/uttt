package com.uttt.common.game;

import org.apache.log4j.Logger;

import com.uttt.common.board.Board;
import com.uttt.common.board.Position;
import com.uttt.common.board.Token;

public interface Player {

	Token  getToken();
	Move   makeMove(Logger log, Board board, Position constraint);
}
