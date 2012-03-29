package com.examples;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class GraphViewActivityScreen extends Activity
	{

		@Override
		public void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.graphview);

				Button backButton = (Button) this.findViewById(R.id.backNavButton);
				backButton.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
							{
								Intent intent = new Intent(getApplicationContext(), Main.class);
								startActivity(intent);
								finish();
							}
					});

				// 
				WebView webview = (WebView) this.findViewById(R.id.webview);
				WebSettings webSettings = webview.getSettings();
				webSettings.setJavaScriptEnabled(true);
				webSettings.setBuiltInZoomControls(true);
				webview.requestFocusFromTouch();
				webview.setWebViewClient(new WebViewClient());
				webview.setWebChromeClient(new WebChromeClient());

				// Load the URL
				webview.loadUrl("file:///android_asset/flot_graph.html");
			}

		@Override
		public void onConfigurationChanged(Configuration newConfig)
			{
				super.onConfigurationChanged(newConfig);
				Toast.makeText(this, "Orientation Change", Toast.LENGTH_SHORT);
				Intent intent = new Intent(this, Main.class);
				startActivity(intent);

				// Finish current Activity
				this.finish();
			}
	}
