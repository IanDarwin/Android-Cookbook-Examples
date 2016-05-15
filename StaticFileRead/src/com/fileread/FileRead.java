package com.fileread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.webkit.WebView;

public class FileRead extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);

		// Read a static text file line-by-line
    	InputStreamReader is = new InputStreamReader(
			this.getResources().openRawResource(R.raw.samplefile));
    	BufferedReader reader = new BufferedReader(is);
    	StringBuilder finalText = new StringBuilder();
    	String line;
    	try {
			while ((line = reader.readLine()) != null) {
			    finalText.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Install the text file's contents into a TextView
    	TextView fileTextView = (TextView)findViewById(R.id.fileText);
    	fileTextView.setText(finalText.toString());

		// Secondly, load a static text file directly into a WebView
		WebView webView = (WebView)findViewById(R.id.webView);
		webView.loadUrl("file:///android_asset/samplefile.html");
    }
}
