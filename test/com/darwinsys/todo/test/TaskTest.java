package com.darwinsys.todo.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.example.myaccount.model.Task;

public class TaskTest {
	
	Task t = new Task();

	@Test
	public void testToString() {
		t.setName("Get the lead out");
		assertEquals("toString", "Get the lead out", t.toString());
	}

}
