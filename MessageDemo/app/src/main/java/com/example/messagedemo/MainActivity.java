package com.example.messagedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MessageDemo";
    final static int DO_LONG_TASK_1 = 42;
    MyLooperThread mLooperThread;

    static class MyLooperThread extends Thread {
        public Handler mHandler;
        public void run() {
            Log.d(TAG, "Starting MyLooperThread.run");
            Looper.prepare();
            mHandler = new Handler() {
                public void handleMessage(Message message) {
                    Log.d(TAG, "Message Received, code = " + message.what);
                    if (message.what == DO_LONG_TASK_1) {
                        doLongRunningTask();
                    }
                }
            };
            Looper.loop();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLooperThread = new MyLooperThread();
        mLooperThread.start();
        findViewById(R.id.start_button).setOnClickListener((view) -> {
            if (mLooperThread.mHandler == null) {
                // Probably a CANTHAPPEN.
                throw new RuntimeException("race condition spotted!");
            } else {
                Message message = mLooperThread.mHandler.obtainMessage(DO_LONG_TASK_1);
                mLooperThread.mHandler.sendMessage(message);
                Log.d(TAG, "Sending message");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLooperThread.mHandler.getLooper().quit();
        Log.d(TAG, "All done!");
    }

    static void doLongRunningTask() {
        Log.d(TAG, "In long-running task");
        try {
            Thread.sleep(2500);
            Log.d(TAG, "Leaving long-running task");
        } catch (InterruptedException e) {
            // Cant Happen
            Log.d(TAG, "GAK! Interrupted in long-running task");
        }
    }
}