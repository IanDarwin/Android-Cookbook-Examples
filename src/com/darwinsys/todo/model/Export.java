package com.example.myaccount.model;

import java.util.ArrayList;
import java.util.List;

public class Export {
	
	public List<String> exportTasks(List<ToDoItem> tasks) {
		List<String> list = new ArrayList<String>();
		for (ToDoItem t : tasks) {
			exportTask(t);
		}
		return list;
	}

	private String exportTask(ToDoItem t) {
		// TODO XXX
		return null;
	}

}
