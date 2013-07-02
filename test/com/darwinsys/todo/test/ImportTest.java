package com.darwinsys.todo.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.example.myaccount.model.Import;
import com.example.myaccount.model.Task;

public class ImportTest {

	@Test
	public void testImportTasks() {
		final List<Task> tasks = Import.importTasks(new ArrayList<String>());
		assertNotNull("import list", tasks);
		assertEquals("import list", 0, tasks.size());
	}

	@Test
	public void testImportTask() {
		Task t = Import.importTask("(A) Call Mom");
		assertNotNull("import", t);
		System.out.println(t.getName());
	}

}
