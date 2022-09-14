package com.androidcookbook;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
/**
 * Simple demo of a SnackBar.
 */
public class SnackBarDemo extends Activity {

    private static final String TAG = SnackBarDemo.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
        findViewById(R.id.button).setOnClickListener(
            v -> Snackbar.make(findViewById(R.id.main_layout),
             "A SnackBar",
              Snackbar.LENGTH_LONG)
                .show());
    }
}

