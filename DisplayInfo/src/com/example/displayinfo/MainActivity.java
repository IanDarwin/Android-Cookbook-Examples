package com.example.displayinfo;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView log;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		log = (TextView) findViewById(R.id.logTextView);
		showInfo();	// initial display
	}
	
	/** Update the info if the screen is rotated, etc. */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		showInfo();
	}
	
	/** Compute the current info, and add it to the textarea. */
	void showInfo() {

		// Get information
		final Display display = getWindowManager().getDefaultDisplay();

		int width, height;

		// This method added in API level 13.
		Point point = new Point();
		display.getSize(point); // READ DISCLAIMER ON METHOD DOCUMENT!!
		width = point.x;
		height = point.y;
		dump("display.getSize()", width, height);

		width = display.getWidth();
		height = display.getHeight();

		dump("display.getWidth(),getHeight", width, height);

		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);

		width = metrics.widthPixels;
		height = metrics.heightPixels;
		dump("metrics.fields", width, height);
	}

	private void dump(String m, int w, int h) {
		log.append(String.format("Via %s: Width %d, Height %d%n", m, w, h));
	}

}
