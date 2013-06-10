package com.example.myaccountmechanism;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/** 
 * This will be the Reading Synchronizer service, for use after the user has signed in successfully.
 * @author Ian Darwin
 */
public class ReadingSynchService extends Service {

	private ReadingSyncAdapter sSyncAdapter;

	@Override
	public synchronized void onCreate() {
		super.onCreate();
		sSyncAdapter = new ReadingSyncAdapter(getApplicationContext(), true);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return sSyncAdapter.getSyncAdapterBinder();
	}

}
