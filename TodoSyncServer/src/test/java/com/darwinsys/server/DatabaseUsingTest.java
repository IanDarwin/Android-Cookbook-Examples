package com.darwinsys.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

public abstract class DatabaseUsingTest {

	/** You can run these tests against either HSQLDB for speed
	 * or PostgreSQL to be more like the production environment
	 * (and to keep the database around in an examinable form).
	 * Does not seem worth creating a dynamic mechanism to change this.
	 */
	static final String puNameFast = "todo_hsqldb",
			puNamePersistent = "todo_pgsql";
	static final String PERSISTENCE_UNIT_NAME = puNameFast;
	protected static EntityManagerFactory factory;
	protected transient EntityManager entityManager;	// used in subclasses

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {		

		// The test configuration is in src/test/resources so it SHOULD just work
		// See http://www.murraywilliams.com/2012/04/maven-and-jpa-programming/

		if (factory == null || !factory.isOpen()) {
			try {
				factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
			} catch (Exception e) {
				System.err.println("*** FAILED TO CREATE ENTITY MANAGER!!! ***");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	@Before
	public void setUpDb() {
		System.out.println(getClass().getName() + ".setUp()");
		entityManager = factory.createEntityManager(); // must be per-thread, per-usage
		assertNotNull(entityManager);
	}

	@After
	public void tearDown() {
		System.out.println("DatabaseUsingTest.tearDown()");
		entityManager.close();
	}
}
