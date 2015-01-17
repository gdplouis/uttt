package com.uttt.common.board;

import java.util.LinkedList;
import java.util.List;

import com.uttt.common.ArgCheck;

public final class Board implements Node {

	private final int height;
	private final int size;
	private final Node[][] field;

	private Node.Status status = Node.Status.OPEN;

	public Board(int height, int size) {
		ArgCheck.rangeClosed("height", height, 1, 3);
		ArgCheck.rangeClosed("size"  , size  , 2, 4);

		this.height = height;
		this.size = size;

		this.field = new Node[size][];
		for (int i = 0; i < size; ++i) {
			field[i] = new Node[size];
		}

		for (int row = 0; row < size; ++row) {
			for (int col = 0; col < size; ++col) {
				final Node node = (height == 1) ? Token.EMPTY : new Board((height - 1), size);
				field[row][col] = node;
			}
		}
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	protected void setStatus(Node.Status status) {
		this.status = status;
	}

	public int getSize() {
		return size;
	}

	public Node[][] getField() {
		return field;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Node> T getSubNode(int row, int col, Class<T> typeClass) {
		ArgCheck.rangeClosedOpen("x", row, 0, size);
		ArgCheck.rangeClosedOpen("y", col, 0, size);

		return (T) field[row][col];
	}

	private void checkCoordinatesInRange(Coordinates coordinates) {
		final int coordHeight = coordinates.getHeight();
		if (coordHeight > height) {
			throw new IllegalArgumentException("Coordinate height [" + coordHeight + "] is greater than board height [" + height + "]");
		}

		ArgCheck.rangeClosedOpen("row", coordinates.getRow(), 0, size);
		ArgCheck.rangeClosedOpen("col", coordinates.getCol(), 0, size);

		if (coordHeight > 1) {
			checkCoordinatesInRange(coordinates.getSubordinates());
		}
	}

	private Coordinates updatePosition(Token token, Coordinates coordinates) {
		int myRow = coordinates.getRow();
		int myCol = coordinates.getCol();

		if (height > 1) {
			Board       subBoard = getSubNode(myRow, myCol, Board.class);
			Coordinates subCoord = coordinates.getSubordinates();

			Coordinates subRestriction = subBoard.updatePosition(token, subCoord);
			Coordinates myRestriction  = (subRestriction == null ? new Coordinates(myRow, myCol) : subRestriction.within(myRow, myCol));

			return myRestriction;
		}

		Token inPlace = getSubNode(myRow, myCol, Token.class);
		if (inPlace != Token.EMPTY) {
			throw new IllegalArgumentException("token position already filled");
		}

		field[myRow][myCol] = token;

		return null;
	}

	public Coordinates placeToken(Token token, Coordinates coordinates) {
		final int coordHeight = coordinates.getHeight();
		if (coordHeight != height) {
			throw new IllegalArgumentException("Coordinate height [" + coordHeight + "] must equal board height [" + height + "]");
		}

		checkCoordinatesInRange(coordinates);

		return updatePosition(token, coordinates);
	}

	private List<StringBuilder> fieldAsListOfStringBuilderForHeightOne() {
		// horizontal rule between rows is as wide as size plus vertical separators (2 * size - 1), with left/right padding

		final String hrule;
		{
			StringBuilder sb = new StringBuilder();
			sb.append(' ');

			for (int i = 0; i < (2 * size - 1); ++i)
				sb.append('-');

			sb.append(' ');

			hrule = sb.toString();
		}

		// top/bottom padding is as wide as the hrule, but is all spaces

		final String topBotPad = hrule.toString().replace('-', ' ');

		// stringize board by rows by tokens, with left/right padding

		List<StringBuilder> myBuilders = new LinkedList<>();
		myBuilders.add((new StringBuilder()).append(topBotPad));

		for (int row = 0; row < size; ++row) {
			if (row > 0)
				myBuilders.add((new StringBuilder()).append(hrule));

			final StringBuilder rowSb = new StringBuilder();
			myBuilders.add(rowSb);

			rowSb.append(' ');
			for (int col = 0; col < size; ++col) {
				if (col > 0)
					rowSb.append('|');

				Token token = getSubNode(row, col, Token.class);
				rowSb.append(token.mark);
			}
			rowSb.append(' ');
		}
		myBuilders.add((new StringBuilder()).append(topBotPad));


		return myBuilders;
	}

	private List<StringBuilder> fieldAsListOfStringBuilder() {

		if (height == 1) {
			return fieldAsListOfStringBuilderForHeightOne();
		}

		List<StringBuilder> myBuilders = new LinkedList<>();

		String hrule = null; // deferred build out until first needed

		// build by row

		for (int row = 0; row < size; ++row) {
			if (row > 0) {
				if (hrule == null) {
					StringBuilder sb = new StringBuilder();

					sb.append(' '); // left padding

					int width = myBuilders.get(0).length();
					for (int i = 1; i < (width - 1); ++i)
						sb.append('-');

					sb.append(' '); // right padding

					hrule = sb.toString();
				}

				for (int i = 0; i < height; ++i)
					myBuilders.add((new StringBuilder()).append(hrule));
			}

			List<StringBuilder> rowBuilders = getSubNode(row, 0, Board.class).fieldAsListOfStringBuilder();
			for (int col = 1; col < size; ++col) {
				List<StringBuilder> subBuilders = getSubNode(row, col, Board.class).fieldAsListOfStringBuilder();

				for (int line = 0; line < rowBuilders.size(); ++line) {
					StringBuilder lineBuilder = rowBuilders.get(line);

					for (int i = 0; i < height; ++i)
						lineBuilder.append('|');

					lineBuilder.append(subBuilders.get(line));
				}
			}
			for (StringBuilder lineBuilder : rowBuilders)
				lineBuilder.insert(0, ' ').append(' '); // left/right padding

			myBuilders.addAll(rowBuilders);
		}

		// top/bottom padding is as wide as the hrule, but is all spaces

		@SuppressWarnings("null")
		final String topBotPad = hrule.toString().replace('-', ' ');
		myBuilders.add(0, (new StringBuilder()).append(topBotPad));
		myBuilders.add((new StringBuilder()).append(topBotPad));


		return myBuilders;
	}

	public String fieldAsPrintableString() {

		final StringBuilder totalSb = new StringBuilder();
		final List<StringBuilder> sbs = fieldAsListOfStringBuilder();
		for (StringBuilder line : sbs) {
			totalSb.append(line).append('\n');
		}

		return totalSb.toString();
	}
}
