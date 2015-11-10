package com.darwinsys.soundrec;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

/**
 * Sound Recording Service, originally forked from jpstrack.android's VoiceNoteActivity.
 * @author Ian Darwin
 */
public class SoundRecService extends Service {

	private final String TAG = "SoundRecService";
	MediaRecorder recorder  = null;
	private String soundFile;
	
	@Override
	public void onCreate() {

		Log.d(TAG, "SoundRecService.onHandleIntent()");
		if (!isSdWritable()) {
			Toast.makeText(this, "SD Card not writable", Toast.LENGTH_LONG).show();
			return;
		}
	}
	

	private boolean isSdWritable() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		int mode = intent.getIntExtra(VoiceNoteActivity.START_STOP_COMMAND, 0);
		switch (mode) {
		case 1:
			startRecording(intent, startId);
			return START_STICKY;
		case 2:
			saveRecording();
			return START_STICKY;
		case 3:
			discardRecording();
			return START_STICKY;
		default:
			throw new IllegalStateException();
		}
	}

	protected void startRecording(Intent intent, int startId) {
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		try {
			Uri soundUri = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
			Log.d(TAG, "SoundUri = " + soundUri);
			if (soundUri == null) {
				String DIRNAME = "/mnt/sdcard/soundrec";
				new File(DIRNAME).mkdirs();
				soundFile = DIRNAME + "/sample" + startId + ".mp3";
			} else {
				soundFile = soundUri.getPath();
			}
			recorder.setOutputFile(soundFile);
			Log.d(TAG, "outputting to " + soundFile);
			recorder.prepare();
			recorder.start();
			Toast.makeText(this, "Started...", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			final String message = "Could not create file:" + e;
			Log.e(TAG, message);
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		}
	}
	
	private void discardRecording() {
		if (recorder == null) {
			return;
		}

		recorder.stop();
		recorder.release();
		new File(soundFile).delete();
		Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT).show();
	}

	private void saveRecording() {
		if (recorder == null) {
			return;
		}
		recorder.stop();
		recorder.release();
		Toast.makeText(this, "Saved voice note into " + soundFile, Toast.LENGTH_SHORT).show();
		// We don't tell the MediaStore about it as it's not music!
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// Not used in this application.
		return null;
	}
}
