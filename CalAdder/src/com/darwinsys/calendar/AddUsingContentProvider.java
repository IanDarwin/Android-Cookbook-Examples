package com.darwinsys.calendar;

import java.util.Calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class AddUsingContentProvider implements EventAdder {
	CalendarContract contract;
	
	final static String[] CALENDAR_QUERY_COLUMNS = {
			CalendarContract.Calendars._ID,
			CalendarContract.Calendars.NAME,
			CalendarContract.Calendars.VISIBLE,
			CalendarContract.Calendars.OWNER_ACCOUNT
	};
	
	@Override
	public void addEvent(Context ctx, String title, Calendar start, Calendar end) {
		Log.d(MainActivity.TAG, "AddUsingContentProvider.addEvent()");
        
        TextView calendarList = (TextView) ((Activity) ctx).findViewById(R.id.calendarList);
        // Get list of Calendars (after Jim Blackler, http://jimblackler.net/blog/?p=151)
        ContentResolver contentResolver = ctx.getContentResolver();
        Log.d(MainActivity.TAG, "URI = " + CalendarContract.Calendars.CONTENT_URI);
        final Cursor cursor = contentResolver.query(CalendarContract.Calendars.CONTENT_URI,
        		CALENDAR_QUERY_COLUMNS, null, null, null);
        Log.d(MainActivity.TAG, "cursor = " + cursor);
        while (cursor.moveToNext()) {
        	final String _id = cursor.getString(0);
        	final String displayName = cursor.getString(1);
        	final Boolean selected = !cursor.getString(2).equals("0");
        	final String accountName = cursor.getString(3);
        	Log.d(MainActivity.TAG, "Found calendar " + accountName);
        	calendarList.append(
        			"Calendar: Id: " + _id + " Display Name: " + displayName + " Selected: " + selected + " Name " + accountName);
        }
        
        ContentValues calEvent = new ContentValues();
        calEvent.put(CalendarContract.Events.CALENDAR_ID, 1); // XXX pick)
        calEvent.put(CalendarContract.Events.TITLE, title);
        calEvent.put(CalendarContract.Events.DTSTART, start.getTimeInMillis());
        calEvent.put(CalendarContract.Events.DTEND, end.getTimeInMillis());
        calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, "Canada/Eastern");
        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, calEvent);
        
        // The returned Uri contains the content-retriever URI for the newly-inserted event, including its id
        int id = Integer.parseInt(uri.getLastPathSegment());
        Toast.makeText(ctx, "Created Calendar Event " + id, Toast.LENGTH_SHORT).show();
    }

}