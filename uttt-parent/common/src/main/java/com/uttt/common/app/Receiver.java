package com.uttt.common.app;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonSyntaxException;

public abstract class Receiver {

	private final static Logger log = Logger.getLogger(Receiver.class);

	private CountDownLatch latch = new CountDownLatch(1);

	private final ErrorHandler handler;

	public Receiver(ErrorHandler handler) {
		this.handler = handler;
	}

	public CountDownLatch getLatch() {
		return latch;
	}

	public void receive(String payload) {

		log.debug("Received this: " + payload);

		final Message message;
		try {
			message = Message.deserialize(payload);
		} catch (JsonSyntaxException e) {
			log.error("Bad message", e);
			return;
		}

		try {
			onReceive(message);
		} catch (Exception e) {
			handler.handleError(message, e);
			return;
		}
		latch.countDown();
	}

	protected JSONObject getBody(Message message) throws JSONException {
		return new JSONObject(new JSONObject(message.getBody()).getString("body"));
	}

	protected abstract void onReceive(Message message) throws Exception;
}
