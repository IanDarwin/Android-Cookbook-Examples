package com.darwinsys.todo.model;

import java.util.ArrayList;
import java.util.List;

public class ExportToodleDo extends Export {

	public static String FIELDS =
		"'TASK','FOLDER','CONTEXT','GOAL','LOCATION','STARTDATE','STARTTIME','DUEDATE','DUETIME','REPEAT','LENGTH','TIMER','PRIORITY','TAG','STATUS','STAR','NOTE'";

	private static final String COMMA = ",", DQ = "\"";
	
	@Override
	public List<String> export(List<Task> tasks) {
		List<String> ret = super.export(tasks);
		ret.add(0, FIELDS);
		return ret;
	}

	@Override
	public String export(Task t) {
		StringBuilder sb = new StringBuilder();
		sb.append(t.name).append(COMMA)
		.append(COMMA) // folder
		.append(notNull(t.context)).append(COMMA)
		.append(notNull(t.project)).append(COMMA) // == goal
		.append(COMMA) // location
		.append(quote(t.creationDate.toString())).append(COMMA) // = startdate
		.append(quote((String)notNull(t.dueDate.toString()))).append(COMMA)
		.append(COMMA) // duetime
		.append(COMMA) // repeat
		.append(COMMA) // length
		.append(COMMA) // timer
		.append(quote(mapPriority(t.priority)))
		.append(COMMA) // tag
		.append(COMMA) // status - not used!!
		.append(COMMA) // star
		.append(COMMA) // note
		;
		return sb.toString();
	}

	private String quote(String s) {
		return '"' + s + '"';
	}

	private final Object notNull(Object s) {
		return s == null ? "" : s;
	}

	private String mapPriority(Character priority) {
		switch(priority) {
		case 'A': return "3";
		case 'B': return "2";
		case 'C': return "1";
		default: return "0";
		}
	}
}
