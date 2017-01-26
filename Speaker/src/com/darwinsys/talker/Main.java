package com.darwinsys.talker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.Button;

public class Main extends Activity implements OnInitListener {

	private TextToSpeech myTTS;
	private List<String> phrases = new ArrayList<String>();

	public void onCreate(Bundle savedInstanceState) {

		phrases.add("Hello Android, Goodbye iPhone");
		phrases.add("The quick brown fox jumped over the lazy dog");
		phrases.add("What is your mother's maiden name?");
		phrases.add("Etaoin Shrdlu for Prime Minister");
		phrases.add("The letter 'Q' does not appear in 'antidisestablishmentarianism')");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button startButton = (Button) findViewById(R.id.start_button);
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent checkIntent = new Intent();
				checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
				startActivityForResult(checkIntent, 1);	
			}
		});	
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {

			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				myTTS = new TextToSpeech(this, this);
				myTTS.setLanguage(Locale.US);
			} else {
				// TTS data not yet loaded, try to install it
				Intent ttsLoadIntent = new Intent();
				ttsLoadIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(ttsLoadIntent);
			}
		}
	}

	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			
			int n = (int)(Math.random() * phrases.size());
			myTTS.speak(phrases.get(n), TextToSpeech.QUEUE_FLUSH, null);

		} else if (status == TextToSpeech.ERROR) {
			myTTS.shutdown();
		}
	}
}
