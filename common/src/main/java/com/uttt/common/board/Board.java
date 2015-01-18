package com.uttt.common.board;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.uttt.common.ArgCheck;
import com.uttt.common.Foreachable;
import com.uttt.common.utils.ArrayUtils;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.JsonNumber;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;

public final class Board implements Node {

	private final Board       parent;
	private final Coordinates metaCoord;
	private final int         height;
	private final int         size;
	private final Node[][]    field;

	private Node.Status status = Node.Status.OPEN;

	private Board(Board parent, Coordinates metaCoord, int height, int size) {
		ArgCheck.rangeClosed("height", height, 1, 3);
		ArgCheck.rangeClosed("size"  , size  , 2, 5);

		this.parent    = parent;
		this.metaCoord = metaCoord;
		this.height    = height;
		this.size      = size;

		this.field = new Node[size][];
		for (int i  : Foreachable.until(size)) {
			field[i] = new Node[size];
		}

		for (int row : Foreachable.until(size)) {
			for (int col : Foreachable.until(size)) {
				final Node node = (height == 1) ? Token.EMPTY : new Board(this, new Coordinates(row, col), (height - 1), size);
				field[row][col] = node;
			}
		}
	}

	public Board(int height, int size) {
		this((Board) null, (Coordinates) null, height,  size);
	}

	private Board(Board parent, Coordinates metaCoord, int height, int size, Node[][] field) {
		this.parent = parent;
		this.metaCoord = metaCoord;
		this.height = height;
		this.size = size;
		this.field = field;
	}

	public Board getParent() {
		return parent;
	}

	public Coordinates getMetaCoord() {
		return metaCoord;
	}

	@Override
	public int getHeight() {
		return height;
	}

