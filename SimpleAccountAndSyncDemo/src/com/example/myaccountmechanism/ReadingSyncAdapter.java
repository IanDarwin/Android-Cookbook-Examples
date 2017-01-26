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
 * Android Synch Adapter for My Data.
 * write data to, and read data from, the REST server.
 * @author Ian Darwin
 *
 */
public class MySyncAdapter extends AbstractThreadedSyncAdapter {
	
	private final static String TAG = "MySyncAdapter";

	public MySyncAdapter(Context appContext, boolean b) {
		super(appContext, b);
		Log.d(TAG, "MySyncAdapter.MySyncAdapter()");
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		Log.d(TAG, "MySyncAdapter.onPerformSync()");
		
		// TODO DO THE WORK HERE	
	}
}
