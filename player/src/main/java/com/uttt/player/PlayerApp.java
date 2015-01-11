package com.uttt.player;


import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import com.google.gson.JsonSyntaxException;
import com.uttt.common.App;
import com.uttt.common.Message;
import com.uttt.common.MessageType;
import com.uttt.common.Receiver;

public class PlayerApp extends App {

	private final static Logger log = Logger.getLogger(PlayerApp.class);

	@Autowired
	AnnotationConfigApplicationContext context;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Bean
	Receiver receiver() {
		return new Receiver() {

			@Override
			public void onReceive(String payload) {

				final Message message;
				try {
					message = Message.toMessage(payload);
				} catch (JsonSyntaxException e) {
					log.error("Bad message", e);
					return;
				}

				if (!message.getApiVersion().equals(MessageType.API_VERSION)) {
					log.error("Received this unsupported message: " + message);
					return;
				}

				final MessageType type = message.getMessageType();
				switch(type) {
				case PLAYER_TEST:
					log.info("Got this: " + message);
					break;
				default:
					log.info("ignoring "+ type);
					break;
				}
			}
		};
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Starting dummy player 1");
		sendMessage(MessageType.API_VERSION, MessageType.PLATFORM_TEST, "Hey Platform");
		receiver().getLatch().await(10, TimeUnit.SECONDS);
		context.close();
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(PlayerApp.class, args);
	}
}