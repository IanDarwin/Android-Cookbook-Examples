package com.darwinsys.todo.test;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.Test;

import com.darwinsys.todo.model.Date;

public class DateSerializeTest {
	
	static final String FILENAME = "/tmp/sss"; // XXX use file.makeTempFile()

	final Date origDate = new Date(2014, 05, 06);
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws Exception {
		ObjectOutputStream os = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(FILENAME)));
        os.writeObject(origDate);
        os.close();
        
        ObjectInputStream is = new ObjectInputStream(
                new FileInputStream(FILENAME));
        Date d2 = (Date) is.readObject();
        is.close();
        System.out.println(d2);
        assertEquals(origDate.getYear(), d2.getYear());
	}

}
