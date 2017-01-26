package com.example.rangegraph;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
	
	private final static String TAG = "RangeGraphDemo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final int min = 140, max = 160;
		
		RangeGraph low = (RangeGraph) findViewById(R.id.lowGauge);
		if (low == null) {
			Log.wtf(TAG, "Custom view is null, did you provide a 2-arg constructor + pass both args to super?");
			return;
		}
		low.setMin(min); low.setMax(max); low.setValue(138);
		RangeGraph ok = (RangeGraph) findViewById(R.id.okGauge);
		ok.setMin(min); ok.setMax(max); ok.setValue(146);
		RangeGraph hi = (RangeGraph) findViewById(R.id.highGauge);
		hi.setMin(min); hi.setMax(max); hi.setValue(162);
	}

}
