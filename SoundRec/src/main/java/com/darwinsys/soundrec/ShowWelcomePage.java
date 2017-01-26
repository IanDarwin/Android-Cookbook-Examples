package com.darwinsys.soundrec;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class ShowWelcomePage extends Activity {
	private static final String WEB_TUTORIAL_URL =
			"http://darwinsys.com/soundrec/tutorial.html";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onboard_webpage);
		
		WebView view = (WebView) findViewById(R.id.onboard_webView);
		view.loadUrl(WEB_TUTORIAL_URL);
		
		Button b = (Button)findViewById(R.id.onboard_webDoneButton);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
}
