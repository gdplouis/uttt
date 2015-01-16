package com.uttt.common.board;

public enum Token implements Node {
	EMPTY      (".", Node.Status.OPEN),
	PLAYER_AAA ("x", Node.Status.WINNER_AAA),
	PLAYER_BBB ("o", Node.Status.WINNER_BBB),
	;

	public final Node.Status status;
	public final String mark;

	Token(String mark, Node.Status status) {
		this.mark   = mark;
		this.status = status;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public Status getStatus() {
		return status;
	}

}