package com.darwinsys.calendar;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	public static final String TAG = "CalAdder";
	
	Calendar mCal = Calendar.getInstance();
	String title = "Team Meeting";
	String location = "Boardroom";
	int duration = 1;	// hours
	Button addUsingContentProvider, addUsingIntent;
		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		addUsingIntent = (Button) findViewById(R.id.addUsingIntentButton);
		addUsingContentProvider = (Button) findViewById(R.id.addUsingContentProviderButton);
		
		addUsingIntent.setOnClickListener(this);
		addUsingContentProvider.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		// DatePicker allows getting values, TimePicker requires a listener,
		// so just hard code demo meeting for 1200-1300.
		DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
		final Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, dp.getYear());
		c.set(Calendar.YEAR, dp.getMonth());
		c.set(Calendar.YEAR, dp.getDayOfMonth());
		c.set(Calendar.HOUR, 12);
		final Calendar d = Calendar.getInstance();
		d.setTimeInMillis(c.getTimeInMillis());
		d.roll(Calendar.HOUR, +1);
		switch (v.getId()) {
		case R.id.addUsingIntentButton:
			// This way works on almost any release...
			new AddUsingIntent().addEvent(this, title, c, d);
			break;
		case R.id.addUsingContentProviderButton:
			// This way uses API that was added late to Android (4.0, ICS).
            // So access it using Reflection API.
			try {
				String packageName = getClass().getPackage().getName();
				EventAdder eventAdder = (EventAdder)Class.forName(packageName + ".AddUsingContentProvider").newInstance();
				eventAdder.addEvent(this, title, c, d);
			} catch (Exception e) {
				Toast.makeText(this, "Can't load AddUsingContentProvider: " + e, Toast.LENGTH_LONG).show();
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown view in onClick()");
		}
	}
}