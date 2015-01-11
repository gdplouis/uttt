package com.uttt.platform;

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

public class PlatformApp extends App {

	private final static Logger log = Logger.getLogger(PlatformApp.class);

	private final static String queueName = "spring-boot";

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
				case PLATFORM_TEST:
					log.info("Got this: " + message);
					rabbitTemplate.convertAndSend(queueName, new Message(MessageType.API_VERSION, MessageType.PLAYER_TEST, "COOOL").toString());
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
		log.info("Started Ultimate Tic-Tac-Toe Platform");
		while(true) {}
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(PlatformApp.class, args);
	}
}