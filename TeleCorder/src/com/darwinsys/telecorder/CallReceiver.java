package com.darwinsys.telecorder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/** This Broadcast Receiver will be called for both incoming and outgoing calls,
 * and will try to use the Intent extras to figure out what to do.
 * @author Ian Darwin
 */
public class CallReceiver extends BroadcastReceiver {
	private final static String TAG = CallReceiver.class.getSimpleName();
	private MediaRecorder recorder;
    private File audiofile;
    private boolean recording = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		Log.d(TAG, "TeleCorder.CallReceiver.onReceive(): action = " + action);
		Bundle bundle = intent.getExtras();
		if (action.equals("android.intent.action.PHONE_STATE")) {		// INCOMING (or outgoing hangup)
			boolean wasRinging = false;
			if (bundle != null) {
				String state = bundle.getString(TelephonyManager.EXTRA_STATE);
				if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
					String inNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
					wasRinging = true;
					Toast.makeText(context, "IN : " + inNumber, Toast.LENGTH_LONG).show();
				} else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
					if (wasRinging) {
						Toast.makeText(context, "ANSWERED", Toast.LENGTH_LONG).show();
						startRecording(context);
					} else {
						Toast.makeText(context, "UNEXPECTED: OFFHOOK but not RINGING!", 
							Toast.LENGTH_LONG).show();
					}
				} else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
					wasRinging = false;
					Toast.makeText(context, "DISCONNECTED", Toast.LENGTH_LONG).show();
					if (recording) {
						stopRecording();
					}
				} else {
					Toast.makeText(context, "OTHER: " + state, Toast.LENGTH_LONG).show();
				}
			}
		} else if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {	// OUTGOING
			Log.d(TAG, "TeleCorder.CallReceiver.onReceive()" + "OUTGOING");
			if ((bundle = intent.getExtras()) != null) {
				String outNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
				Toast.makeText(context, "OUT : " + outNumber, Toast.LENGTH_LONG).show();
			}
			String state = bundle.getString(TelephonyManager.EXTRA_STATE);
			Toast.makeText(context, "STATE : " + state, Toast.LENGTH_LONG).show();
			// On certain states, call startRecording
			// on others, stopRecording
			startRecording(context);
		}
	}

	/**
	 * Begin the recording process
	 */
	private void startRecording(Context context) {
		File sampleDir = new File(Environment.getExternalStorageDirectory(), "/TeleCorder");
		if (!sampleDir.exists()) {
			sampleDir.mkdirs();
		}
		String file_name = 
				"Record" + new SimpleDateFormat("yyyy-MM-dd-hh-mm-SS", Locale.US).format(new Date());
		try {
			audiofile = File.createTempFile(file_name, ".amr", sampleDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		recorder = new MediaRecorder();
		
		// Using MediaRecorder.AudioSource.VOICE_CALL gives RTE in native code,
		// so turn on speakerphone and record from mic! Kludge du jour.
		
		AudioManager mAudioManager = (AudioManager)context.getSystemService("audio");
		mAudioManager.setSpeakerphoneOn(true);
		// recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
		recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
		recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(audiofile.getAbsolutePath());
		try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
		recorder.start();
		recording = true;
	}
	
	/**
	 * End the recording process
	 */
	private void stopRecording() {
		recorder.stop();
		recording = false;
	}
}
