package com.darwinsys.todosynch;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.darwinsys.RestConstants;
import com.darwinsys.todo.model.Task;
import com.darwinsys.todocontent.TaskUtils;
import com.darwinsys.todocontent.TodoContentProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;


/**
 * Android Synch Adapter for Todo List Tasks;
 * write items to, and read items from, the REST server.
 * @author Ian Darwin
 */
public class ToDoSyncAdapter extends AbstractThreadedSyncAdapter {
	
	private final static String TAG = ToDoSyncAdapter.class.getSimpleName();
	
	private final static String LAST_SYNC_TSTAMP = "last sync";
	
	private final ContentResolver mResolver;
	private SharedPreferences mPrefs;
	
	ObjectMapper om = new ObjectMapper();
	
	public ToDoSyncAdapter(Context appContext, boolean b) {
		super(appContext, b);
		Log.d(TAG, "ToDoSyncAdapter.ToDoSyncAdapter()");
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
		Log.d(TAG, "ToDoSyncAdapter.onPerformSync()");
		
		// Get the username and password, set there by our LoginActivity.
		final AccountManager accountManager = AccountManager.get(getContext());
		
		long tStamp = mPrefs.getLong(LAST_SYNC_TSTAMP, 0L);
		
		String userName = account.name;
		String password = accountManager.getPassword(account);
		Log.d(TAG, "Starting TODO Sync for " + userName);
		
		HttpClient client = new DefaultHttpClient();
		Credentials creds = new UsernamePasswordCredentials(userName, password);        
        ((AbstractHttpClient)client).getCredentialsProvider()
        	.setCredentials(new AuthScope(RestConstants.SERVER, RestConstants.PORT), creds); 
		
		// First, get list of items modified on the server
		try {
		final URI getUri = new URI(String.format(RestConstants.PROTO + "://%s:%d/%s/%s/tasks", 
				RestConstants.SERVER, RestConstants.PORT, RestConstants.PATH_PREFIX, userName));
		Log.d(TAG, "Getting Items From " + getUri);
		HttpGet httpAccessor = new HttpGet();
		httpAccessor.setURI(getUri);
		httpAccessor.addHeader("Content-Type", "application/json");
		httpAccessor.addHeader("Accept", "application/json");
		HttpResponse getResponse = client.execute(httpAccessor);	// CONNECT
		final HttpEntity getResults = getResponse.getEntity();
		final String tasksStr = EntityUtils.toString(getResults);
		Log.d(TAG, "JSON list string is: " + tasksStr);
		List<?> newToDos = om.readValue(tasksStr, List.class);
		Log.d(TAG, "Done Getting Items, list size = " + newToDos.size());
	
		// NOW SEND ANY ITEMS WE'VE CREATED/MODIFIED, going FROM the ContentResolver
		// TO the remote sync server.

		final URI postUri = new URI(String.format(RestConstants.PROTO + "://%s/todo/%s/tasks", RestConstants.PATH_PREFIX, userName));
		String sqlQuery = "modified < ?";
		Cursor cur = mResolver.query(TodoContentProvider.TASKS_URI, null, sqlQuery, 
				new String[]{Long.toString(tStamp)}, null);
		while (cur.moveToNext()) {
			Task t = TaskUtils.cursorToTask(cur);

			// Send a POST request with to upload this Task
			Log.d(TAG, "Connecting to server for " + postUri);

			HttpPost postAccessor = new HttpPost();
			postAccessor.setURI(postUri);
			postAccessor.addHeader("Content-Type", "application/json");
			postAccessor.addHeader("Accept", "application/json");
			
			final ObjectWriter w = om.writer();
			String json = w
			  .with(SerializationFeature.INDENT_OUTPUT)
			  .without(SerializationFeature.WRAP_EXCEPTIONS)
			  .writeValueAsString(t);

			postAccessor.setEntity(new StringEntity(json));

			// INVOKE
			HttpResponse response = client.execute(postAccessor);

			// Get the response body from the response
			HttpEntity postResults = response.getEntity();
			final String resultStr = EntityUtils.toString(postResults);

			// it actually sends the URL of the new ID
			Uri resultUri = Uri.parse(resultStr);
			long id = ContentUris.parseId(resultUri);
			t.setId(id);;
			int n = mResolver.update(TodoContentProvider.CONTENT_URI, 
					TaskUtils.taskToContentValues(t),
					"_id = ", new String[]{Long.toString(id)});
			if (n != 1) {
				Log.e(TAG, "FAILED TO UPDATE");
			}
			Log.d(TAG, "UPDATED " + t + ", new _ID = " + t.getId());
		}
		
		// NOW GET ONES UPDATED ON THE SERVER
		
		// Order matters to avoid possibility of bouncing items back to the server that we just got

		for (Object o : newToDos) {
			System.out.println(o);
			Task t = om.readValue(o.toString(), Task.class);
			mResolver.insert(TodoContentProvider.CONTENT_URI, TaskUtils.taskToContentValues(t));
			Log.d(TAG, "Downloaded and inserted this new Task: " + t);
		}
	
	} catch (Exception e) {
		Log.wtf(TAG, "ERROR in synchronization!: " + e, e);
	}
}
}
