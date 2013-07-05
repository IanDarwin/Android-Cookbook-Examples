package com.darwinsys.todo.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.pobox.cbarham.testhelpers.EqualsHashCodeTestCase;

public class TaskEqualsHashcodeTest extends EqualsHashCodeTestCase {

	@Override
	protected App createInstance() throws Exception {
		return new Task("Get Stuff Done");
	}

	@Override
	protected App createNotEqualInstance() throws Exception {
		return new App("Get MORE stuff done!");
	}

	@Test
	public void testAssertTrue() {
		assertTrue(true);
	}
}
