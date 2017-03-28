package com.example.analytics;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Demo of one activity that has tracking enabled.
 */
public class MainActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Track the page view for the Activity
		Tracker tracker =
			((GADemoApp)getApplication()).getTracker();
		tracker.setScreenName("MainActivity");
		tracker.send(new HitBuilders.ScreenViewBuilder().build());

		/* You can track events like button clicks... */
		findViewById(R.id.actionButton).setOnClickListener(
			new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Tracker tracker =
					((GADemoApp)getApplication()).getTracker();
				tracker.send(new HitBuilders.EventBuilder(
					"Action Event", "Button Clicked").build());
			}
		});

		// Rest of the Activity initialization here...
	}
}