	public int getSize() {
		return size;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	protected void setStatus(Node.Status status) {
		this.status = status;
	}

	/**
	 * Returns the underlying field data structure. Only visible at package level for testing.
	 *
	 * @return
	 */
	/* pkg */ Node[][] getField() {
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

	private boolean isWinner(Token token, int placedRow, int placedCol) {

		for (int col : Foreachable.to(size)) {
			if (col == size) {
				return true;
			}
			if (field[placedRow][col].getStatus() != token.getStatus())
				break;
		}

		for (int row : Foreachable.to(size)) {
			if (row == size) {
				return true;
			}
			if (field[row][placedCol].getStatus() != token.getStatus())
				break;
		}

		if (placedRow == placedCol) {
			for (int diag : Foreachable.to(size)) {
				if (diag == size) {
					return true;
				}
				if (field[diag][diag].getStatus() != token.getStatus())
					break;
			}
		}

		if (placedCol == (size - 1 - placedRow)) {
			for (int diag : Foreachable.to(size)) {
				if (diag == size) {
					return true;
				}
				if (field[(size - 1 - diag)][diag].getStatus() != token.getStatus())
					break;
			}
		}

		return false;
	}

	private Coordinates updatePosition(Token token, Coordinates coordinates) {
		int myRow = coordinates.getRow();
		int myCol = coordinates.getCol();

		if (height > 1) {
			Board       subBoard = getSubNode(myRow, myCol, Board.class);
			Coordinates subCoord = coordinates.getSubordinates();

			if (subBoard.getStatus() != Node.Status.OPEN) {
				throw new IllegalArgumentException("Illegal Move: board at height [" + subBoard.getHeight() + "] is not OPEN for play");
			}

			Coordinates subRestriction = subBoard.updatePosition(token, subCoord);
			Coordinates myRestriction  = (subRestriction == null ? new Coordinates(myRow, myCol) : subRestriction.within(myRow, myCol));

			return myRestriction;
		}

		Token inPlace = getSubNode(myRow, myCol, Token.class);
		if (inPlace != Token.EMPTY) {
			throw new IllegalArgumentException("token position already filled");
		}

		field[myRow][myCol] = token;

		// check win conditions, percolating towards top (null parent)

		Board boardCheck = this;
		int   rowCheck   = myRow;
		int   colCheck   = myCol;

		while (boardCheck != null) {
			if (!boardCheck.isWinner(token, rowCheck, colCheck)) {
				break;
			}
			boardCheck.setStatus(token.getStatus());

			if (boardCheck.parent == null) {
				break;
			}

			rowCheck = boardCheck.metaCoord.getRow();
			colCheck = boardCheck.metaCoord.getCol();

			boardCheck = boardCheck.parent;
		}

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

	private char getPadChar() {
		switch (status) {
			case OPEN:       return ' ';
			case DRAW:       return '?';
			case WINNER_AAA: return 'X';
			case WINNER_BBB: return 'O';
			default:         return '*';
		}
	}
	private List<StringBuilder> fieldAsListOfStringBuilderForHeightOne() {
		final char padChar = getPadChar();

		// horizontal rule between rows is as wide as size plus vertical separators (2 * size - 1), with left/right padding

		final String hrule;
		{
			StringBuilder sb = new StringBuilder();
			sb.append(padChar);

			for (int i : Foreachable.until(2 * size - 1))
				sb.append('-');

			sb.append(padChar);

			hrule = sb.toString();
		}

		// top/bottom padding is as wide as the hrule, but is all spaces

		final String topBotPad = hrule.toString().replace('-', padChar);

		// stringize board by rows by tokens, with left/right padding

		List<StringBuilder> myBuilders = new LinkedList<>();
		myBuilders.add((new StringBuilder()).append(topBotPad));

		for (int row : Foreachable.until(size)) {
			if (row > 0)
				myBuilders.add((new StringBuilder()).append(hrule));

			final StringBuilder rowSb = new StringBuilder();
			myBuilders.add(rowSb);

			rowSb.append(padChar);
			for (int col : Foreachable.until(size)) {
				if (col > 0)
					rowSb.append('|');

				Token token = getSubNode(row, col, Token.class);
				rowSb.append(token.mark);
			}
			rowSb.append(padChar);
		}
		myBuilders.add((new StringBuilder()).append(topBotPad));

		return myBuilders;
	}

	private List<StringBuilder> fieldAsListOfStringBuilder() {
		if (height == 1) {
			return fieldAsListOfStringBuilderForHeightOne();
		}

		final char padChar = getPadChar();
		final List<StringBuilder> myBuilders = new LinkedList<>();

		String hrule = null; // deferred build out until first needed

		// build by row

		for (int row : Foreachable.until(size)) {
			if (row > 0) {
				if (hrule == null) {
					StringBuilder sb = new StringBuilder();

					sb.append(padChar); // left padding

					int width = myBuilders.get(0).length();
					for (@SuppressWarnings("unused") int i : Foreachable.until(1, (width - 1)))
						sb.append('-');

					sb.append(padChar); // right padding

					hrule = sb.toString();
				}

				for (int i : Foreachable.until(height))
					myBuilders.add((new StringBuilder()).append(hrule));
			}

			List<StringBuilder> rowBuilders = getSubNode(row, 0, Board.class).fieldAsListOfStringBuilder();
			for (int col : Foreachable.until(1, size)) {
				List<StringBuilder> subBuilders = getSubNode(row, col, Board.class).fieldAsListOfStringBuilder();

				for (int line : Foreachable.until(rowBuilders.size())) {
					StringBuilder lineBuilder = rowBuilders.get(line);

					for (int i : Foreachable.until(height))
						lineBuilder.append('|');

					lineBuilder.append(subBuilders.get(line));
				}
			}
			for (StringBuilder lineBuilder : rowBuilders)
				lineBuilder.insert(0, padChar).append(padChar); // left/right padding

			myBuilders.addAll(rowBuilders);
		}

		// top/bottom padding is as wide as the hrule, but is all spaces

		@SuppressWarnings("null")
		final String topBotPad = hrule.toString().replace('-', padChar);
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

		StringBuilder boardId = new StringBuilder();
		Coordinates pCoord = metaCoord;
		for (Board p = parent; p != null; pCoord = p.metaCoord, p = p.parent) {
			boardId.insert(0, "-(" + pCoord.getRow() + "," + pCoord.getCol() + ")");
		}
		boardId.insert(0, "TOP");

		totalSb.append(boardId).append(".\n");

		return totalSb.toString();
	}

	public String serialize() {
		return new JSONSerializer().serialize(this);
	}
	
	public static Board deserialize(String jsonStr) {
		
		return new JSONDeserializer<Board>().use(Board.class, new ObjectFactory() {
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
				Map<String, Object> node = (Map<String, Object>) value;
				return getNode(node);
			}

			@SuppressWarnings("unchecked")
			private Board getNode(Map<String, Object> node) {
				
				if (node == null) {
					return null;
				}
				
				Object[][] oField = ArrayUtils.convertNestedList(((List<List<Object>>) node.get("field")));
				Node[][] nFile = new Node[oField.length][oField[0].length];
				for (int i = 0; i < oField.length; i++) {
					for (int j = 0; j < oField[0].length; j++) {
						if (oField[i][j] instanceof String) {
							nFile[i][j] = Token.toToken((String)oField[i][j]);
							continue;
						}
						Map<String, Object> coco = (Map<String, Object>)oField[i][j];
						nFile[i][j] = getNode(coco);
					}
				}
				final Board parent = getNode((Map<String, Object>) node.get("parent"));
				final Coordinates metaCoord = null;
				final int height = ((JsonNumber) node.get("height")).intValue();
				final int size = ((JsonNumber) node.get("size")).intValue();
				return new Board(parent, metaCoord, height, size, nFile);
			}
		})
		.deserialize(jsonStr);
	}
}
