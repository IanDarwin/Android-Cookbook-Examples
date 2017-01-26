package com.androidcookbook.ipcservicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String SVC_PKG_NAME = MyRemoteService.class.getPackage().getName();
    private final static String SVC_CLASS_NAME = MyRemoteServiceImpl.class.getName();

    private boolean started = false;
    private ServiceConnection conn;
    private MyRemoteService remoteService;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView)findViewById(R.id.message);
    }

    public void updateServiceStatus() {
        Log.d(TAG, "UpdateServiceStatus()");
    }

    public void startService(View v){
        if (started) {
            Toast.makeText(this, "Service already started",
                    Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent();
            i.setClassName(SVC_PKG_NAME, SVC_CLASS_NAME);
            startService(i);
            started = true;
            updateServiceStatus();
            Log.d(TAG, "startService()" );
            mTextView.setText("");
        }
    }

    public void bindService(View v) {
        if (conn == null) {
            conn = new RemoteServiceConnection();
            Intent i = new Intent();
            i.setClassName(SVC_PKG_NAME, SVC_CLASS_NAME);
            bindService(i, conn, Context.BIND_AUTO_CREATE);
            updateServiceStatus();
            Log.d(TAG, "bindService()" );
        } else {
            Toast.makeText(this,
                    "Cannot bind - service already bound", Toast.LENGTH_SHORT).show();
        }
    }

    class RemoteServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName className,
                                       IBinder boundService ) {
            remoteService = MyRemoteService.Stub.asInterface((IBinder)boundService);
            Log.d(TAG, "onServiceConnected()" );
        }

        public void onServiceDisconnected(ComponentName className) {
            remoteService = null;
            updateServiceStatus();
            Log.d(TAG, "onServiceDisconnected" );
        }
    };

    public void invokeService(View v) {
        if(conn == null) {
            Toast.makeText(this,
                    "Cannot invoke - service not bound", Toast.LENGTH_SHORT).show();
        } else {
            try {
                String message = remoteService.getMessage();
                TextView t = (TextView)findViewById(R.id.message);
                t.setText("Message: "+message);
                Log.d(TAG, "invokeService()" );
            } catch (RemoteException re) {
                Log.e(TAG, "RemoteException" );
            }
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
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void stopService(View v) {
        if (!started) {
            Toast.makeText(this, "Service not yet started",
                    Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent();
            i.setClassName(SVC_PKG_NAME, SVC_CLASS_NAME);
            stopService(i);
            started = false;
            updateServiceStatus();
            Log.d(TAG, "stopService()" );
            mTextView.setText("");
        }
    }
}
