package com.example.epochjscalendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/** THIS IS NOT THE REAL CREATEENTRYSCREEN, JUST A PLACEHOLDER */
public class CreateEntryScreen extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView badView = new TextView(this);
		badView.setText("We cannot find the real CreateEntryScreen; this is just a placeholder!");
		setContentView(badView);
	}
}
