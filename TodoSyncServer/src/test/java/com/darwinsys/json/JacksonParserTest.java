package com.darwinsys.json;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.darwinsys.todo.model.Date;
import com.darwinsys.todo.model.Task;
import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.JSON.Feature;

/** We figure that Jackson probably "just works", but wanted to prove
 * that it is idempotent (or close to it) for serializing and restoring Task objects.
 * @author Ian Darwin
 */
public class JacksonParserTest {

	private static final String TASK_NAME = "Buy more chocolate";
	private Task t;
	
	@Before
	public void setUp() throws Exception {
		t = new Task();
		t.setCompletedDate(new Date(2011, 12, 13));
		t.setName(TASK_NAME);
	}

	@Test
	public void testJacksonJunior() throws Exception {
		String json = JSON.std
			    .with(Feature.PRETTY_PRINT_OUTPUT)
			    .without(Feature.WRITE_NULL_PROPERTIES)
				.asString(t);
		
		System.out.println("Your Task as a JSON string looks like this:");
		System.out.println(json);
		
		Task t2 = JSON.std.beanFrom(Task.class, json);
		assertEquals("Year from completed date", (int)2011,(int) t2.getCompletedDate().getYear());
		assertEquals("Name from restored task", TASK_NAME, t2.getName());
	}

}
