package com.darwinsys.todo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Import {
	
	public static Pattern re = Pattern.compile(
			"(x)?( \\d{4}\\-\\d{2}\\-\\d{2})?" + // Completion flag, completion date+
			" ?" +
			"(\\([A-Z]\\))?( \\d{4}-\\d{2}-\\d{2}) ?" +		// PRIORITY, CreationDate
			".*((\\+\\w+)|(@\\w+))*.*",			// name, optional +Project, @Context in either order anywhere
					
			Pattern.COMMENTS);
			

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
			t.setName(str);
			return t;
		} else {
			throw new IllegalArgumentException("Task failed to parse: " + str);
		}
	}
}
