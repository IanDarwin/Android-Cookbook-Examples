package com.darwinsys.cplist;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class CpListActivity extends ListActivity {
	
	private static final String TAG = "Cp24";
	
	ListAdapter mAdapter;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cp_list);
        
		List<ProviderInfo> providers = getPackageManager().queryContentProviders(null, 0, 0);

		for (ProviderInfo pi : providers) {
			Log.d(TAG, "Provider " + pi.name);
		}
		
		mAdapter = new ArrayAdapter<ProviderInfo>(this, R.layout.cp_list_item, providers);
		// TODO override getItem() to return just the name.

		setListAdapter(mAdapter);
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				ProviderInfo pi = (ProviderInfo) mAdapter.getItem(pos);
				Intent intent = new Intent(CpListActivity.this, CpDetailActivity.class);
				intent.putExtra("provider", pi);
				startActivity(intent);
			}
		});
    }
}