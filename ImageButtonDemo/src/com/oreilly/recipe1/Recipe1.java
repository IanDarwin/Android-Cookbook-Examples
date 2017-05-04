package com.oreilly.recipe1;

import com.oreilly.recipie1.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Recipe1 extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final Button playButton = (Button) findViewById(R.id.button);
		final Button settingsButton = (Button)findViewById(R.id.settings);
		
		playButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        // Perform action on clicks
		    	Toast.makeText(Recipe1.this, com.oreilly.recipie1.R.string.play_button_pressed, Toast.LENGTH_SHORT).show();
		    }
		});
		
		settingsButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        // Perform action on clicks
		    	Toast.makeText(Recipe1.this, R.string.settings_button_pressed, Toast.LENGTH_SHORT).show();
		    }
		});
    }
}