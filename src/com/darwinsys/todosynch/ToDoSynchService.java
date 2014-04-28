package com.darwinsys.todosynch;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/** 
 * This will be the Reading Synchronizer service, for use after the user has signed in successfully.
 * @author Ian Darwin
 */
public class ToDoSynchService extends Service {

	private ToDoSyncAdapter sSyncAdapter;
	private static final Object sLock = new Object();

	@Override
	public void onCreate() {
		super.onCreate();
		synchronized(sLock) {
			if (sSyncAdapter == null) {
				sSyncAdapter = new ToDoSyncAdapter(getApplicationContext(), true);
			}
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return sSyncAdapter.getSyncAdapterBinder();
	}

}
