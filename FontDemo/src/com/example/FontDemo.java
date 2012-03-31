package com.example;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class FontDemo extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView v = (TextView) findViewById(R.id.FontView);	// 1
        Typeface t = Typeface.createFromAsset(getAssets(),		// 2
        		"fonts/fontdemo.ttf");
        v.setTypeface(t, Typeface.BOLD_ITALIC);					// 3
    }
}
