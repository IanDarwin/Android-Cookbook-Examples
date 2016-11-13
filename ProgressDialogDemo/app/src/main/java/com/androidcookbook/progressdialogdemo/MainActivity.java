package com.androidcookbook.progressdialogdemo;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Runnable {

    private TextView mComplete;
    private Button mProcessing;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mComplete = (TextView) this.findViewById(R.id.complete);
        mComplete.setText("Press the Button to start Processing");
        mProcessing = (Button) findViewById(R.id.processing);
        mProcessing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mProgressDialog = ProgressDialog.show(MainActivity.this,
                        "Please Wait", "Processing Information..", true, false);
                Thread thread = new Thread(MainActivity.this);
                thread.start();
            }
        });
    }

    @Override
    public void run() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(0);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
            mComplete.setText("Processing Finished");
        }
    };

}
