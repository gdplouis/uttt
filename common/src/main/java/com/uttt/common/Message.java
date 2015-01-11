package com.uttt.common;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Message {

	private String apiVersion;
	private long time;
	private MessageType messageType;
	private String body;

	public Message(String apiVersion, long time, MessageType messageType, String body) {
		this.apiVersion = apiVersion;
		this.time = time;
		this.messageType = messageType;
		this.body = body;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	public static Message toMessage(String jsonStr) throws JsonSyntaxException {
		return new Gson().fromJson(jsonStr, Message.class);
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}