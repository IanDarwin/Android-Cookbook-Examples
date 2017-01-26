package com.darwinsys.gcmclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmReceiver extends WakefulBroadcastReceiver {
	/** 
	 * Called when a message is received from GCM for this App
	 */
    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.d(GcmMainActivity.TAG, "GcmReceiver.onReceive()");
        // Recycle "intent" into an explicit intent for the handler
        ComponentName comp = 
        	new ComponentName(context.getPackageName(), GcmService.class.getName());
        intent.setComponent(comp);
        
        // Pass control to the handler; using "startWakefulService" will keep the device awake so the user
        // has a good chance of seeing the message; the WakeLock is released at the end of the handler.
        // Re-using the incoming intent this way lets us pass along Intent.extras etc.
        startWakefulService(context, intent);
        
        // If we didn't throw an exception yet, life is good.
        setResultCode(Activity.RESULT_OK);
    }
}
