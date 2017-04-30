package com.androidcookbook.simpledialer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends Activity {
	String phoneNumber = "555-1212";
	String intentStr = "tel:" + phoneNumber;

	/** Standard creational callback.
	 * Just dial the phone
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Intent intent = new Intent("android.intent.action.DIAL",
				Uri.parse(intentStr));
		startActivity(intent);
	}
}
