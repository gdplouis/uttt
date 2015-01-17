package com.uttt.common;

import java.util.Iterator;


public class Foreachable implements Iterable<Integer> {

	public final int start;
	public final int limit;
	public final int step;

	private Foreachable(int start, int limit, int step) {
		if (step < 1) {
			throw new IllegalArgumentException("step [" + step + "] must be positive value");
		}

		this.start = start;
		this.limit = limit;
		this.step  = step;
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	public static Foreachable until(int start, int limit, int step) {
		return new Foreachable(start, limit, step);
	}

	public static Foreachable until(int start, int limit) {
		return until(start, limit, 1);
	}

	public static Foreachable until(int limit) {
		return until(0, limit, 1);
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	public static Foreachable to(int start, int limit, int step) {
		return until(start, (limit+step), step);
	}

	public static Foreachable to(int start, int limit) {
		return to(start, limit, 1);
	}

	public static Foreachable to(int limit) {
		return to(0, limit, 1);
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {

			private int cursor = start;

			@Override
			public boolean hasNext() {
				return (cursor < limit);
			}

			@Override
			public Integer next() {
				int rval = cursor;
				cursor += step;

				return rval;
			}
		};
	}
}