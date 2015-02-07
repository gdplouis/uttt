package com.uttt.common.game;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import com.uttt.common.ExUtil;
import com.uttt.common.StackFrameUtil;
import com.uttt.common.UtttException;
import com.uttt.common.board.Board;
import com.uttt.common.board.Position;
import com.uttt.common.board.Token;

public class GameImpl implements Game {

	private Pair<Player, Player> players;
	private Player currentPlayer;
	private Board board;
	private Logger log;
	private GameStatus status;

	@Override
	public Game initGame(Player playerAAA, Player playerBBB, int height, int size) {
		
		if (playerAAA.getToken() == playerBBB.getToken()) {
			throw ExUtil.create(UtttException.UnuniquePlayers.class)
					.append(String.format("found two players with '%s' token", playerAAA.getToken()))
					.build();
		}		
		if (playerAAA.getToken() == Token.EMPTY) {
			throw ExUtil.create(UtttException.UnuseableToken.class)
			.append(String.format("player AAA is using '%s' token", playerAAA.getToken()))
			.build();
		}
		if (playerBBB.getToken() == Token.EMPTY) {
			throw ExUtil.create(UtttException.UnuseableToken.class)
			.append(String.format("player BBB is using '%s' token", playerBBB.getToken()))
			.build();
		}	
		
		players = Pair.of(playerAAA, playerBBB);
		board = new Board(height, size);
		log = StackFrameUtil.methodLogger();
		switchTurn();
		
		return this;
	}

	@Override
	public void play() {

		Position constraint = null;
		
		while (true) {

			final Move move = currentPlayer.makeMove(log, board.copyDeep(), constraint);
			final Position position = move.toPosition(board);
			constraint = position.place(currentPlayer.getToken());

			log.trace("after placing:\n" + board.fieldAsPrintableString());

			if (!board.isPlayable()) {
				break;
			}
			
			switchTurn();
		}

		switch (board.getStatus()) {
		case DRAW:
			status = GameStatus.GAME_ENDED_TIE;
			break;
		case WINNER_AAA:
			status = GameStatus.GAME_ENDED_PLAYER_AAA;
			break;
		case WINNER_BBB:
			status = GameStatus.GAME_ENDED_PLAYER_BBB;
			break;
		default:
			throw new RuntimeException("Unexpected game status '"
					+ board.getStatus() + "' when game supposedly ended");
		}
	}

	@Override
	public GameStatus getStatus() {
		return status;
	}

	@Override
	public Board getBoard() {
		return board.copyDeep();
	}

	private void switchTurn() {
		currentPlayer = currentPlayer == null || currentPlayer == players.getRight() ? players.getLeft() : players.getRight();
		status = currentPlayer.getToken() == Token.PLAYER_AAA ? GameStatus.PLAYER_AAA_TURN : GameStatus.PLAYER_BBB_TURN;
	}

	public static void main(String[] args) {

		final Game game = new GameImpl();
		game.initGame(
				PlayerUnpredictable.create(Token.PLAYER_AAA),
				PlayerUnpredictable.create(Token.PLAYER_BBB),
				2, 3).play();

		final Logger log = StackFrameUtil.methodLogger();

		switch (game.getStatus()) {
		case GAME_ENDED_TIE:
			log.info("Game Tied");
			break;
		case GAME_ENDED_PLAYER_AAA:
			log.info("Player X Wins!");
			break;
		case GAME_ENDED_PLAYER_BBB:
			log.info("Player O Wins!");
			break;
		default:
			break;
		}

		log.info(game.getBoard().fieldAsPrintableString());
	}
}
