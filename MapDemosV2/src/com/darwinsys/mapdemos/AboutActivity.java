package com.darwinsys.mapdemos;

import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

/*
 * Displays information about the application
 */
public class AboutActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		
		WebView aboutView = (WebView)findViewById(R.id.about_content);
		aboutView.loadUrl("file:///android_asset/about.html");
		
		TextView tv = (TextView)findViewById(R.id.about_version_tv);
		tv.setText(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this));		
	}
	
	public void done(View v) {
		finish();
	}
}
