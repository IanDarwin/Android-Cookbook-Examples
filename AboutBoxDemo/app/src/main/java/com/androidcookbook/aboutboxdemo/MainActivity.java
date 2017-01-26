package com.androidcookbook.aboutboxdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                AboutBox.Show(MainActivity.this);
            }
        });
    }
}
