package com.uttt.common.board;

public interface Node {

	int getHeight();

	default <T extends Node> T getSubNode(int x, int y, Class<T> typeClass) {
		throw new RuntimeException(typeClass.getName() + " nodes don't have sub-nodes");
	}

	default Node getSubNode(int x, int y) {
		return getSubNode(x, y, Node.class);
	}
}
