package com.uttt.common.board;

public enum Token implements Node {
	EMPTY      ("."),
	PLAYER_AAA ("x"),
	PLAYER_BBB ("o"),
	;

	public final String      mark;

	Token(String mark) {
		this.mark   = mark;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public Status getStatus() {
		switch(this) {
			case EMPTY     : return Node.Status.OPEN;
			case PLAYER_AAA: return Node.Status.WINNER_AAA;
			case PLAYER_BBB: return Node.Status.WINNER_BBB;

			default: return null;
		}
	}

}