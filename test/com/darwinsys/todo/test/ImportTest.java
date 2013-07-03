package com.darwinsys.todo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.darwinsys.todo.model.Import;
import com.darwinsys.todo.model.Task;

@RunWith(Parameterized.class)
public class ImportTest {

	/** This method provides data to the constructor for use in tests */
    @Parameters
    public static List<Object[]> data() {           
            final Object[][] data = new Object[][]  {
                            {new Task("Call Mom"),  "(A) Call Mom"},
                            {new Task("Call Mom"), "Call Mom"},
                            {new Task("Call Mom @Home", null, "Home"),  "Call Mom @Home"},
            };
            return Arrays.asList(data);                                     
    }
    
    private Task expected;
    private String input;
    
    public ImportTest(Task expected, String input) {
    		this.expected = expected;
    		this.input = input;
    }   

	@Test
	public void testImportTask() {
		Task actual = Import.importTask(input);
		assertNotNull("import", actual);
		System.out.println(actual.getName());
		assertEquals(expected, actual);
	}

}
