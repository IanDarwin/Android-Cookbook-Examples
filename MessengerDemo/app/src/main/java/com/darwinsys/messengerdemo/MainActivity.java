package com.darwinsys.messengerdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * The client side of the Messenger Demo
 */
public class MainActivity extends Activity {
    protected final static String TAG = "MessengerDemo";

    /** Messenger to send Messages to the service. */
    Messenger mService = null;

    /** Flag indicating we are bound to the service. */
    boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.hello_button).setOnClickListener(this::sayHello);
        String message = String.format("Hi from %s (pid %d, tid %d)",
                TAG, Process.myPid(), Thread.currentThread().getId());
        Log.d(TAG, message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind (or re-bind) to the service
        bindService(new Intent(this, MessengerService.class),
                mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Disconnect gracefully from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    /**
     * Standard ServiceConnection for interacting with the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We are connected; say so, and create the Messenger for communicating
            mService = new Messenger(service);
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            // Service disconnected, so record that
            mService = null;
            mBound = false;
        }
    };

    /**
     * Some interaction with the service, via sending a message
     * @param v - needed since we're called as a View.onClickListener
     */
    public void sayHello(View v) {
        if (!mBound) return;
        // Create and send a message to the service. The "what"
        // values are arbitrary, agreed upon by the service and client.
        Message msg = Message.obtain(null, MessengerService.MSG_SAY_HELLO, 0, 0);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            Toast.makeText(this, "Comms failure!: " + e, Toast.LENGTH_LONG).show();
        }
    }
}
