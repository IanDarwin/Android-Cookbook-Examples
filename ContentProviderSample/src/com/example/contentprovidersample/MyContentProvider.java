package com.example.contentprovidersample;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

/**
 * A sample Content Provider.
 * @author Ashwini Shahapurkar, http://androidcookbook.com/Recipe.seam?recipeId=1558
 * @author Ian Darwin - fleshed out
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MyContentProvider extends ContentProvider {

	private static final String _ID_EQ_QUESTION = "_id = ?";
	MyDatabaseHelper mDatabase;
	
	/** The authority name. MUST be as listed as <provider android:authorities=...> in AndroidManifest */
	public static final String AUTHORITY = "com.example.contentprovidersample";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	
	public static final Uri ITEMS_URI = Uri.withAppendedPath(CONTENT_URI, "items");
	
	public static final String TABLE_NAME = "mydata";
	
	public static final String MIME_VND_TYPE = "vnd.example.item";
	
	private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

	private static final int ITEM = 1;
	private static final int ITEMS = 2;
	static {
		matcher.addURI(AUTHORITY, "items/#", ITEM);
		matcher.addURI(AUTHORITY, "items", ITEMS);
	}
	
	@Override
	public boolean onCreate() {
		mDatabase = new MyDatabaseHelper(getContext());
		return true;
	}
	
	@Override
	public void shutdown() {
		mDatabase.close();
		super.shutdown();
	}
	
	@Override
	public String getType(Uri uri) {
		int matchType = matcher.match(uri);
		Log.d("ReadingsContentProvider.getType()", uri + " --> " + matchType);
		switch (matchType) {
		case ITEM:
			return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MIME_VND_TYPE;
		case ITEMS:
			return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MIME_VND_TYPE;
		default:
			throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
		}
	}

	/** The C of CRUD */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(Constants.TAG, "MyContentProvider.insert()");
		switch(matcher.match(uri)) {
		case ITEM: // Fail
			throw new RuntimeException("Cannot specify ID when inserting");
		case ITEMS: // OK
			break;
		default:
			throw new IllegalArgumentException("Did not recognize URI " + uri);
		}
		
		long id = mDatabase.getWritableDatabase().insert(
				TABLE_NAME, null, values);
		uri = Uri.withAppendedPath(uri, "/" + id);
		return uri;
	}
	
	/** The R of CRUD */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.d(Constants.TAG, "MyContentProvider.query()");
		switch(matcher.match(uri)) {
		case ITEM: // OK
			selection = _ID_EQ_QUESTION;
			selectionArgs = new String[]{ Long.toString(ContentUris.parseId(uri)) };
		case ITEMS: // OK
			break;
		default:
			throw new IllegalArgumentException("Did not recognize URI " + uri);
		}
		// build the query with SQLiteQueryBuilder
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		qBuilder.setTables(TABLE_NAME);

		// query the database and get result in cursor
		final SQLiteDatabase db = mDatabase.getWritableDatabase();
		Cursor resultCursor = qBuilder.query(db,
				projection, selection, selectionArgs, null, null, sortOrder,
				null);
		resultCursor.setNotificationUri(getContext().getContentResolver(), uri);
		return resultCursor;

	}

	/** The U of CRUD */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		Log.d(Constants.TAG, "MyContentProvider.update()");
		switch(matcher.match(uri)) {
		case ITEM: // OK
			long id = ContentUris.parseId(uri);
			return mDatabase.getWritableDatabase().update(
					TABLE_NAME, values, _ID_EQ_QUESTION, new String[]{ Long.toString(id) });
		case ITEMS: // OK
			return mDatabase.getWritableDatabase().update(
					TABLE_NAME, values, selection, selectionArgs);
		default:
			throw new IllegalArgumentException("Did not recognize URI " + uri);
		}
	}
	
	/** The D of CRUD */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.d(Constants.TAG, "MyContentProvider.delete()");
		switch(matcher.match(uri)) {
		case ITEM: // OK
			long id = ContentUris.parseId(uri);
			return mDatabase.getWritableDatabase().delete(TABLE_NAME, _ID_EQ_QUESTION, new String[]{ Long.toString(id) });
		case ITEMS: // OK
			return mDatabase.getWritableDatabase().delete(TABLE_NAME, selection, selectionArgs);
		default:
			throw new IllegalArgumentException("Did not recognize URI " + uri);
		}
	}
	
	public static final String[] COLUMNS = {
		"_id", "content" 
		};
	
	/**
	 * Typical Android SQLite DB Helper class
	 */
	final static class MyDatabaseHelper extends SQLiteOpenHelper {

		public static final String DBNAME = "data_db.sqlite";
		public static final int VERSION = 1;
		
		public MyDatabaseHelper(Context context) {
			// Super's constructor arguments:
			// Context, database name, CursorFactory object (may be null), 
			// database schema version number (used to decide when to run 
			// the onUpdate() method.
			super(context, DBNAME, null, VERSION);
		}

		public void onCreate(SQLiteDatabase db) {
			createDatabase(db);
		}

		private void createDatabase(SQLiteDatabase db) {
			db.execSQL("create table " + TABLE_NAME + "(" +
					COLUMNS[0] + " integer primary key autoincrement not null, " + 
					COLUMNS[1] + " text " + 
					");");
			for (int i = 0; i < 3; i++) {
				db.execSQL("insert into " + TABLE_NAME + "(" + COLUMNS[1] + ") values ('" + "Item " + i + "')");
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			throw new IllegalStateException(
				"No versions exist yet, this should not get called.");
		}
	}

}