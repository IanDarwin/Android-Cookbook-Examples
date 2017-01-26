package com.example.localbroadcastdemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

/** A very simple demo of using LocalBroadcastManager to send Intents around within your app.
 * For simplicity the sending code and the receiving code are in the same Activity; this is
 * not the normal state of affairs.
 * @author Ian Darwin
 */
public class MainActivity extends Activity {

	private static final String TAG = "LocalBroadcastDemo";
	private IntentFilter receiveFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "MainActivity.onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		receiveFilter = new IntentFilter(getClass().getName());
		LocalBroadcastManager.getInstance(this).registerReceiver(handler, receiveFilter);
		LocalBroadcastManager.getInstance(this).registerReceiver(handler2, receiveFilter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; items go in the action bar if present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private BroadcastReceiver handler = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG,"MainActivity.handler.new BroadcastReceiver() {...}.onReceive(): " + Thread.currentThread());
			Toast.makeText(MainActivity.this, "Message received", Toast.LENGTH_LONG).show();
		}	
	};
	
	private BroadcastReceiver handler2 = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG,"MainActivity.handler.new BroadcastReceiver() {...}.onReceive(): " + Thread.currentThread());
			Toast.makeText(MainActivity.this, "Message received here too", Toast.LENGTH_LONG).show();
		}	
	};

	/** 
	 * Create and dispatch an Intent via the LocalBroadcastManager.
	 * Called from a Button with android:onClick="send"
	 */
	public void send(View v) {
		Log.d(TAG, "MainActivity.send(): " + Thread.currentThread());
		Intent sendableIntent = new Intent(getClass().getName());
		LocalBroadcastManager.getInstance(this).sendBroadcast(sendableIntent);
	}
}
