package com.example.myaccount.model;

import java.util.ArrayList;
import java.util.List;

public class Export {
	
	public List<String> exportTasks(List<Task> tasks) {
		List<String> list = new ArrayList<String>();
		for (Task t : tasks) {
			exportTask(t);
		}
		return list;
	}

	private String exportTask(Task t) {
		// TODO XXX
		return null;
	}

}
