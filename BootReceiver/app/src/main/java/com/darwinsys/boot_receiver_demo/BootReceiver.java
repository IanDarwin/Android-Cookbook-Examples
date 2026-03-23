package com.darwinsys.boot_receiver_demo;

import android.content.*;

/**
 * Boot Receiver runs once each time the device is booted.
 * Can start the Activity, update database info, etc.
 */
public class BootReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (intent != null && "android.permission.RECEIVE_BOOT_COMPLETED".equals(intent.getAction())) {

			// Get some data and save it in your database, perhaps?

			// Now start the main Activity.
            var startIntent = new Intent(context, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startIntent);
        }
    }
}
