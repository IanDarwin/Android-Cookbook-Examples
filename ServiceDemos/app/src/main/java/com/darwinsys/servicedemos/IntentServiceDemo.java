package com.darwinsys.servicedemos;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

public class IntentServiceDemo extends IntentService {

    public static final String TAG = "IntentServiceDemo";

    public IntentServiceDemo() {
        super(TAG);
        Log.d(TAG, TAG + "<init>(): thread = " + Thread.currentThread().getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Categories: " + intent.getCategories());
        Log.d(TAG, "Data: " + intent.getData());
        Log.d(TAG, "Message: " + intent.getStringExtra("message"));
    }
}
