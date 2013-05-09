package com.example.javascriptdatademo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	static final String TAG = ArrayApplication.TAG;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void showWebView(View v) {
		Intent starter = new Intent(this, WebViewActivity.class);
		startActivity(starter);
	}
}
