package com.example.stringsxml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setting Text Programmatically
        TextView tview = (TextView) findViewById(R.id.textview1);
        tview.setText("Text set with code.");
    }
}
