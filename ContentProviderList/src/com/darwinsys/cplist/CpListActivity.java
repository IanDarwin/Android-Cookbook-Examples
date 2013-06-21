package com.darwinsys.cplist;

import java.util.List;

import android.app.ListActivity;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CpListActivity extends ListActivity {
	
	private static final String TAG = "Cp24";

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cp_list);
        
		List<ProviderInfo> providers = getPackageManager().queryContentProviders(null, 0, 0);

		for (ProviderInfo pi : providers) {
			Log.d(TAG, "Provider " + pi.name);
		}
		
		ListAdapter adapter = new ArrayAdapter<ProviderInfo>(this, R.layout.cp_list_item, providers);

		setListAdapter(adapter);
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Toast.makeText(CpListActivity.this, 
						"You picked " + ((TextView)view).getText(), 
						Toast.LENGTH_LONG).show();
			}
		});
    }
}