package com.darwinsys.todosynch;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.accounts.Account;
import android.accounts.AccountManager;
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
import android.widget.Toast;

import com.darwinsys.todo.model.Task;
import com.darwinsys.todocontent.TaskUtils;
import com.darwinsys.todocontent.TodoContentProvider;
import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.JSON.Feature;

/**
 * Android Synch Adapter for Todo List Tasks;
 * write readings to, and read readings from, the REST server.
 * @author Ian Darwin
 */
public class ToDoSyncAdapter extends AbstractThreadedSyncAdapter {
	
	private final static String TAG = "ReadingSyncAdapter";
	
	private final static String LAST_SYNC_TSTAMP = "last sync";
	
	private final ContentResolver mResolver;
	private SharedPreferences mPrefs;

	private static final String SERVER = "172.17.162.115";
	// CONTEXT may be the empty string or a directory name ending in "/"
	private static final String CONTEXT = "todoserver/";
	
	private Account mAccount;

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
		
		// Get the username and password, set there by our LoginActivity.
		final AccountManager accountManager = AccountManager.get(getContext());
		
		mAccount = account;;
		String userName = mAccount.name;
		String password = accountManager.getPassword(mAccount);
		Toast.makeText(getContext(), "Starting TODO Sync for " + userName, Toast.LENGTH_LONG).show();
		
		long tStamp = mPrefs.getLong(LAST_SYNC_TSTAMP, 0L);
		HttpClient client = new DefaultHttpClient();
		
		String authToken = "xxx xxx xxx";
		
		// First get list of items modified on the server
		try {
		final URI getUri = new URI(String.format("https://%s/%s/todo/%s/tasks"), 
				SERVER, CONTEXT, userName);
	
		// NOW SEND ANY ITEMS WE'VE MODIFIED

		final URI postUri = new URI(String.format("https://%s/todo/%s/task"), CONTEXT, "12345");
		String sqlQuery = "modified < ?";
		Cursor cur = mResolver.query(TodoContentProvider.CONTENT_URI, null, sqlQuery, 
				new String[]{Long.toString(tStamp)}, null);
		while (cur.moveToNext()) {
			Task t = TaskUtils.cursorToTask(cur);

			// Send a POST request with to upload this Task
			Log.d(TAG, "Connecting to server for " + postUri);

			HttpPost postAccessor = new HttpPost();
			postAccessor.setURI(postUri);
			postAccessor.addHeader("Content-Type", "application/json");
			postAccessor.addHeader("Accept", "application/json");
			postAccessor.addHeader("Authorization", "Token token=\"" + authToken + "\"");
			
			String json = JSON.std
					.with(Feature.PRETTY_PRINT_OUTPUT)
					.without(Feature.WRITE_NULL_PROPERTIES)
					.asString(t);
			postAccessor.setEntity(new StringEntity(json));

			// INVOKE
			HttpResponse response = client.execute(postAccessor);

			// Get the response body from the response
			HttpEntity postResults = response.getEntity();
			final String resultStr = EntityUtils.toString(postResults);

			// The server just sends it back to us with their ID field filled in.
			Task t2 = JSON.std.beanFrom(Task.class, resultStr);
			int n = mResolver.update(TodoContentProvider.CONTENT_URI, 
					TaskUtils.taskToContentValues(t2),
					"_id = ", new String[]{Long.toString(t2.getId())});
			if (n != 1) {
				Log.e(TAG, "FAILED TO UPDATE");
			}
			Log.d(TAG, "UPDATED " + t2 + ", new _ID = " + t2.getId());
		}
		
		// NOW GET ONES UPDATED ON THE SERVER
		
		// Order matters to avoid possibility of bouncing items back to the server that we just got
		HttpGet httpAccessor = new HttpGet();
		httpAccessor.setURI(getUri);
		httpAccessor.addHeader("Content-Type", "application/json");
		httpAccessor.addHeader("Accept", "application/json");
		httpAccessor.addHeader("Authorization", "Token token=\"" + authToken + "\"");
		HttpResponse getResponse = client.execute(httpAccessor);
		final HttpEntity getResults = getResponse.getEntity();
		final String tasksStr = EntityUtils.toString(getResults);
		List<Task> newToDos = JSON.std.listOfFrom(Task.class, tasksStr);
		for (Task t : newToDos) {
			mResolver.insert(TodoContentProvider.CONTENT_URI, TaskUtils.taskToContentValues(t));
			Log.d(TAG, "Downloaded this new Task: " + t);
		}
	
	} catch (Exception e) {
		Log.wtf(TAG, "ERROR in synchronization!: " + e, e);
	}
}
}
