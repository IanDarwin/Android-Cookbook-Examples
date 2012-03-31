package com.example.openurl;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main extends Activity {
	int i;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button b = (Button)findViewById(R.id.go_easy);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText textField = (EditText)findViewById(R.id.textfield);
				String uri = textField.getText().toString();

				try {
					Intent intent = new Intent(Intent.ACTION_VIEW, 
							Uri.parse(uri));
					startActivity(intent);
				} catch (Exception e) {
					Toast.makeText(Main.this, "Error: " + e, Toast.LENGTH_LONG);
				}
			}
		});
		
		Button b2 = (Button)findViewById(R.id.go_results);
		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText textField = (EditText)findViewById(R.id.textfield);
				String data = textField.getText().toString();

				try {
					Intent intent = new Intent(Intent.ACTION_VIEW, 
							Uri.parse(data));

					startActivityForResult(intent, ++i);
				} catch (Exception e) {
					Toast.makeText(Main.this, "Error: " + e, Toast.LENGTH_LONG);
				}
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		Toast.makeText(Main.this,
				String.format("Activity %d result %d", requestCode, resultCode), 
				Toast.LENGTH_LONG);
	}
}