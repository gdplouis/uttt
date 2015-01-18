package com.uttt.common.framework;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public abstract class App implements CommandLineRunner {

	private final static Logger log = Logger.getLogger(App.class);

	protected final static String PLATFORM_QUEUE = "platform-queue";
	private final static String PLAYER_QUEUE = "player-queue-";

	private final String appId = UUID.randomUUID().toString();

	protected AtomicBoolean running;

	@Autowired
	protected AnnotationConfigApplicationContext context;

	@Autowired
	protected RabbitTemplate rabbitTemplate;

	protected enum AppType {
		PLATFORM,
		PLAYER;
	}

	protected abstract AppType getAppType();
	protected abstract Receiver receiver();	
	protected abstract void onRun(String... args) throws Exception;

	@Bean
	Queue queue() {
		return new Queue(getThisQueueName());
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("spring-boot-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(getThisQueueName());
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(getThisQueueName());
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receive");
	}

	@Override
	public void run(String... args) throws Exception {
		if (getAppType() == AppType.PLATFORM) {
			log.info("Starting platform");
		} else {
			log.info("Starting player " + getAppId());
		}
		log.info("My route =" + getThisQueueName());
		running = new AtomicBoolean(true);
		onRun(args);
		while (running.get()) {
			Thread.sleep(1000l);
		}
		receiver().getLatch().await(1, TimeUnit.SECONDS);
		context.close();
	}

	protected void close() throws InterruptedException {
		running.set(false);
	}

	protected String getAppId() {
		return appId;
	}

	protected ErrorHandler getErrorHandler() {
		return new ErrorHandler() {
			@Override
			public void handleError(Message cause, Exception e) {
				log.error("This message: " + cause + "raised exception", e);
				try {
					sendErrorMessage(cause, e);
				} catch (Exception e2) {
					log.error("Got this while try to send error message", e);
				}
			}
		};
	}

	protected String getThisQueueName() {
		return getAppType() == AppType.PLATFORM ? PLATFORM_QUEUE : PLAYER_QUEUE + getAppId();
	}

	protected String getOtherQueue(String dest) {
		return getAppType() == AppType.PLAYER ? PLATFORM_QUEUE : PLAYER_QUEUE + dest;
	}

	protected void sendMessage(String dest, MessageType messageType, String body) throws JSONException {
		final JSONObject bodyJson = new JSONObject();
		bodyJson.put("body", body);
		final Message message = new Message(getAppId(), dest, MessageType.API_VERSION, System.currentTimeMillis(), messageType, bodyJson.toString());
		sendMessage(message);
	}

	protected void sendErrorMessage(Message cause, Exception e) throws JSONException {
		final JSONObject json = new JSONObject();
		json.put("cause", cause.toString());
		json.put("exception", e.toString());
		sendMessage(cause.getSrc(), MessageType.FAILED, json.toString());
	}

	protected void sendMessage(Message message) {
		final String payload = message.toString();
		log.debug("Sending this: " + payload);
		rabbitTemplate.convertAndSend("spring-boot-exchange", getOtherQueue(message.getDest()), payload);
	}
}