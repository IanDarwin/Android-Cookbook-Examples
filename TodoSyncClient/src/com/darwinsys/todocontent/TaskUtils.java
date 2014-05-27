package com.darwinsys.todocontent;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.darwinsys.todo.model.Task;

public class TaskUtils {

	public static Task cursorToTask(Cursor cur) {
		throw new IllegalStateException("write more code dude");
	}
	
	public static List<Task> cursorToTaskList(Cursor cur) {
		List<Task> ret = new ArrayList<>();
		while (cur.moveToNext()) {
			ret.add(cursorToTask(cur));
		}
		return ret;
	}
	
	public static ContentValues taskToContentValues(Task t) {
		ContentValues cv = new ContentValues();
		throw new IllegalStateException("write more code dude");
		//return cv;
	}
}
