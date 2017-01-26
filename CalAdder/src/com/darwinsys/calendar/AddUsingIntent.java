package com.darwinsys.calendar;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AddUsingIntent implements EventAdder {
	public void addEvent(Context ctx, String title, Calendar start, Calendar end) {
		Log.d(MainActivity.TAG, "AddUsingIntent.addEvent()");
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("title", title);
		intent.putExtra("beginTime", start.getTimeInMillis());
		intent.putExtra("endTime", end.getTimeInMillis());
		intent.putExtra("allDay", false);
		ctx.startActivity(intent);
	}
}
