package com.SMS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.SMS.R;

public class ReceivedSMSActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        startService(new Intent ("com.android.PLAY"));
        setContentView(R.layout.invite);
    }
	public boolean onKeyDown(int keyCode, KeyEvent service) {
		stopService(new Intent("com.bombdefusal.START_AUDIO_SERVICE"));
		finish();
		return true;
	}
}