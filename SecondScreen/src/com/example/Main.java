package com.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.nextButton).setOnClickListener(new handleButton());
    }
    class handleButton implements OnClickListener {
        public void onClick(View v) {
		    Intent intent = new Intent(Main.this, Screen2.class);
		    startActivity(intent);	
        }
    }
}