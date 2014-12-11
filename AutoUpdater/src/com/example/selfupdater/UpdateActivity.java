package com.example.selfupdater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/** Offers the user the option to manually update the app when it has
 * been updated on a private web server
 * @author Ian Darwin
 */
public class UpdateActivity extends Activity {
	
	private static final String TAG = UpdateActivity.class.getSimpleName();
	private Intent intent;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		
		String app_name = getString(R.string.app_name);
		String format = getString(R.string.update_message);
		final String formattedMessage = String.format(format, app_name);
		((TextView)findViewById(R.id.update_message)).setText(formattedMessage);
		Log.d(TAG, "MESSAGE='" + formattedMessage + "'");
		intent = getIntent();
	}
	
	/**
	 * Start the updating process
	 * @param v View origin; unused. 
	 */
	public void doIt(View v) {
		intent.setComponent(null);
		intent.setAction(Intent.ACTION_VIEW);
		Log.d(TAG, "doIt: start " + intent);
		startActivity(intent);
		finish();
	}
}
