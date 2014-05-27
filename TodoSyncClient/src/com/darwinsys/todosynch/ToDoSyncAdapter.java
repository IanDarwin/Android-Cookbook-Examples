package com.darwinsys.todosynch;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.darwinsys.todo.model.Task;
import com.darwinsys.todocontent.TaskUtils;
import com.darwinsys.todocontent.TodoContentProvider;

/**
 * Android Synch Adapter for Todo List Tasks;
 * write readings to, and read readings from, the REST server.
 * @author Ian Darwin
 *
 */
public class ToDoSyncAdapter extends AbstractThreadedSyncAdapter {
	
	private final static String TAG = "ReadingSyncAdapter";
	
	private final static String LAST_SYNC_TSTAMP = "last sync";
	
	private final ContentResolver mResolver;
	private SharedPreferences mPrefs;

	public ToDoSyncAdapter(Context appContext, boolean b) {
		super(appContext, b);
		Log.d(TAG, "ReadingSyncAdapter.ReadingSyncAdapter()");
		mResolver = appContext.getContentResolver();
		mPrefs = PreferenceManager.getDefaultSharedPreferences(appContext);
	}
	
	/** Alternate constructor form to maintain compatibility with Android 3.0
     * and later platform versions
     */
    public ToDoSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        mResolver = context.getContentResolver();
    }


	@Override
	public void onPerformSync(Account account, 
			Bundle extras, 
			String authority,
			ContentProviderClient provider, 
			SyncResult syncResult) {
		Log.d(TAG, "ReadingSyncAdapter.onPerformSync()");
		
		long tStamp = mPrefs.getLong(LAST_SYNC_TSTAMP, 0L);
		
		// First get any items modified on the server
		
		
		// now send any items we've modified
		String query = "modified < ?";
		Cursor cur = mResolver.query(TodoContentProvider.CONTENT_URI, null, query, 
				new String[]{Long.toString(tStamp)}, null);
		while (cur.moveToNext()) {
			Task t = TaskUtils.cursorToTask(cur);
			mResolver.insert(TodoContentProvider.CONTENT_URI, TaskUtils.taskToContentValues(t));
		}
	}
}
