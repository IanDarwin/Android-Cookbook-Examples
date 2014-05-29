package com.darwinsys.todoclient;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.darwinsys.todocontent.TodoContentProvider;

public class AddTaskActivity extends Activity {

	static final String TAG = AddTaskActivity.class.getSimpleName();
	
	private EditText mNameTF;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "AddTaskActivity.onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_task);
		mNameTF = (EditText) findViewById(R.id.taskname);
	}
	
	public void doSave(View v) {
		// If Android data were truly Object Oriented, we would create a Task here
		ContentValues cv = new ContentValues();
		cv.put("name", mNameTF.getText().toString());
		// Several other values needed!
		
		// Now save the value in the CP, from where the SyncAdapter will soon get it
		final Uri inserted = getContentResolver().insert(TodoContentProvider.TASKS_URI, cv);
		long id = ContentUris.parseId(inserted);
		Toast.makeText(this, "Created entry " + id, Toast.LENGTH_LONG).show();
		finish();
	}
}
