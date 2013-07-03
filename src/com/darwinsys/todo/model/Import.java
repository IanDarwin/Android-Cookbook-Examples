package com.darwinsys.todo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Import {
	
	public static Pattern re = Pattern.compile(
			"(x)?( \\d{4}\\-\\d{2}\\-\\d{2})?" + // Completion flag, completion date+
			" ?" +
			"(\\([A-Z]\\))?\\s*(\\d{4}-\\d{2}-\\d{2}\\s+)?" +		// PRIORITY, CreationDate
			"(.*(\\+\\w+)|(@\\w+)*.*)",			// name, optional +Project, @Context in either order anywhere	
			Pattern.COMMENTS);
	
	final static int GROUP_COMPLETED = 1;
	final static int GROUP_COMPL_DATE = 2;
	final static int GROUP_PRIO = 3;
	final static int GROUP_CREATION_DATE = 4;
	final static int GROUP_REST = 5;

	public static List<Task> importTasks(List<String> input) {
		List<Task> list = new ArrayList<Task>();
		for (String s : input) {
			list.add(importTask(s));
		}
		return list;
	}
	
	public static Task importTask(String str) {
		Matcher m = re.matcher(str);
		Task t = new Task();
		if (m.matches()) {
			for (int i = 0; i < m.groupCount(); i++) {
				System.out.println(i + " " + m.group(i));
			}
			if (m.group(GROUP_COMPLETED) != null) {
				t.setComplete(true);
			}
			String prio = m.group(GROUP_PRIO);
			if (prio != null) {
				t.setPriority(prio.charAt(1));
			}
			t.setName(m.group(GROUP_REST));
			return t;
		} else {
			throw new IllegalArgumentException("Task failed to parse: " + str);
		}
	}
}
