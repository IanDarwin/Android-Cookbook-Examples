package com.darwinsys.telecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * This might mutate into a usable call recorder, when:
 * (a) it works, and
 * (b) somebody adds call-started, call-ended broadcast receiver
 * This version uses some code from "Pratt"'s answer at
 * http://stackoverflow.com/questions/18887636/how-to-record-phone-calls-in-android/21331756#21331756
 * @author Ian Darwin
 */
public class MainActivity extends Activity {
	private final static String TAG = TeleService.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, "MainActivity.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startTeleService();
    }

	/**
	 * Start the background service to keep the app alive
	 */
	private void startTeleService() {
		Log.d(TAG, "MainActivity: Starting Service");
		Intent intent = new Intent(MainActivity.this, TeleService.class);
		startService(intent);
	}
}

