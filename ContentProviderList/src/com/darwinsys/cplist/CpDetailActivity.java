package com.darwinsys.cplist;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class CpDetailActivity extends Activity {

    private static final String TAG = CpDetailActivity.class.getSimpleName();

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cp_detail);
        Intent intent = getIntent();
		ProviderInfo pi = (ProviderInfo)intent.getParcelableExtra("provider");
		Log.d(TAG, "PI = " + pi.name);
		((TextView)findViewById(R.id.pi_name)).setText(pi.name);
		
		// TODO extend the layout, and Strings.xml, as per pi.name, for the following fields.
		Object o = pi.applicationInfo;
		Object o2 = pi.authority;
		boolean enabled = pi.enabled;
		Object o4 = pi.exported;
		boolean grantUriPerms = pi.grantUriPermissions;
		boolean syncable = pi.isSyncable;
		Object md = pi.metaData;
		// etc.
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cp_detail, menu);
        return true;
    }
    
}
