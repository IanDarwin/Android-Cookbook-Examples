package com.androidcookbook.homeapp;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/** 
 * A simplistic Home Screen app built on a ListView.
 * Meant to be a form of "kiosk mode", only allowing certain apps.
 * It's ugly, man!
 * NOT A SECURE KIOSK: See http://androidcookbook/r/4124
 * As with all free software, use at own risk!!!!!
 * @author Ian Darwin
 */
public class HomeActivity extends ListActivity {
	
	private static final String TAG = "HomeScreenDemo";
	
	private static MyAppDesc mDemoAppDesc;
	/** Trust me, you really want to set this false for production versions! */
	private final boolean ALLOW_EXIT_BUTTON = true;

	/** Toy data structure to track the allowable apps */
	class MyAppDesc {
		public MyAppDesc(String name, Intent intent) {
			super();
			this.name = name;
			this.intent = intent;
		}
		String name;
		Intent intent;
	}

	/** The allowed apps */
	MyAppDesc[] progs = {
			new MyAppDesc("Phone", 
					new Intent(Intent.ACTION_DIAL, null)),
			new MyAppDesc("Web", 						// XXX Grossly insecure kiosk!
					new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"))),
			mDemoAppDesc = new MyAppDesc("App By Class", null),
	};

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);

		mDemoAppDesc.intent = getPackageManager().getLaunchIntentForPackage("com.android.browser");
		setListAdapter(adapter);
		if (!ALLOW_EXIT_BUTTON) {
			View v = findViewById(R.id.exitButton);
			v.setVisibility(View.GONE);
		}
	}
	
	/**
	 * The heart of the home app: run user's chosen other app.
	 */
	@Override
	protected void onListItemClick(final ListView l, final View v, int position, long id) {
		Log.d(TAG, "Starting activity for " + progs[position].name);
		startActivity(progs[position].intent);
	}

	private final ListAdapter adapter = new BaseAdapter() {

		@Override
		public View getView(final int position, final View convertView, final ViewGroup parent) {
			View v = convertView;

			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.app_row, null);
			}

			MyAppDesc prog = progs[position];
			TextView name = (TextView)v.findViewById(R.id.app_name);

			name.setText(prog.name);

			return v;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return progs[position];
		}

		@Override
		public int getCount() {
			return progs.length;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

	/** This method should be removed (via CodeGuard) in the 
	 * non-debugging version.
	 */
	 public void exitPressed(View v) {
		 System.exit(0);
	 }
}
