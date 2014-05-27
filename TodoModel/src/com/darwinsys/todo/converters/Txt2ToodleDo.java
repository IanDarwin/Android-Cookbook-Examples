package com.darwinsys.todo.converters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.darwinsys.todo.model.ExportToodleDo;
import com.darwinsys.todo.model.Import;
import com.darwinsys.todo.model.Task;

public class Txt2ToodleDo {

	public static void main(String[] args) throws Exception {
		List<Task> tasks = Import.importTasks(fileToList("/home/ian/TODO.txt"));
		final List<String> exportedTasks = new ExportToodleDo().export(tasks);
		for (String s : exportedTasks) {
			System.out.println(s);
		}
	}

	private static List<String> fileToList(String fileName) throws IOException {
		List<String> ret = new ArrayList<String>(100);
		BufferedReader is = new BufferedReader(new FileReader(fileName));
		String line;
		try {
			while ((line = is.readLine()) != null) {
				ret.add(line);
			}
		} finally {
			is.close();
		}
		return ret;
	}

}
