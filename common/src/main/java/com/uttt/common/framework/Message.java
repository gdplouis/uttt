package com.uttt.common.framework;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

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
		return new JSONSerializer().serialize(message);
	}

	public static Message deserialize(String jsonStr) {
		return new JSONDeserializer<Message>().deserialize(jsonStr);
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