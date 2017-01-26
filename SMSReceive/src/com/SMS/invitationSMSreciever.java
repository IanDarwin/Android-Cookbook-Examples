package com.SMS;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class invitationSMSreciever extends BroadcastReceiver {

	final String TAG = "BombDefusalApp";
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String message = "";
		if(bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			
			for(int i=0; i<msgs.length;i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				message = msgs[i].getMessageBody();
				Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
				
				if (msgs[i].getMessageBody().equalsIgnoreCase("Invite")) {
					//Intent myIntent = new Intent(MainMenu.this, com.bombdefusal.ReceivedSMSActivity.class);
					Intent myIntent = new Intent();
					myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					myIntent.setAction("com.example.helloandroid.INVITE");
					context.startActivity(myIntent);
				}
			}
					
		}
		
	}

}
