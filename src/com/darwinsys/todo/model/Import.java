package com.example.myaccount.model;

import java.util.ArrayList;
import java.util.List;

public class Import {

	public List<ToDoItem> importTasks(List<String> input) {
		List<ToDoItem> list = new ArrayList<ToDoItem>();
		for (String s : input) {
			list.add(importTask(s));
		}
		return list;
	}
	
	public ToDoItem importTask(String str) {
		// XXX
		return (ToDoItem)null;
	}
}
