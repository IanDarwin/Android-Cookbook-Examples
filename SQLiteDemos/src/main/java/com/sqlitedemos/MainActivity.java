package com.sqlitedemos;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	SQLiteDatabase mDatabase;
	
	private ListView mListView;

	public static final String TABLE_NAME = "tasks";
	public static final String ID = "_id";
	public static final String NAME = "name";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		SqlOpenHelper helper = new SqlOpenHelper(this);
		mDatabase = helper.getWritableDatabase();
		
		mListView = getListView();

		ContentValues values = new ContentValues();
		values.put(NAME, "Mangoes");
		long id = (mDatabase.insert(TABLE_NAME, null, values));
		values.put(NAME, "Pumpernickel bread");
		id = (mDatabase.insert(TABLE_NAME, null, values));
		System.out.println("Max id = " + id);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Cursor listCursor = mDatabase.query(TABLE_NAME, new String[] { ID, NAME }, null, null, null, null, NAME);
		List<Food> dataList = new ArrayList<>();
		while (listCursor.moveToNext()) {
			long foodId = listCursor.getLong(0);
			String name = listCursor.getString(1);
			Food food = new Food(foodId, name);
			dataList.add(food);
		}
		listCursor.close();
		mListView.setAdapter(new ArrayAdapter<Food>(this,
                android.R.layout.simple_list_item_1, dataList));
	}

	public class SqlOpenHelper extends SQLiteOpenHelper {
		public static final String DBNAME = "tasksdb.sqlite";
		public static final int VERSION = 1;

		public SqlOpenHelper(Context context) {
			// The constructor for the parent SQLiteOpenHelper class takes
			// three arguments: the context, the database name,
			// the CursorFactory object (which is most often null), and
			// the version number of your database schema.
			super(context, DBNAME, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			createDatabase(db);
		}

		private void createDatabase(SQLiteDatabase db) {
			db.execSQL("create table " + TABLE_NAME + "(" + ID + " integer primary key autoincrement not null, " + NAME
					+ " text " + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int v1, int v2) {
			throw new UnsupportedOperationException("No upgrade yet");
		}
	}

	public class Food {

		long id;
		String name;

		public Food() {
			// Empty
		}

		public Food(long id, String name) {
			this.id = id;
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}