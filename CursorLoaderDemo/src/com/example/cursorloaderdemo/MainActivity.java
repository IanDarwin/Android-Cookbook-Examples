package com.example.cursorloaderdemo;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends ListActivity implements LoaderCallbacks<Cursor>{
	
	SimpleCursorAdapter mAdapter;
	
	String mSearchType;
	
	Integer year, month, day;

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	private static final String TAG = "CursorLoaderDemo.MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, null,
                new String[] { },
                new int[] { android.R.id.text1, android.R.id.text2 }, 0);
        setListAdapter(mAdapter);

		// Prepare the loader.  Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle stuff) {
		Log.d(TAG, "MainActivity.onCreateLoader()");
		Uri baseUrl = Browser.BOOKMARKS_URI;
		StringBuilder sb = new StringBuilder();
		if (mSearchType != null) {
			sb.append("'discriminator = '").append(mSearchType).append("'");
		}
		if (year != null) {
			if (sb.length() > 0) {
				sb.append(" AND ");
			}
			sb.append(String.format("creationTime like '%04d-%02d-%02d%%;", year, month));
		}
		String select = sb .toString();
		String[] projection = null;
		return new CursorLoader(this, baseUrl, projection, select, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
