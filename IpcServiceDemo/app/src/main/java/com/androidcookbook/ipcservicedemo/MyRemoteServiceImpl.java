package com.androidcookbook.ipcservicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MyRemoteServiceImpl extends Service {

    // The actual implementation of the remote service
    private MyRemoteService.Stub myRemoteServiceStub = new MyRemoteService.Stub() {
        public String getMessage() throws RemoteException {
            return "Hello World!";
        }
    };


    // The onBind() method in the service class:
    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(getClass().getSimpleName(), "onBind()");
        return myRemoteServiceStub;
    }

}
