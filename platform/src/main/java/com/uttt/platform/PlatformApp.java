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

public class PlatformApp extends App {

	private final static Logger log = Logger.getLogger(PlatformApp.class);

	@Autowired
	private AnnotationConfigApplicationContext context;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	private GameManager gameManager = new GameManager();

	@Bean
	Receiver receiver() {
		return new Receiver(getAppId(), getErrorHandler()) {

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
					final JSONObject appIdJson = new JSONObject();
					appIdJson.put("platform_app_id", getAppId());
					sendMessage(message.getSrc(), MessageType.PLATFORM_DISCOVERY_RESPONSE, appIdJson.toString());
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
					final String result = gameManager.addPlayerToGame(existingGameId, message.getSrc()).name();
					final JSONObject resultJson = new JSONObject();
					resultJson.put("result", result);
					resultJson.put("game_id", existingGameId);
					sendMessage(message.getSrc(), MessageType.JOIN_GAME_RESPONSE, resultJson.toString());
					break;

				default:
					log.info("ignoring "+ message);
					break;
				}
			}
		};
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Started Ultimate Tic-Tac-Toe Platform: " + getAppId());
		while(true) {}
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(PlatformApp.class, args);
	}
}