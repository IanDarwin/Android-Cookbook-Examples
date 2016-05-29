package com.example.cursorloaderdemo;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends ListActivity {

	private static final String TAG = "CursorLoaderDemo.MainActivity";

	private SimpleCursorAdapter mAdapter;

	private static final Uri mUri = Browser.BOOKMARKS_URI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String[] fromFields = new String[] {
				Browser.BookmarkColumns.TITLE, 
	    		Browser.BookmarkColumns.URL
		};
		int[] toViews = new int[] { android.R.id.text1, android.R.id.text2 };
		mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, 
				null, fromFields, toViews, 0);
		setListAdapter(mAdapter);

		// Prepare the loader: re-connects an existing one or reuses one.
		getLoaderManager().initLoader(0, null, new MyCallbacks(this));
	}

	class MyCallbacks implements LoaderCallbacks<Cursor> {
		Context context;

		public MyCallbacks(Activity context) {
			this.context = context;
		}

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle stuff) {
			Log.d(TAG, "MainActivity.onCreateLoader()");
			return new CursorLoader(context,
					// Normal CP query: url, proj, select, where, having
					mUri, null, null, null, null);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			// Load has finished, swap the loaded cursor into the view
			Log.d(TAG, "MainActivity.onLoadFinished(), count = " + data.getCount());
			mAdapter.swapCursor(data);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			// The end of time: set the cursor to null to prevent bad ending.
			mAdapter.swapCursor(null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
