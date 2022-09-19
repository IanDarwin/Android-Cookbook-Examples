package com.darwinsys.testingwithmockito;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "TestingWithMockito";

    WorkerBee bee = new WorkerBee();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(v -> bee.process());
    }
}