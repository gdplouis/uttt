package com.uttt.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.uttt.common.StackFrameUtil;

public class StackFrameUtilTest {

	@Test()
	public void util_whoami() {
		final String expected   = "StackFrameUtilTest.util_whoami";
		final String determined = StackFrameUtil.whoami();

		assertEquals(expected, determined);
	}
}
