package com.example.emailtextview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {

	private static final String TAG = "EmailTextView.MainActivity";

	private Button emailButton;
	private EditText emailText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the View Layer
		setContentView(R.layout.activity_main);

		// Get reference to Email Button
		emailButton = (Button) this.findViewById(R.id.emailButton);

		// Sets the Event Listener onClick
		emailButton.setOnClickListener(this);

		emailText = (EditText) findViewById(R.id.text_to_send);
	}

	@Override
	public void onClick(View view) {

		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("text/html");
		emailIntent.putExtra(android.content.Intent.EXTRA_TITLE, "My Title");
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Subject");

		// Obtain reference to String and pass it to Intent
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
			emailText.getText());

		// Start the user's email client.
		startActivity(emailIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
