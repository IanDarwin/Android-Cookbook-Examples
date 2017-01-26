package com.darwinsys.telecorder;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TeleService extends Service {
	private final static String TAG = TeleService.class.getSimpleName();
	
    private BroadcastReceiver callReceiver;
    
    public TeleService() {
		Log.d(TAG, "TeleService::init()");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // Not gonna be used, but needed.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "TeleService.onStartCommand()");
		Toast.makeText(this, "Hello from onStartCommand", Toast.LENGTH_SHORT).show();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        filter.addAction("android.intent.action.PHONE_STATE");
        callReceiver = new CallReceiver();
        registerReceiver(callReceiver, filter);

        return START_NOT_STICKY;
    }
}
