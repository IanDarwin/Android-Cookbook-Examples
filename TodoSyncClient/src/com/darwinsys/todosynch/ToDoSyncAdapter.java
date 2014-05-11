package com.darwinsys.todosynch;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
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
	
	private final ContentResolver mContentResolver;

	public ToDoSyncAdapter(Context appContext, boolean b) {
		super(appContext, b);
		Log.d(TAG, "ReadingSyncAdapter.ReadingSyncAdapter()");
		mContentResolver = appContext.getContentResolver();
	}
	
	/** Alternate constructor form to maintain compatibility with Android 3.0
     * and later platform versions
     */
    public ToDoSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        mContentResolver = context.getContentResolver();
    }


	@Override
	public void onPerformSync(Account account, 
			Bundle extras, 
			String authority,
			ContentProviderClient provider, 
			SyncResult syncResult) {
		Log.d(TAG, "ReadingSyncAdapter.onPerformSync()");
		
		// TODO DO THE WORK HERE	
	}
}
