package org.ehealthinnovation.server;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.EntityTransaction;

import model.Patient;
import model.reading.ReadingEncoder;
import model.reading.WeightReading;

import org.junit.Before;
import org.junit.Test;

public class RestServiceTest extends DatabaseUsingTest {
	
	private ReadingHome readingBean;
	private RestService target;
	
	// Beware of using @BeforeClass - it's in superclass.

	@Before
	public void setUp() {
		target = new RestService();
		readingBean = new ReadingHome();
		target.setReadingBean(readingBean);
		super.setUp();
		readingBean.setEntityManager(entityManager);  // simulate CDI; EM created in super.
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
	public void testDateParse() {
		String data = "2012-05-29T14:00:56.000Z";
		final DateFormat serverDF = RestService.df;
		final DateFormat encoderDF = ReadingEncoder.df;
		synchronized(serverDF) {
			try {
				serverDF.parse(data);
				serverDF.parse(encoderDF.format(new Date()));
			} catch (ParseException e) {
				fail(data + " failed to parse: " + e);
			}
		}
	}

	@Test
	public void testGetOneReading() {
		final EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		Patient p = new Patient();
		String username = "fred";
		p.setLogin(username);
		p.setFirstName("Fredi");
		p.setLastName("Smith");
		entityManager.persist(p);
		entityManager.flush();	// should force generation of p.id
		System.out.println("RestServiceTest.testGetOneReading(): patient.id = " + p.getId());
		
		WeightReading w = new WeightReading();
		w.setPatient(p);
		
		long rId = System.currentTimeMillis();
		w.setRid(rId);
		w.setMeasurement(150d);
		w.setUnits("pounds");
		entityManager.persist(w);
		
		transaction.commit();
		
		String ret = target.getOneReading(username, rId);
		assertNotNull(ret);
		System.out.println("RestServiceTest.testGetOneReading() -> " + ret);
	}
}
