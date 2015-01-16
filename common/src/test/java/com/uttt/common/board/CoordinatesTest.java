package com.uttt.common.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoordinatesTest {

	@Test
	public void accessor_getRow() {
		Coordinates coord = new Coordinates(3, 2);
		assertEquals("getRow: ", 3, coord.getRow());
	}

	@Test
	public void accessor_getCol() {
		Coordinates coord = new Coordinates(3, 2);
		assertEquals("getCol: ", 2, coord.getCol());
	}

	@Test
	public void accessor_getSubordinates_Null() {
		Coordinates coord = new Coordinates(3, 2);
		assertEquals("getSubordinates: ", null, coord.getSubordinates());
	}

	@Test
	public void accessor_getSubordinates_Extant() {
		Coordinates subordinates = new Coordinates(0,2);
		Coordinates coord = new Coordinates(3, 2, subordinates);
		assertSame("getSubordinates: ", subordinates, coord.getSubordinates());
	}

	@Test
	public void accessor_getHeight_One() {
		Coordinates coord = new Coordinates(3, 2);
		assertEquals("xx: ", 1, coord.getHeight());
	}

	@Test
	public void accessor_getHeight_Two() {
		final int expected = 2;

		Coordinates coord = new Coordinates (0,0);
		for (int i = 1; i < expected; ++i) {
			coord = new Coordinates (i, i, coord);
		}
		assertEquals("getHeight: ", expected, coord.getHeight());
	}

	@Test
	public void accessor_getHeight_Five() {
		final int height = 5;

		Coordinates coord = new Coordinates (0,0);
		for (int i = 1; i < height; ++i) {
			coord = new Coordinates (i, i, coord);
		}
		assertEquals("getHeight: ", height, coord.getHeight());
	}

	@Test
	public void testWithin() {
		final int height = 5;

		Coordinates coord = new Coordinates (0,0);
		for (int i = 1; i < height; ++i) {
			coord = coord.within(i, i);

			assertEquals("coord.getHeight: ", (i+1), coord.getHeight());
			assertEquals("coord.getRow(): ",  i,     coord.getRow());
			assertEquals("coord.getCol(): ",  i,     coord.getCol());

			Coordinates subord = coord.getSubordinates();
			assertEquals("subord.getHeight: ", i,     subord.getHeight());
			assertEquals("subord.getRow(): ",  (i-1), subord.getRow());
			assertEquals("subord.getCol(): ",  (i-1), subord.getCol());
		}
	}

	@Test
	public void equals_null() {
		Coordinates coord = new Coordinates (0,1);

		assertFalse(coord.equals(null));
	}

	@Test
	public void equals_OtherType() {
		Coordinates coord = new Coordinates (0,1);

		assertFalse(coord.equals("other"));
	}

	@Test
	public void equals_self() {
		Coordinates coord = new Coordinates (0,1);

		assertTrue(coord.equals(coord));
	}

	@Test
	public void equals_h1_same() {
		Coordinates aaaCoord = new Coordinates (0,1);
		Coordinates bbbCoord = new Coordinates (0,1);

		assertEquals(aaaCoord, bbbCoord);
	}

	@Test
	public void equals_h1_diff() {
		Coordinates aaaCoord = new Coordinates (0,1);
		Coordinates bbbCoord = new Coordinates (2,1);

		assertFalse("aaaCoord.equals(bbbCoord): ", aaaCoord.equals(bbbCoord));
	}

	@Test
	public void equals_h2_same() {
		Coordinates aaaCoord = new Coordinates (0,1).within(2,3);
		Coordinates bbbCoord = new Coordinates (0,1).within(2,3);

		assertEquals(aaaCoord, bbbCoord);
	}

	@Test
	public void equals_h2_diff() {
		Coordinates aaaCoord = new Coordinates (0,1).within(2,3);
		Coordinates bbbCoord = new Coordinates (2,1).within(2,3);
		Coordinates cccCoord = new Coordinates (2,1).within(4,5);

		assertFalse("aaaCoord.equals(bbbCoord): ", aaaCoord.equals(bbbCoord));
		assertFalse("bbbCoord.equals(cccCoord): ", bbbCoord.equals(cccCoord));
	}

	@Test
	public void equals_h2_h3() {
		Coordinates aaaCoord = new Coordinates (0,1).within(2,3);
		Coordinates bbbCoord = new Coordinates (2,1).within(2,3).within(4,5);

		assertFalse("aaaCoord.equals(bbbCoord): ", aaaCoord.equals(bbbCoord));
		assertFalse("bbbCoord.equals(aaaCoord): ", bbbCoord.equals(aaaCoord));
	}

	@Test
	public void asPrintableString_h1() {
		assertEquals("{H=1: (0,0)}", (new Coordinates (0,0)).asPrintableString());
	}

	@Test
	public void asPrintableString_h2() {
		assertEquals("{H=2: (1,2),(0,0)}", (new Coordinates (0,0)).within(1,2).asPrintableString());
	}

	@Test
	public void asPrintableString_h3() {
		assertEquals("{H=3: (2,3),(1,2),(0,0)}", (new Coordinates (0,0)).within(1,2).within(2,3).asPrintableString());
	}
}
