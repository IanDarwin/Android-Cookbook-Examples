package com.examples;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Main extends Activity
	{

		/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.main);

				WebView webview = (WebView) this.findViewById(R.id.webview);
				WebSettings webSettings = webview.getSettings();
				webSettings.setJavaScriptEnabled(true);
				webSettings.setBuiltInZoomControls(true);
				webview.requestFocusFromTouch();
				webview.setWebViewClient(new WebViewClient());
				webview.setWebChromeClient(new WebChromeClient());

				// Load the URL
				webview.loadUrl("file:///android_asset/rgraphview.html");
			}

	}