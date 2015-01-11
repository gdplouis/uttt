package com.uttt.common;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Message {

	private String apiVersion;
	private MessageType messageType;
	private String body;

	public Message(String apiVersion, MessageType messageType, String body) {
		this.setApiVersion(apiVersion);
		this.setMessageType(messageType);
		this.setBody(body);
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