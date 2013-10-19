package com.darwinsys.todo.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Export {
	
	public List<String> export(List<Task> tasks) {
		List<String> list = new ArrayList<String>();
		for (Task t : tasks) {
			export(t);
		}
		return list;
	}

	public abstract String export(Task t);
}
