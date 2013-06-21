package com.example.myaccount.model;

import java.util.ArrayList;
import java.util.List;

public class Import {

	public List<Task> importTasks(List<String> input) {
		List<Task> list = new ArrayList<Task>();
		for (String s : input) {
			list.add(importTask(s));
		}
		return list;
	}
	
	public Task importTask(String str) {
		// XXX
		return (Task)null;
	}
}
