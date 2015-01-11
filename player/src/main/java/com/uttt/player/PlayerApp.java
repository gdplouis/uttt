package com.uttt.player;

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
import com.uttt.player.game.Player;

public class PlayerApp extends App {

	private final static Logger log = Logger.getLogger(PlayerApp.class);

	@Autowired
	private AnnotationConfigApplicationContext context;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	private Player player;

	@Bean
	Receiver receiver() {
		return new Receiver(getAppId(), getErrorHandler()) {

			@Override
			public void onReceive(Message message) throws Exception {
				final MessageType type = message.getMessageType();
				
				switch(type) {
				case PLAYER_TEST_RESPONSE:
					log.info("Got this: " + message);
					break;

				case PLATFORM_DISCOVERY_RESPONSE:
					log.info("Found platform");
					final String platformId = getBody(message).getString("platform_app_id");
					sendMessage(platformId, MessageType.GET_OPEN_GAMES_REQUEST, "Give me an open game");
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
					log.info("Adding game " + newGameId +", waiting for platform");
					player.addNewGame(newGameId);
					break;

				case JOIN_GAME_RESPONSE:
					final String existingGameId = getBody(message).getString("game_id");
					log.info("Adding game " + existingGameId +", waiting for platform");
					player.addNewGame(existingGameId);
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
		setPlayer(new Player() {
			@Override
			public int makeMove(String gameId) {
				return 0;
			}
		});
		log.info("Starting dummy player " + getAppId());
		sendMessage("*", MessageType.PLATFORM_DISCOVERY_REQUEST, "Where are you platform?");
		//		receiver().getLatch().await(10, TimeUnit.SECONDS);
		//		context.close();
		while(true) {}
	}

	private void setPlayer(Player player) {
		this.player = player;
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(PlayerApp.class, args);
	}
}