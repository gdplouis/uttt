package com.uttt.platform;

import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import com.uttt.common.App;
import com.uttt.common.Message;
import com.uttt.common.MessageType;
import com.uttt.common.Receiver;
import com.uttt.platform.game.GameManager;
import com.uttt.platform.game.GameSession.GameState;
import com.uttt.platform.game.GameSession.MoveResult;

public class PlatformApp extends App {

	private final static Logger log = Logger.getLogger(PlatformApp.class);

	@Autowired
	private AnnotationConfigApplicationContext context;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	private GameManager gameManager = new GameManager();

	@Bean
	@Override
	protected Receiver receiver() {
		return new Receiver(getErrorHandler()) {

			@Override
			public void onReceive(Message message) throws Exception {
				final MessageType type = message.getMessageType();

				switch(type) {
				case PLAYER_TEST_REQUEST:
					log.info("Got this: " + message);
					sendMessage(message.getSrc(), MessageType.PLAYER_TEST_RESPONSE, "Hey Player");
					break;

				case PLATFORM_DISCOVERY_REQUEST:
					log.info(message.getSrc() + ": Looking for us");
					sendMessage(message.getSrc(), MessageType.PLATFORM_DISCOVERY_RESPONSE, "Hello");
					break;

				case GET_OPEN_GAMES_REQUEST:
					log.info(message.getSrc() + ": Wants to retrieve any open games");
					final Set<String> openGames = gameManager.currentWaitingGames();
					final JSONArray openGamesArray = new JSONArray(openGames.toArray(new String[]{}));
					final JSONObject openGameIdsJson = new JSONObject();
					openGameIdsJson.put("open_game_ids", openGamesArray);
					sendMessage(message.getSrc(), MessageType.GET_OPEN_GAMES_RESPONSE, openGameIdsJson.toString());
					break;

				case START_NEW_GAME_REQUEST:
					log.info(message.getSrc() + ": Starting new game");
					final String newGameId = gameManager.addStartNewGame();
					gameManager.addPlayerToGame(newGameId, message.getSrc());
					final JSONObject newGameIdJson = new JSONObject();
					newGameIdJson.put("game_id", newGameId);
					sendMessage(message.getSrc(), MessageType.START_NEW_GAME_RESPONSE, newGameIdJson.toString());
					break;

				case JOIN_GAME_REQUEST:
					final String existingGameId = getBody(message).getString("game_id");
					log.info(message.getSrc() + ": Joining game " + existingGameId);
					final GameState gameState = gameManager.addPlayerToGame(existingGameId, message.getSrc());
					final JSONObject resultJson = new JSONObject();
					resultJson.put("result", gameState.name());
					resultJson.put("game_id", existingGameId);
					sendMessage(message.getSrc(), MessageType.JOIN_GAME_RESPONSE, resultJson.toString());
					if (gameState == GameState.WAITING_FOR_PLAYER_TO_MOVE) {
						final String turnAppId = gameManager.getTurn(existingGameId);
						final JSONObject yourTurnJson = new JSONObject();
						final JSONArray state = new JSONArray(gameManager.getRawBoardState(existingGameId).toArray(new Integer[]{}));
						yourTurnJson.put("i_am", gameManager.getTurnPiece(existingGameId).getValue());
						yourTurnJson.put("game_state", state);
						yourTurnJson.put("game_id", existingGameId);
						sendMessage(turnAppId, MessageType.GET_MOVE_REQUEST, yourTurnJson.toString());
					}
					break;

				case GET_MOVE_REPONSE:
					final int move = getBody(message).getInt("move");
					final String playingGameId = getBody(message).getString("game_id");
					log.info(message.getSrc() + " is try to move to " + move);
					final MoveResult moveResult = gameManager.addMove(playingGameId, message.getSrc(), move);
					final JSONArray state = new JSONArray(gameManager.getRawBoardState(playingGameId).toArray(new Integer[]{}));
					if (moveResult == MoveResult.VALID_MOVE) {
						final String turnAppId = gameManager.getTurn(playingGameId);
						final JSONObject yourTurnJson = new JSONObject();
						yourTurnJson.put("i_am", gameManager.getTurnPiece(playingGameId).getValue());
						yourTurnJson.put("game_state", state);
						yourTurnJson.put("game_id", playingGameId);
						sendMessage(turnAppId, MessageType.GET_MOVE_REQUEST, yourTurnJson.toString());
					} else if (moveResult ==  MoveResult.INVALID_MOVE || moveResult ==  MoveResult.NOT_YOUR_TURN) {
						throw new RuntimeException("Whoa, move result=" + moveResult.name());
					} else if (moveResult == MoveResult.WIN_1) {
						log.info("PLAYER 1 WON");
						final JSONObject player1WonFinalGameStateJson = new JSONObject();
						player1WonFinalGameStateJson.put("game_state", state);
						sendMessage(gameManager.getFirstPlayer(playingGameId), MessageType.WINNER_REQUEST, player1WonFinalGameStateJson.toString());
						sendMessage(gameManager.getSecondPlayer(playingGameId), MessageType.LOSER_REQUEST, player1WonFinalGameStateJson.toString());
					} else if (moveResult == MoveResult.WIN_2) {
						log.info("PLAYER 2 WON");
						final JSONObject player2WonFinalGameStateJson = new JSONObject();
						player2WonFinalGameStateJson.put("game_state", state);
						sendMessage(gameManager.getSecondPlayer(playingGameId), MessageType.WINNER_REQUEST, player2WonFinalGameStateJson.toString());
						sendMessage(gameManager.getFirstPlayer(playingGameId), MessageType.LOSER_REQUEST, player2WonFinalGameStateJson.toString());
					} else {
						log.info("TIE");
						final JSONObject playersTiedFinalGameStateJson = new JSONObject();
						playersTiedFinalGameStateJson.put("game_state", state);
						sendMessage(gameManager.getFirstPlayer(playingGameId), MessageType.TIE_REQUEST, playersTiedFinalGameStateJson.toString());
						sendMessage(gameManager.getSecondPlayer(playingGameId), MessageType.TIE_REQUEST, playersTiedFinalGameStateJson.toString());
					}

				default:
					log.info("ignoring "+ message);
					break;
				}
			}
		};
	}

	@Override
	protected AppType getAppType() {
		return AppType.PLATFORM;
	}

	@Override
	protected void onRun(String... args) throws Exception {
		log.info("Started Ultimate Tic-Tac-Toe Platform: " + getAppId());
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(PlatformApp.class, args);
	}
}