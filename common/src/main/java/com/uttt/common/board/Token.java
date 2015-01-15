package com.uttt.common.board;

public enum Token implements Node {
	EMPTY       ("."),
	PLAYER_AAA ("X"),
	PLAYER_BBB ("O"),
	;

	public final String mark;

	Token(String mark) {
		this.mark = mark;
	}

	@Override
	public int getHeight() {
		return 0;
	}

}