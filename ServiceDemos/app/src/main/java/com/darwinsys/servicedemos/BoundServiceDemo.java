package com.darwinsys.servicedemos;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileDescriptor;

public class BoundServiceDemo extends Service {

    private final static String TAG = "BoundServiceDemo";

    private final IBinder binder = new MyBinder();
    class MyBinder extends Binder {
        BoundServiceDemo getService() {
            Log.d(TAG, "binder::getService");
            return BoundServiceDemo.this;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "in BoundServiceDemo::onBind");
        return binder;
    }

    /** This is a placeholder for the actual public method that would do something */
    public String getMessage() {
        return "I never metacharacter I didn't like";
    }
}
