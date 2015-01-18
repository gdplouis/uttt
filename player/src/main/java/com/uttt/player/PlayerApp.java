package com.uttt.player;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import com.uttt.common.board.Board;
import com.uttt.common.board.Coordinates;
import com.uttt.common.board.Token;
import com.uttt.common.framework.App;
import com.uttt.common.framework.Message;
import com.uttt.common.framework.MessageType;
import com.uttt.common.framework.Receiver;
import com.uttt.common.player.Player;
import com.uttt.common.player.impl.DumbPlayer;

public class PlayerApp extends App {

	private final static Logger log = Logger.getLogger(PlayerApp.class);

	@Autowired
	private AnnotationConfigApplicationContext context;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	private Player player;

	@Bean
	@Override
	protected Receiver receiver() {
		return new Receiver(getErrorHandler()) {

			@Override
			public void onReceive(Message message) throws Exception {
				final MessageType type = message.getMessageType();

				switch(type) {
				case PLAYER_TEST_RESPONSE:
					log.info("Got this: " + message);
					break;

				case PLATFORM_DISCOVERY_RESPONSE:
					log.info("Found platform");
					sendMessage(message.getSrc(), MessageType.GET_OPEN_GAMES_REQUEST, "Give me an open game");
					break;

				case GET_OPEN_GAMES_RESPONSE:
					final JSONArray openGameIds = getBody(message).getJSONArray("open_game_ids");
					if (openGameIds.length() == 0) {
						log.info("No open games, let's start a new one");
						sendMessage(message.getSrc(), MessageType.START_NEW_GAME_REQUEST, "No open games, so start a new game");
					} else {
						log.info(openGameIds.length() + " open games, joining whichever");
						final JSONObject chosenGameId = new JSONObject();
						chosenGameId.put("game_id", openGameIds.get(0));
						sendMessage(message.getSrc(), MessageType.JOIN_GAME_REQUEST, chosenGameId.toString());
					}
					break;

				case START_NEW_GAME_RESPONSE:
					final String newGameId = getBody(message).getString("game_id");
					log.info("Found game " + newGameId +", waiting for platform");
					break;

				case JOIN_GAME_RESPONSE:
					final String existingGameId = getBody(message).getString("game_id");
					log.info("Joining game " + existingGameId +", waiting for platform");
					break;

				case GET_MOVE_REQUEST:
					final Token iAm = Token.toToken(getBody(message).getString("i_am"));
					final String playingGameId = getBody(message).getString("game_id");
					final Coordinates coords;
					if (getBody(message).has("coords")) {
						coords = Coordinates.deserialize(getBody(message).getString("coords"));
					} else {
						coords = null;
					}
					final Board board = Board.deserialize(getBody(message).getString("game_state"));
					log.info("Getting told to move with " + iAm);
					log.info(board.fieldAsPrintableString());
					final Coordinates newCoords = player.makeMove(iAm, board, coords);
					log.info("My move =" + newCoords.asPrintableString());
					final JSONObject moveJson = new JSONObject();
					moveJson.put("coords", newCoords);
					moveJson.put("game_id", playingGameId);
					sendMessage(message.getSrc(), MessageType.GET_MOVE_REPONSE, moveJson.toString());
					break;

				case WINNER_REQUEST:
					final JSONArray iWonFinalGameStateJson = getBody(message).getJSONArray("game_state");
					final List<Integer> finalGameState = new ArrayList<>(iWonFinalGameStateJson.length());
					for (int i=0; i<iWonFinalGameStateJson.length(); i++) {
						finalGameState.add(iWonFinalGameStateJson.getInt(i));
					}
					log.info("I won: " + finalGameState);
					close();
					break;

				case LOSER_REQUEST:
					final JSONArray iLostFinalGameStateJson = getBody(message).getJSONArray("game_state");
					final List<Integer> iLostFinalGameState = new ArrayList<>(iLostFinalGameStateJson.length());
					for (int i=0; i<iLostFinalGameStateJson.length(); i++) {
						iLostFinalGameState.add(iLostFinalGameStateJson.getInt(i));
					}
					log.info("I lost: " + iLostFinalGameState);
					close();
					break;

				case TIE_REQUEST:
					final JSONArray iTiedfinalGameStateJson = getBody(message).getJSONArray("game_state");
					final List<Integer> iTiedFinalGameState = new ArrayList<>(iTiedfinalGameStateJson.length());
					for (int i=0; i<iTiedfinalGameStateJson.length(); i++) {
						iTiedFinalGameState.add(iTiedfinalGameStateJson.getInt(i));
					}
					log.info("I tied: " + iTiedFinalGameState);
					close();
					break;

				default:
					log.info("ignoring "+ message);
					break;
				}
			}
		};
	}

	@Override
	protected AppType getAppType() {
		return AppType.PLAYER;
	}

	@Override
	protected void onRun(String... args) throws Exception {
		setPlayer(new DumbPlayer());
		sendMessage(PLATFORM_QUEUE, MessageType.PLATFORM_DISCOVERY_REQUEST, "Where are you platform?");
	}

	private void setPlayer(Player player) {
		this.player = player;
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(PlayerApp.class, args);
	}
}