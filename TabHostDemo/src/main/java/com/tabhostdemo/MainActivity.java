package com.tabhostdemo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    int TAB_DIALOG = 0;

    public void launch(View v) {
        showDialog(0);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return new CustomDialog(this);
    }
}