package com.darwinsys.soundrec;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * Offer a "voice note" facility so the user can record comments.
 * We use this instead of the default Sound Record app since unlike
 * the standard Video Recorder, the standard Voice Recorder app
 * blatantly ignores Intent extra "Extra Output" for filename.
 * 
 * onCreate() calls startRecording(), since the user already pressed a button
 * with "start recording" function; we offer discard and save buttons only.
 * @author Ian Darwin
 */
public class VoiceNoteActivity extends Activity implements OnClickListener {

	public static final String START_STOP_COMMAND = "startStopCommand";

	private final String TAG = "VoiceNoteActivity";
	private boolean recording;
	private Intent soundRecIntent;
	private final static int DIALOG_ABOUT = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// View has only Save and Discard buttons, 
		setContentView(R.layout.voicenote);
		View startButton = findViewById(R.id.voicenote_start_button);
		startButton.setOnClickListener(this);
		View saveButton = findViewById(R.id.voicenote_save_button);
		saveButton.setOnClickListener(this);
		View discardButton = findViewById(R.id.voicenote_discard_button);
		discardButton.setOnClickListener(this);
		
		soundRecIntent = new Intent(this, SoundRecService.class);
	}

	private void startRecording() {
		soundRecIntent.putExtra(START_STOP_COMMAND, 1);
		startService(soundRecIntent);
	}
	
	private void stopRecording() {
		soundRecIntent.putExtra(START_STOP_COMMAND, 2);
		startService(soundRecIntent);
	}
	
	private void discardRecording() {
		soundRecIntent.putExtra(START_STOP_COMMAND, 3);
		startService(soundRecIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.menu, menu);
		// Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case R.id.about:
			showDialog(DIALOG_ABOUT);
			return true;
		case R.id.exit:
			System.exit(0);
			/*NOTREACHED*/
		default:
			Toast.makeText(this, "Unhandled menu item", Toast.LENGTH_SHORT).show();
			return false;
		}
	}


	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ABOUT:
			final AlertDialog aboutDialog = new AlertDialog.Builder(this)
			.setCancelable(true)
			.setTitle(R.string.about_name)
			.setMessage(R.string.about_text)
			.setPositiveButton(R.string.about_done_button_label,
					new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Nothing to do?
						}
			}).create();
			return aboutDialog;
		default:
			Toast.makeText(this, "Unhandled dialog item", Toast.LENGTH_SHORT).show();
			return null;
		}
	}


	@Override
	public void onClick(View v) {
		int source = v.getId();
		switch(source) {
		case R.id.voicenote_start_button:
			startRecording();
			break;
		case R.id.voicenote_save_button:
			stopRecording();
			break;
		case R.id.voicenote_discard_button:
			discardRecording();
			break;
		default:
			Log.e(TAG, "Unexpected click");
		}
	}
	
	@Override
	public void onBackPressed() {
		if (recording) {
			return;			// Can't back out, must save or cancel.
		}
		super.onBackPressed();
	}

}
