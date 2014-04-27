package com.example.myaccountmechanism;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

/**
 * This is hopefully going to morph into:
 * Android Synch Adapter for eHealth Readings;
 * write readings to, and read readings from, the REST server.
 * @author Ian Darwin
 *
 */
public class ToDoSyncAdapter extends AbstractThreadedSyncAdapter {
	
	private final static String TAG = "ReadingSyncAdapter";

	public ToDoSyncAdapter(Context appContext, boolean b) {
		super(appContext, b);
		Log.d(TAG, "ReadingSyncAdapter.ReadingSyncAdapter()");
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		Log.d(TAG, "ReadingSyncAdapter.onPerformSync()");
		
		// TODO DO THE WORK HERE	
	}
}
