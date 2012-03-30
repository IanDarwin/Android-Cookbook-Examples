package com.example.sendsms;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SendSMSActivity extends Activity {
	SendSMS mSender = new SendSMS();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void sendit(View v) {
    	boolean success = mSender.sendSMSMessage("647-000-0000",
    		// This is standard lorem-ipsum text, do not bother
    		// trying to wrap it, there's about 500 characters...
    		"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    		);
    	Toast.makeText(this, "Message sent " + (
    		success ? "successfully" : "unsuccessfully"), 
    		Toast.LENGTH_SHORT).show();
    }
}
