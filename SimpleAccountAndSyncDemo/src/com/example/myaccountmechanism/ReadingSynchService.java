package com.example.myaccountmechanism;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/** 
 * This will be the Data Synchronizer service, for use after the user has signed in successfully.
 * @author Ian Darwin
 */
public class MySynchService extends Service {

	private MySyncAdapter sSyncAdapter;

	@Override
	public synchronized void onCreate() {
		super.onCreate();
		sSyncAdapter = new MySyncAdapter(getApplicationContext(), true);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return sSyncAdapter.getSyncAdapterBinder();
	}

}
