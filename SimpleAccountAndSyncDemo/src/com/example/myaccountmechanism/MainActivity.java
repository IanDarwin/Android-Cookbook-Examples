package com.example.myaccountmechanism;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	static final String TAG = "AuthFake";
	
	private Account mAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "MainActivity.onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Account[] accounts = AccountManager.get(this).getAccountsByType(Constants.MY_ACCOUNT_TYPE);
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
			Toast.makeText(this, "What do I do with all these accounts? " + accounts, Toast.LENGTH_LONG).show();
			break;
		}
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
		startAccountRegistration();
	}

	/**
	 * 
	 */
	void startAccountRegistration() {
		startActivity(new Intent(this, LoginActivity.class));
	}

}
