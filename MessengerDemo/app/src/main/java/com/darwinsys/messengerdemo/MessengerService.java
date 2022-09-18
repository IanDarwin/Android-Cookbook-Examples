package com.darwinsys.messengerdemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

/**
 * The server side of the Messenger Demo
 */
 public class MessengerService extends Service {
    private final static String TAG = MainActivity.TAG;
    /**
     * Commands to the service to display messages
     */
    static final int MSG_SAY_HELLO = 1,
        MSG_SAY_GOODBYE = 2;

    /** Handler of incoming messages from clients. */
    static class IncomingHandler extends Handler {

        private final Context applicationContext;

        IncomingHandler(Context context) {
            super();
            applicationContext = context.getApplicationContext();
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    String message = String.format("Hi from %s (pid %d, tid %d)",
                            TAG, Process.myPid(), Thread.currentThread().getId());
                    Toast.makeText(applicationContext, message,
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, message);
                    break;
                case MSG_SAY_GOODBYE:
                    throw new IllegalStateException("You got here before we coded this");
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
    /** Target for clients to send messages to IncomingHandler;
     * they get a reference from onBind(). */
    Messenger mMessenger;

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        mMessenger = new Messenger(new IncomingHandler(this));
        return mMessenger.getBinder();
    }
}
