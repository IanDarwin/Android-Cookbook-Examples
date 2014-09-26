package com.example.selfupdater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UpdateActivity extends Activity {
	
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		
		String app_name = getString(R.string.app_name);
		String format = getString(R.string.update_message);
		final String formattedMessage = String.format(format, app_name);
		((TextView)findViewById(R.id.update_message)).setText(formattedMessage);
		System.err.println("MESSAGE='" + formattedMessage + "'");
		intent = getIntent();
	}
	
	public void doIt(View v) {
		intent.setComponent(null);
		intent.setAction(Intent.ACTION_VIEW);
		System.err.println("doIt: start " + intent);
		startActivity(intent);
		finish();
	}
}
