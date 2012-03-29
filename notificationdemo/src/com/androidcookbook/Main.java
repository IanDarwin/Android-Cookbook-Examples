package com.androidcookbook;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity {
	
	private static final int NOTIFICATION_ID = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Notification n = 
		    new Notification(R.drawable.icon, getString(R.string.noticeMe), 
			System.currentTimeMillis());
		
		Intent notIntent = new Intent(this, NotificationTarget.class);
		PendingIntent wrappedIntent = 
		PendingIntent.getActivity(this, 0, notIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		n.setLatestEventInfo(getApplicationContext(), getString(R.string.title), getString(R.string.message), wrappedIntent);
		n.flags |= Notification.FLAG_AUTO_CANCEL;
		n.flags |= Notification.DEFAULT_SOUND;
		n.flags |= Notification.DEFAULT_VIBRATE;
		n.ledARGB = 0xff0000ff;
		n.flags |= Notification.FLAG_SHOW_LIGHTS;
		
		// Now invoke the Notification Service
		String notifService = Context.NOTIFICATION_SERVICE;
		NotificationManager mgr = 
		    (NotificationManager) getSystemService(notifService);
		mgr.notify(NOTIFICATION_ID, n);
		finish();
	}
}
