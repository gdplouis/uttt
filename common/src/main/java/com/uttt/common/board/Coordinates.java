package com.uttt.common.board;

import java.lang.reflect.Type;
import java.util.Map;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.JsonNumber;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;

public class Coordinates {

	private final int row;
	private final int col;
	private final Coordinates subordinates;
	private final int height;

	public Coordinates(int row, int col, Coordinates subordinates) {
		super();
		this.row = row;
		this.col = col;
		this.subordinates = subordinates;

		this.height = 1 + (subordinates == null ? 0 : subordinates.height);
	}

	public Coordinates(int row, int col) {
		this(row, col, null);
	}

	public Coordinates within(int metaRow, int metCol) {
		return new Coordinates(metaRow, metCol, this);
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Coordinates getSubordinates() {
		return subordinates;
	}

	public int getHeight() {
		return height;
	}

	public String asPrintableString() {
		StringBuilder sb = new StringBuilder();

		boolean first = true;
		sb.append('{');
		for (Coordinates x = this; x != null; x = x.getSubordinates()) {
			if (first) {
				first = false;
				sb.append("H=").append(x.getHeight()).append(": ");
			} else {
				sb.append(',');
			}
			sb.append('(');
			sb.append(x.getRow());
			sb.append(',');
			sb.append(x.getCol());
			sb.append(')');
		}
		sb.append('}');

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + height;
		result = prime * result + row;
		result = prime * result + ((subordinates == null) ? 0 : subordinates.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Coordinates)) return false;

		Coordinates that = (Coordinates) obj;
		if (this.col    != that.col)     return false;
		if (this.height != that.height)  return false;
		if (this.row    != that.row)     return false;

		if (subordinates == null) {
			return (that.subordinates == null);
		}

		return subordinates.equals(that.subordinates);
	}

	public String serialize() {
		return new JSONSerializer().deepSerialize(this);
	}
	
	public static Coordinates deserialize(String jsonStr) {
		return new JSONDeserializer<Coordinates>()
				.use(Coordinates.class, genObjectFactory())
				.deserialize(jsonStr);
	}

	public static ObjectFactory genObjectFactory() {

		return new ObjectFactory() {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
				return getCoords((Map<String, Object>) value);
			}

			@SuppressWarnings("unchecked")
			private Coordinates getCoords(Map<String, Object> map) {
				final int row = ((JsonNumber) map.get("row")).intValue();
				final int col = ((JsonNumber) map.get("col")).intValue();
				if (map.get("subordinates") != null) {
					return new Coordinates(row, col, getCoords((Map<String, Object>) map.get("subordinates")));
				}
				return new Coordinates(row, col);
			}
		};
	}
}
