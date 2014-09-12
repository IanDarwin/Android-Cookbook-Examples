package com.darwinsys.cplist;

import java.util.ArrayList;
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
	
	private static final String TAG = "Cp24"; // Toronto joke
	
	List<ProviderInfo> mProviders;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cp_list);
        
		mProviders = getPackageManager().queryContentProviders(null, 0, 0);
		List<String> names = new ArrayList<>(mProviders.size());
		for (ProviderInfo pi : mProviders) {
			Log.d(TAG, "Provider " + pi.name);
			names.add(pi.name);
		}
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.cp_list_item, names));
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				ProviderInfo pi = mProviders.get(pos);
				Intent intent = new Intent(CpListActivity.this, CpDetailActivity.class);
				intent.putExtra("provider", pi);
				startActivity(intent);
			}
		});
    }
}
