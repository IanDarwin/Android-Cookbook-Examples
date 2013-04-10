package com.example.contentprovidersample;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * A sample Content Provider.
 * @author Ashwini Shahapurkar, http://androidcookbook.com/Recipe.seam?recipeId=1558
 * @author Ian Darwin - fleshed out
 */
public class MyContentProvider extends ContentProvider {

	MyDatabaseHelper mDatabase;
	private static final int RECORDS = 1;
	public static final String AUTHORITY = "com.example.contentprovidersample";
	public static final Uri CONTENT_URI = 
		Uri.parse("content://" + AUTHORITY);
	
	public static final String TABLE = "mydata";
	
	public static final String[] COLUMNS = { "_id", "content" };

	private static final UriMatcher matcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		matcher.addURI(AUTHORITY, "records", RECORDS);
	}
	
	@Override
	public boolean onCreate() {
		// initialize your database constructs
		return true;
	}
	
	@Override
	public String getType(Uri uri) {
		int matchType = matcher.match(uri);
		switch (matchType) {
		case RECORDS:
			return ContentResolver.CURSOR_DIR_BASE_TYPE + "/records";
		default:
			throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
		}
	}

	/** The C of CRUD */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// App-specific insertion code goes here
		// it can be as simple as follows; inserting all values in database and
		// returning the record id
		long id = mDatabase.getWritableDatabase().insert(
				MyDatabaseHelper.TABLE_NAME, null, values);
		uri = Uri.withAppendedPath(uri, "/" + id);
		return uri;
	}
	
	/** The R of CRUD */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// build the query with SQLiteQueryBuilder
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		qBuilder.setTables(MyDatabaseHelper.TABLE_NAME);
		int uriType = matcher.match(uri);

		// query the database and get result in cursor
		Cursor resultCursor = qBuilder.query(mDatabase.getWritableDatabase(),
				projection, selection, selectionArgs, null, null, sortOrder,
				null);
		resultCursor.setNotificationUri(getContext().getContentResolver(), uri);
		return resultCursor;

	}

	/** The U of CRUD */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// to be implemented
		return 0;
	}
	
	/** The D of CRUD */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// the app specific code for deleting records from database goes here
		return 0;
	}
	
	/**
	 * Typical Android SQLite DB Helper class
	 */
	final static class MyDatabaseHelper extends SQLiteOpenHelper {

		public static final String DBNAME = "data_db.sqlite";
		public static final String TABLE_NAME = "data";
		public static final int VERSION = 1;
		public static final String[] COLUMNS = { "id", "content" };
		
		public MyDatabaseHelper(Context context) {
			// Super's constructor arguments:
			// Context, database name, CursorFactory object (may be null), 
			// database schema version number (used to decide when to run 
			// the onUpdate() method.
			super(context, DBNAME, null, VERSION);
		}

		// CREATE TABLE <table-name> (column1 INTEGER PRIMARY KEY AUTOINCREMENT
		// NOT NULL, column2 TEXT);

		public void onCreate(SQLiteDatabase db) {
			createDatabase(db);
		}

		private void createDatabase(SQLiteDatabase db) {
			db.execSQL("create table " + TABLE_NAME + "(" +
					COLUMNS[0] + " integer primary key autoincrement not null, " + 
					COLUMNS[1] + " varchar " + 
					");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			throw new IllegalStateException(
					"No versions exist yet, this should not get called.");
		}
	}

}