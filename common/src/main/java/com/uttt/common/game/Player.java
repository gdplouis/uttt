package com.uttt.common.game;

import org.apache.log4j.Logger;

import com.uttt.common.board.Board;
import com.uttt.common.board.Position;
import com.uttt.common.board.Token;

public abstract class Player {

	private final Token token;

	public Player(Token token) {
		this.token = token;
	}

	public Token getToken() {
		return token;
	}

	/**
	 * Constructs a new player. Assumes the first available constructor of klass
	 * is public and requires one Token arg.
	 * 
	 * @param klass
	 *            the class of the Player implementation.
	 * @param token
	 *            the token.
	 * @return a new player
	 */
	public static Player create(Class<? extends Player> klass, Token token) {
		try {
			return (Player) klass.getConstructors()[0].newInstance(token);
		} catch (Exception e) {
			throw new RuntimeException("Tried to reflectively construct new player", e);
		}
	}

	public abstract Move makeMove(Logger log, Board board, Position constraint);
}
