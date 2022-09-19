package com.example.helloespressotesting;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.tvTarget);
        EditText et = (EditText) findViewById(R.id.tf);
        Button b = (Button) findViewById(R.id.startButton);
        b.setOnClickListener(v -> {
            tv.setText(et.getText());
        });
    }
}
