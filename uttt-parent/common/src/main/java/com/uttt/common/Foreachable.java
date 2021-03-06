package com.uttt.common;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The {@code Foreachable} class is designed to simplify the abstraction of looping across a range of {@code int}
 * values. Rather than loop controls like:
 *
 * <PRE> for(final int i = 0; i < size; ++i) {...}</PRE>
 *
 * you can write them as:
 *
 * <PRE> for(final int i : Foreachable.until(size)) {...}</PRE>
 *
 * Further, there are several variations of such factory methods which cover exclusive and inclusive upper bound,
 * implicit zero vs. explicit lower bound, and implicit step-by-one vs. explicit step value.
 */
public final class Foreachable implements Iterable<Integer> {

	public final int start;
	public final int limit;
	public final int step;

	private Foreachable(int start, int limit, int step) {
		if (step < 1) {
			throw ExUtil.create(IllegalArgumentException.class)
				.ident("step", step)
				.append("must be positive value")
				.build();
		}

		this.start = start;
		this.limit = limit;
		this.step  = step;
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	/**
	 * Iterable as in: {@code for (int i = start; i < limit; i+=step)}.
	 *
	 * @param start - initial value
	 * @param limit - upper bound, exclusive
	 * @param step - must be positive
	 */
	public static Foreachable until(int start, int limit, int step) {
		return new Foreachable(start, limit, step);
	}

	/**
	 * Iterable as in: {@code for (int i = start; i < limit; ++i)}.
	 *
	 * @param start - initial value
	 * @param limit - upper bound, exclusive
	 */
	public static Foreachable until(int start, int limit) {
		return until(start, limit, 1);
	}

	/**
	 * Iterable as in: {@code for (int i = 0; i < limit; ++i)}.
	 *
	 * @param limit - upper bound, exclusive
	 */
	public static Foreachable until(int limit) {
		return until(0, limit, 1);
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	/**
	 * Iterable as in: {@code for (int i = start; i <= limit; i+=step)}.
	 *
	 * @param start - initial value
	 * @param limit - upper bound, inclusive
	 * @param step - must be positive
	 */
	public static Foreachable to(int start, int limit, int step) {
		return until(start, (limit+step), step);
	}

	/**
	 * Iterable as in: {@code for (int i = start; i <= limit; ++i)}.
	 *
	 * @param start - initial value
	 * @param limit - upper bound, inclusive
	 */
	public static Foreachable to(int start, int limit) {
		return to(start, limit, 1);
	}

	/**
	 * Iterable as in: {@code for (int i = 0; i <= limit; ++i)}.
	 *
	 * @param limit - upper bound, inclusive
	 */
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
				if (!hasNext()) {
					throw ExUtil.create(NoSuchElementException.class)
						.ident("cursor", cursor)
						.append("already at/beyond limit[=", limit, "]")
						.build();
				}

				int rval = cursor;
				cursor += step;

				return rval;
			}
		};
	}
}