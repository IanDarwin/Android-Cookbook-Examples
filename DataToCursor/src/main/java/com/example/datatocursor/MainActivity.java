package com.example.datatocursor;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

/**
 * A trivial ListActivity with a SimpleCursorAdapter to demonstrate our
 * "data to Cursor" class in action. This use of
 * SimpleCursorAdapter is deprecated now but will suffice as a proof
 * of concept of our Cursor generator.
 */
public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Cursor c = new DataToCursor();
		String[] fromColumns = { c.getColumnName(1), c.getColumnName(2) };
		int[] toViews = { android.R.id.text1, android.R.id.text2 };
		@SuppressWarnings("deprecation")
		ListAdapter adapter = 
			new SimpleCursorAdapter(this, 
			android.R.layout.simple_list_item_2, c, fromColumns, toViews);
		getListView().setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
