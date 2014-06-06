package com.example.emailtextview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private static final String tag = "Main";
	private Button emailButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);

			// Set the View Layer
			setContentView(R.layout.activity_main);

			// Get reference to Email Button
			this.emailButton = (Button) this.findViewById(R.id.emailButton);

			// Sets the Event Listener onClick
			this.emailButton.setOnClickListener(this);

		}

	@Override
	public void onClick(View view) {
			if (view == this.emailButton) {
					Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
					emailIntent.setType("text/html");
					emailIntent.putExtra(android.content.Intent.EXTRA_TITLE, "My Title");
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Subject");

					// Obtain reference to String and pass it to Intent
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.my_text));
					startActivity(emailIntent);
				}
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
