package com.androidcookbook.ipcdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MyRemoteService extends Service {

    static final String TAG = MyRemoteService.class.getSimpleName();

    private IMyRemoteService.Stub myRemoteServiceStub = new IMyRemoteService.Stub() {
        public String getMessage() throws RemoteException {
            return "Hello World!";
        }
    };

    // The onBind() method in the service class:
    public IBinder onBind(Intent arg0) {
        Log.d(TAG, "onBind()");
        return myRemoteServiceStub;
    }

    // Overridden just for logging
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
    }
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart() [deprecated]");
    }
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}