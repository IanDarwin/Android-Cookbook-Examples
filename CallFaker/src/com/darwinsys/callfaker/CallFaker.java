package com.darwinsys.callfaker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/*
 * This is a dummy to catch phone calls for the somewhat bizarre use case
 * of testing apps that try to place phone calls but run them on a Tablet
 * or an emulator. Don't ask, OK?
 */
public class CallFaker extends Activity {
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		Intent intent = getIntent();
		Uri callee = intent.getData();
		if (callee == null) {
			return;
		}
		View callingNumber = findViewById(R.id.number);
		((TextView) callingNumber).setText(callee.toString());
    }
}
