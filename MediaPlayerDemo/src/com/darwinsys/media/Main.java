package com.darwinsys.media;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener {
	protected static final String LOG_TAG = "MediaPlayerDemo";
	String fileName = "/sdcard/Surfin-Full.mp3";
	MediaPlayer mp = new MediaPlayer();
	MediaPlayer player;
	int volume_level = 10, volume_incr = 10;
	boolean done;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        View startButton = findViewById(R.id.play_button);
        startButton.setOnClickListener(this);

        View alarmButton = findViewById(R.id.alarm_button);
        alarmButton.setOnClickListener(this);       
        
    }

    @Override
    public void onClick(View v) {
    	try {
    		switch (v.getId()) {
    		case R.id.play_button:
    			playMedia(mp, fileName);
    			break;
    		case R.id.alarm_button:
    			playAlarm();
    		}
    	} catch (IOException e) {
    		Toast.makeText(this, "Media Failure: " + e, Toast.LENGTH_LONG).show();
    	}
    }
    
	private void playMedia(MediaPlayer mp, String fileName) throws IOException {
		File f = new File(fileName);
		if (!f.canRead()) {
			Toast.makeText(this, "CANNOT READ " + fileName, Toast.LENGTH_SHORT).show();
			return;
		}
		mp.setDataSource(fileName);
		mp.prepare();
		Toast.makeText(this, "Start play", Toast.LENGTH_SHORT).show();
		mp.setOnCompletionListener(new OnCompletionListener() {			
			@Override
			public void onCompletion(MediaPlayer mp) {
				Toast.makeText(Main.this, "Media Play Complete", Toast.LENGTH_SHORT).show();
			}
		});
		mp.start();
		Toast.makeText(this, "Started OK", Toast.LENGTH_SHORT).show();
	}
	
	void playAlarm() {
		AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.setTitle("Wake up time!");
		dlg.setButton(AlertDialog.BUTTON1, "I'm awake!", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				player.setOnCompletionListener(null);
				player.stop();
				player.release();
				done = true;
			}
		});
		dlg.show();
		player = MediaPlayer.create(this, R.raw.alarm_sound);
		player.setVolume(volume_level, volume_level);
		player.start();
		player.setOnCompletionListener(new OnCompletionListener() {			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				volume_level += volume_incr;
				player.setVolume(volume_level, volume_level);
				/*try {
					player.prepare();
				} catch (IOException e) {
					// Should be a CANTHAPPEN since was previously prepared!
					Log.i(LOG_TAG, "Unexpected IOException " + e);
				}*/
				player.start();
			}
		});
	}
}
