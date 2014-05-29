package com.darwinsys.server;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Test;

import com.darwinsys.todo.model.Task;

public class RestServiceTest extends DatabaseUsingTest {
	
	private RestService target;
	
	// Beware of using @BeforeClass - it's in superclass.

	@Before
	public void setUp() {
		super.setUpDb();
		target = new RestService();
		target.setEntityManager(entityManager);  // simulate CDI; EM created in super.
	}

	@Test
	public void testGetIndex() {
		final String index = target.getIndex();
		assertNotNull(index);
		assertTrue(index.contains("<html>"));
	}

	@Test
	public void testGetStatus() {
		final String ret = target.getStatus();
		assertTrue(ret.contains("working"));
	}

	@Test
	public void testPostNormalReading() {
		// Can't do this until we have readingEncoder in new code.
	}

	@Test
	public void testGetOneItem() {
		final EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		Task item = new Task();
		
		item.setName("Get the lead out");
		// item.setDescription("Have a plumber remove lead solder joints in all exposed pipes");
		entityManager.persist(item);
		
		transaction.commit();
		
		String ret = target.findTaskById("User123", item.getId());
		assertNotNull(ret);
		System.out.println("RestServiceTest.testGetOneItemg() -> " + ret);
	}
}
