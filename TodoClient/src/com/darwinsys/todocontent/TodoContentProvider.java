package com.darwinsys.todocontent;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class TodoContentProvider extends ContentProvider {

	public static final String AUTHORITY = "com.darwinsys.todo";
	
	public static final int DB_VERSION = 1;
	
	public static final String TASKS_TABLE = "tasks";
	
	public static final Uri CONTENT_URI = 
		Uri.parse("content://" + AUTHORITY);
	
	public static final Uri TASKS_URI = 
			Uri.parse("content://" + AUTHORITY + "/" + TASKS_TABLE);
	
	private DatabaseHelper mDatabase;
	
	final static int ITEM = 0, ITEMS = 1;
	
	private static final UriMatcher matcher = 
		new UriMatcher(UriMatcher.NO_MATCH);

	private static final String MIME_VND_TYPE = "vnd.darwinsys.todo";

	private static final String TAG = null;
	
	static {
		matcher.addURI(AUTHORITY, TASKS_TABLE, ITEMS);
		matcher.addURI(AUTHORITY, TASKS_TABLE + "/#", ITEM);
	}

	@Override
	public boolean onCreate() {
		Log.d(TAG, "TodoContentProvider.onCreate()");
		mDatabase = new DatabaseHelper(getContext(), "tasks.db", null, DB_VERSION);
		return true;
	}
	
	@Override
	public String getType(Uri uri) {
		int matchType = matcher.match(uri);
        switch (matchType) {
            case ITEM:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MIME_VND_TYPE;
            case ITEMS:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MIME_VND_TYPE;
            default:
            	return null;
        }
	}

	@Override
	public Uri insert(Uri uri, ContentValues cv) {
		Log.d(TAG, "INSERT " + uri);
		switch(matcher.match(uri)) {
			case ITEM: case ITEMS:
				long id = mDatabase.getWritableDatabase().insertOrThrow(TASKS_TABLE, null, cv);
				return ContentUris.withAppendedId(uri, id);
			default:
				throw new IllegalArgumentException("Can't insert on uri " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] columns, String selection, 
			String[] selectionArgs,
			String orderBy) {
		Log.d(TAG, "QUERY: " + selection);
		switch(matcher.match(uri)) {
		case ITEM:
			selection = selection + "_ID = " + uri.getLastPathSegment();
			/*FALLTHROUGH*/
		case ITEMS:
			Cursor c = mDatabase.getReadableDatabase().query(TASKS_TABLE, columns, selection, selectionArgs, orderBy, null, null);
			return c;
		default:
			throw new IllegalArgumentException("Can't query on uri " + uri);
		}
	}

	@Override
	public int update(Uri uri, ContentValues cv, 
			String selection, 
			String[] selectionArgs) {
		Log.d(TAG, "Update: " + selection);
		switch(matcher.match(uri)) {
		case ITEM:
			selection = selection + "_ID = " + uri.getLastPathSegment();
			/*FALLTHROUGH*/
		case ITEMS:
			int rowCount = mDatabase.getWritableDatabase().update(
					TASKS_TABLE, cv, selection, selectionArgs);
			return rowCount;
		default:
			throw new IllegalArgumentException("Can't update on uri " + uri);
		}
	}
	
	@Override
	public int delete(Uri uri, String arg1, String[] arg2) {
		Log.d(TAG, "DELETE NOT SUPPORTED");
		return 0;
	}

	private class DatabaseHelper extends SQLiteOpenHelper {

		private final String sqlCreateTable = 
			"create table " + TASKS_TABLE + "(" +
			"_id integer primary key," + 
			"modified integer," +	// modified timestamp
			"priority character(1)," + // 'A'..'Z': how important?
			"name varchar," +		// what to do
			"creationDate DATETIME," + // when you realized you had to do it
			"project varchar," +	// what this task is part of
			"context varchar," +	// where to do it
			"dueDate DATETIME," +	// when to do it by
			"complete BOOLEAN," +	// true if done
			"completedDate DATETIME" + // When you finally got it done
			")";

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
	          db.execSQL(sqlCreateTable);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			throw new IllegalStateException("No upgrades here");
		}
		
	}

}
