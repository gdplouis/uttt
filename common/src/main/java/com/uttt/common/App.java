package com.uttt.common;

import java.util.UUID;

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

import com.google.gson.JsonObject;

@Configuration
@EnableAutoConfiguration
public abstract class App implements CommandLineRunner {

	private final static Logger log = Logger.getLogger(App.class);

	private final static String queueName = "spring-boot";

	private final String appId = UUID.randomUUID().toString();

	@Autowired
	protected AnnotationConfigApplicationContext context;

	@Autowired
	protected RabbitTemplate rabbitTemplate;

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("spring-boot-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}

	//	@Bean
	//	public ConnectionFactory connectionFactory() {
	//		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
	//		connectionFactory.setPort(61636);
	//		return connectionFactory;
	//	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receive");
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

	protected void sendMessage(String dest, MessageType messageType, String body) throws JSONException {
		final JSONObject bodyJson = new JSONObject();
		bodyJson.put("body", body);
		final Message message = new Message(getAppId(), dest, MessageType.API_VERSION, System.currentTimeMillis(), messageType, bodyJson.toString());
		sendMessage(message);
	}

	protected void sendErrorMessage(Message cause, Exception e) throws JSONException {
		final JsonObject json = new JsonObject();
		json.addProperty("cause", cause.toString());
		json.addProperty("exception", e.toString());
		sendMessage(cause.getSrc(), MessageType.FAILED, json.toString());
	}

	protected void sendMessage(Message message) {
		final String payload = message.toString();
		log.debug("Sending this: " + payload);
		rabbitTemplate.convertAndSend(queueName, payload);
	}
}