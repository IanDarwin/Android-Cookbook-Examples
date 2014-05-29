package com.darwinsys.todoclient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.darwinsys.authenticator.AuthConstants;
import com.darwinsys.todocontent.TodoContentProvider;

public class MainActivity extends ListActivity implements LoaderCallbacks<Cursor>{
    
    SimpleCursorAdapter mAdapter;

	public static final String TAG = MainActivity.class.getSimpleName();
	
	private Account mAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "MainActivity.onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Account[] accounts = AccountManager.get(this).getAccountsByType(AuthConstants.MY_ACCOUNT_TYPE);
		switch (accounts.length) {
		case 0:
			Toast.makeText(this, "You don't appear to be logged in to your device!", Toast.LENGTH_LONG).show();
			startAccountRegistration();
			break;
		case 1:
			mAccount = accounts[0];
			Toast.makeText(this, "Welcome, " + mAccount.name, Toast.LENGTH_LONG).show();
			break;
		default:
			Toast.makeText(this, "You have too many accounts? " + accounts, Toast.LENGTH_LONG).show();
			return;
		}
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
		Uri baseUrl = TodoContentProvider.TASKS_URI;

		// This assumes you want to see all the columns
		String[] projection = null;

		// Write code here to build a query to only select for incomplete,
		// to change the sorting order, etc.
		String select = null;
		String[] selectionArgs = null;
		String order = null;

		return new CursorLoader(this, baseUrl, projection, select, selectionArgs, order);
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
		Log.d(TAG, "onCreateOptionsMenu");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Log.d(TAG, "onMenuItemSelected: " + featureId);
		switch (featureId) {
		default:
			startAccountRegistration();
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	public void doAccounts(View v) {
		Log.d(TAG, "MainActivity.doAccounts()");
		startAccountRegistration();
	}

	/**
	 * Start the accounts activity.
	 */
	void startAccountRegistration() {
		startActivity(new Intent(this, LoginActivity.class));
	}
}
