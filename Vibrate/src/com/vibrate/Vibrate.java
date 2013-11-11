package com.vibrate;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;

public class Vibrate extends Activity {
	
	// The vibration times in mSec are pairs of silent time and vibrate time.
	private static final long[] ONE_CYCLE = new long[]{100, 1000};
    private static final long[] THREE_CYCLES = new long[]{100, 1000, 1000, 1000, 1000, 1000};

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void shortVibrate(View v) {
    	vibrate(ONE_CYCLE);
    }
    
    public void longVibrate(View v) {
    	vibrate(THREE_CYCLES);
    }
    
    private void vibrate(long[] cycles) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
        Notification notification = new Notification();

        notification.vibrate = cycles; 
        notificationManager.notify(0, notification);
    }
}