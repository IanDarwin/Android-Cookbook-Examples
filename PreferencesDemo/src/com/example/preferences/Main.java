package com.example.preferences;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

public class Main extends Activity {
    private SharedPreferences sharedPreferences;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	String message = String.format(
    			"Prefs: List %s, Name %s, Choice %b",
    			sharedPreferences.getString("listChoice", "None"),
    			sharedPreferences.getString("nameChoice", "No name"),
    			sharedPreferences.getBoolean("booleanChoice", false));
    	Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    
    public void showPrefs(View v) {
    	Intent intent = new Intent(Main.this, DemoPreferenceActivity.class);
    	startActivity(intent);
    }
}