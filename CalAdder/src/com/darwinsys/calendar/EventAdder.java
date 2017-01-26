package com.darwinsys.calendar;

import java.util.Calendar;

import android.content.Context;

public interface EventAdder {
	void addEvent(Context ctx, String title, Calendar start, Calendar end);
}
