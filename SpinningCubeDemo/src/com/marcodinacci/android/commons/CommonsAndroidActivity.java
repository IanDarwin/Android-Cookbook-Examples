package com.marcodinacci.android.commons;

import com.marcodinacci.book.acb.R;

import android.app.Activity;
import android.os.Bundle;

public class CommonsAndroidActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}