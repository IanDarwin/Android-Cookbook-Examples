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
	private int i;
	private EditText textField;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		textField = (EditText)findViewById(R.id.textfield);

		Button b = (Button)findViewById(R.id.go_easy);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String uri = textField.getText().toString();
				Toast.makeText(Main.this, "Starting " + uri, Toast.LENGTH_SHORT).show();
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
				String uri = textField.getText().toString();
				Toast.makeText(Main.this, "Starting " + uri, Toast.LENGTH_SHORT).show();
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW, 
							Uri.parse(uri));
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
