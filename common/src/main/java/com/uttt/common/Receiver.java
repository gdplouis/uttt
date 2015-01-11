package com.uttt.common;

import java.util.concurrent.CountDownLatch;

public abstract class Receiver {

	private CountDownLatch latch = new CountDownLatch(1);

	public CountDownLatch getLatch() {
		return latch;
	}
	
	public void receive(String payload) {
		onReceive(payload);
	}
	
	protected abstract void onReceive(String payload);
}
