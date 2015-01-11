package com.uttt.common;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Message {

	private String src;
	private String dest;
	private String apiVersion;
	private long time;
	private MessageType messageType;
	private String body;

	public Message(String src, String dest, String apiVersion, long time, MessageType messageType, String body) {
		this.src = src;
		this.dest = dest;
		this.apiVersion = apiVersion;
		this.time = time;
		this.messageType = messageType;
		this.body = body;
	}

	@Override
	public String toString() {
		return serialize(this);
	}

	public static String serialize(Message message) {
		return new Gson().toJson(message);
	}

	public static Message deserialize(String jsonStr) throws JsonSyntaxException {
		return new Gson().fromJson(jsonStr, Message.class);
	}

	public String getSrc() {
		return src;
	}

	public String getDest() {
		return dest;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public long getTime() {
		return time;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public String getBody() {
		return body;
	}
}