package com.example.dreamsdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/** 
 * Placeholder for a Settings Activity; see ../PreferencesDemo for real how-to.
 */
public class SettingsActivity extends Activity {

	private static final String TAG = DreamsDemo.TAG;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "SettingsActivity.onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dream_settings);
	}
}
