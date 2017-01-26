package com.darwinsys.gcmclient;

import static com.google.android.gms.gcm.GoogleCloudMessaging.MESSAGE_TYPE_DELETED;
import static com.google.android.gms.gcm.GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE;
import static com.google.android.gms.gcm.GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/** Intent Service to handle each incoming GCM message */
public class GcmService extends IntentService {
	
	final static String TAG = GcmMainActivity.TAG;

	public GcmService() {
		super(GcmService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String messageType = GoogleCloudMessaging.getInstance(this).getMessageType(intent);
		Log.d(GcmMainActivity.TAG, "Got a message of type " + messageType);
		Bundle extras = intent.getExtras();
		
		if (messageType.equals(MESSAGE_TYPE_MESSAGE)) {
			// GOOD
			String message = extras.getString("message");
			Log.d(TAG, "MESSAGE = '" + message + "' (" + extras.toString() + ")");
		} else if (messageType.equals(MESSAGE_TYPE_SEND_ERROR)) {
			Log.e(TAG, "Error sending previous message (which is odd because we don't send any");
		} else if (messageType.equals(MESSAGE_TYPE_DELETED)) {
			// Too many messages for you, server deleted some
		}
		
		GcmReceiver.completeWakefulIntent(intent);
	}
}
