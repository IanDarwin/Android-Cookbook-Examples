package com.example.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class DemoPreferenceActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.prefs);
	}

}
