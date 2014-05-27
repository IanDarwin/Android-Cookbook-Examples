package com.darwinsys.todo.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.darwinsys.todo.model.Date;

public class DateTest {

	private static final String DATE_STRING = "2013-06-01";
	Date d = new Date(2013,06,01);

	@Test
	public void testDateIntIntInt() {
		assertEquals(2013, d.getYear());
		assertEquals(06, d.getMonth());
		assertEquals(01, d.getDay());
	}
	
	@Test
	public void testDateString() {
		Date dd = new Date(DATE_STRING);
		assertEquals(d, dd);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDateBadString() {
		Date dd = new Date(DATE_STRING.replace("06","June"));
		assertEquals(d, dd);
	}

	@Test
	public void testDateDate() {
		java.util.Date jud = new java.util.Date();
		Date myDate = new Date(jud);
		assertTrue(myDate.getYear() == 1900 + jud.getYear());
		assertTrue(myDate.getMonth() == 1 + jud.getMonth());
	}

	@Test
	public void testGetDate() {
		java.util.Date jud = d.asJULDate();
		assertEquals(2013, jud.getYear());
		assertEquals(6-1, jud.getMonth());
	}

	@Test
	public void testToString() {
		assertEquals(DATE_STRING, d.toString());
	}
}
