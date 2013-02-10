package com.darwinsys.mapdemos;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MapsActivity extends Activity {
	
	public static final String TAG = MainActivity.TAG;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "MapsActivity.onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
}
