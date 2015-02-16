package com.uttt.common.game;

import static org.hamcrest.core.Is.is;

import org.junit.Assert;
import org.junit.Test;

import com.uttt.common.UtttException;
import com.uttt.common.board.Token;

public class GameTest {

	@Test(expected=UtttException.UnuniquePlayers.class)
	public void testUnuniquePlayers() {
		final Game game = new Game();
		game.initGame(
				Player.create(PlayerPredictable.class, Token.PLAYER_AAA),
				Player.create(PlayerPredictable.class, Token.PLAYER_AAA),
				1, 3);
	}

	@Test(expected=UtttException.UnuseableToken.class)
	public void testUnuseableToken() {
		final Game game = new Game();
		game.initGame(
				Player.create(PlayerPredictable.class, Token.EMPTY),
				Player.create(PlayerPredictable.class, Token.PLAYER_AAA),
				1, 3);
	}

	@Test
	public void testPlayerAAATurn() {
		final Game game = new Game();
		game.initGame(
				Player.create(PlayerPredictable.class, Token.PLAYER_AAA),
				Player.create(PlayerPredictable.class, Token.PLAYER_BBB),
				1, 3);
		Assert.assertThat(game.getStatus(), is(Game.GameStatus.PLAYER_AAA_TURN));
	}

	@Test
	public void testPlayerBBBTurn() {
		final Game game = new Game();
		game.initGame(
				Player.create(PlayerPredictable.class, Token.PLAYER_BBB),
				Player.create(PlayerPredictable.class, Token.PLAYER_AAA),
				1, 3);
		Assert.assertThat(game.getStatus(), is(Game.GameStatus.PLAYER_BBB_TURN));
	}

	@Test
	public void testGameTied() {
		final Game game = new Game();
		game.initGame(
				Player.create(PlayerPredictable.class, Token.PLAYER_AAA),
				Player.create(PlayerPredictable.class, Token.PLAYER_BBB),
				2, 3).play();
		Assert.assertThat(game.getStatus(), is(Game.GameStatus.GAME_ENDED_TIE));
	}

	@Test
	public void testGamePlayerAAAWins() {
		final Game game = new Game();
		game.initGame(
				Player.create(PlayerPredictable.class, Token.PLAYER_AAA),
				Player.create(PlayerPredictable.class, Token.PLAYER_BBB),
				1, 2).play();
		Assert.assertThat(game.getStatus(), is(Game.GameStatus.GAME_ENDED_PLAYER_AAA));
	}

	@Test
	public void testGamePlayerBBBWins() {
		final Game game = new Game();
		game.initGame(
				Player.create(PlayerPredictable.class, Token.PLAYER_BBB),
				Player.create(PlayerPredictable.class, Token.PLAYER_AAA),
				1, 2);
		game.play();
		Assert.assertThat(game.getStatus(), is(Game.GameStatus.GAME_ENDED_PLAYER_BBB));
	}
}
