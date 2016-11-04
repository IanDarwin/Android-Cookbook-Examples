package com.example.marketsearch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Market Search by Intent, from http://androidcookbook.com/r/3172
 * @author Dan Fowler
 */
public class MarketSearchActivity extends Activity {

	RadioButton publisherOption;    // Option for straight search or details

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// Search button press processed by inner class
		findViewById(R.id.butSearch).setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				String searchText;
				// Reference search input
				EditText searchFor=(EditText)findViewById(R.id.etSearch);
				try {
					// URL encoding handles spaces and punctuation in search term
					searchText = URLEncoder.encode(searchFor.getText().toString(),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					searchText = searchFor.getText().toString();
				}
				// Get search option
				RadioButton searchOption=(RadioButton)findViewById(R.id.rdSearch);
				Uri intentUri;
				if (searchOption.isChecked()) {
					intentUri=Uri.parse("market://search?q=" + searchText);
				} else {
					intentUri=Uri.parse("market://details?id=" + searchText);
				} 
				Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
				try {
					MarketSearchActivity.this.startActivity(intent);
				} catch (ActivityNotFoundException anfe) {
					Toast.makeText(MarketSearchActivity.this,
						"Please install the Google Play Store App",
						Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
