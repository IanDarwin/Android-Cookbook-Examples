package com.example.javascriptdatademo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {
	static final String TAG = ArrayApplication.TAG;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		
		WebView webView = (WebView) this.findViewById(R.id.webView);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		Application application = getApplication();
		webView.addJavascriptInterface(application, "android");
		((ArrayApplication) application).setActivity(this);
		webView.requestFocusFromTouch();
		webView.setWebViewClient(new WebViewClient());
		webView.setWebChromeClient(new WebChromeClient());
		webView.loadUrl("file:///android_asset/jsdatademo.html");
	}
}
