package com.androidcookbook.ipcdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private boolean started = false, bound = false;
    private IMyRemoteService remoteService;
    private ServiceConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "My PID is " + Process.myPid());
    }

    private void updateServiceStatus() {
        Log.d(TAG, String.format("Status: started = %b;\n" +
                "    bound: %b\n" +
                "    remoteService: %s\n" +
                "    ServiceConnection %s\n", started, bound, remoteService, conn));
    }

    public void startService(View v){
        if (started) {
            Toast.makeText(this, "Service already started",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(this, MyRemoteService.class);
        startService(i);
        started = true;
        updateServiceStatus();
        Log.d( getClass().getSimpleName(), "startService()" );
    }

    public void bindService(View v) {
        if (conn == null) {
            conn = new RemoteServiceConnection();
            Intent intent = new Intent(this, MyRemoteService.class);
            intent.setPackage(getClass().getPackage().getName());
            bound = bindService(intent, conn, Context.BIND_AUTO_CREATE);
            if (!bound) {
                Toast.makeText(this, "bindService FAILED", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Failing intent was: " + intent);
                return;
            }
            updateServiceStatus();
            Log.d(TAG, "bindService()" );
        } else {
            Toast.makeText(this,
                    "Cannot bind - service already bound", Toast.LENGTH_SHORT).show();
        }
    }

    public void invokeService(View v) {
        if (conn == null) {
            Toast.makeText(this,
                    "Cannot invoke - service not bound", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String message = remoteService.getMessage();
            TextView t = findViewById(R.id.output);
            t.setText("Message: " + message);
            Log.d(TAG, "Service::getMessage() returned: " + message);
        } catch (RemoteException re) {
            Log.e(TAG, "RemoteException: " + re);
        }
    }

    public void releaseService(View v) {
        if(conn != null) {
            unbindService(conn);
            conn = null;
            updateServiceStatus();
            Log.d(TAG, "releaseService()" );
        } else {
            Toast.makeText(this,
                    "Cannot unbind - service not bound",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void stopService(View v) {
        if (!started) {
            Toast.makeText(this, "Service not yet started",
                    Toast.LENGTH_LONG).show();
        } else {
            Intent i = new Intent(this, MyRemoteService.class);
            stopService(i);
            started = false;
            updateServiceStatus();
            Log.d( getClass().getSimpleName(), "stopService()" );
        }
    }

    /** Inner class to implement ServiceConnection */
    class RemoteServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName className,
                                       IBinder boundService) {
            remoteService = IMyRemoteService.Stub.asInterface(boundService);
            Log.d(getClass().getSimpleName(), "onServiceConnected()");
        }

        public void onServiceDisconnected(ComponentName className) {
            remoteService = null;
            updateServiceStatus();
            Log.d(getClass().getSimpleName(), "onServiceDisconnected");
        }
    }
}
