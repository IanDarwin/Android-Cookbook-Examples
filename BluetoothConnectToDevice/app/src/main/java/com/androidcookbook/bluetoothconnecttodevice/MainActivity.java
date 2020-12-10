package com.androidcookbook.bluetoothconnecttodevice;

import android.app.Activity;
import android.os.Bundle;

import android.bluetooth.*;
import java.io.*;
import java.util.UUID;


public class MainActivity extends Activity {

    // Unique UUID for this application, you should use different
    private static final UUID MY_UUID = UUID
            .fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    private BluetoothAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get a BluetoothDevice as per the other demos
        // new BluetoothConnection(btDev).start();
    }

    @SuppressWarnings("unused")
    private class BluetoothConnection extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        byte[] buffer;

        public BluetoothConnection(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            // Get a BluetoothSocket for a connection with the given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
            //now make the socket connection in separate thread to avoid FC
            Thread connectionThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // Always cancel discovery because it will slow down a connection
                    mAdapter.cancelDiscovery();
                    // Make a connection to the BluetoothSocket
                    try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                        mmSocket.connect();
                    } catch (IOException e) {
                        //connection to device failed so close the socket
                        try {
                            mmSocket.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            });
            connectionThread.start();
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
                buffer = new byte[1024];
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    //read the data from socket stream
                    mmInStream.read(buffer);
                    // Send the obtained bytes to the UI Activity
                    // XXX Left as an exercise for the reader!
                } catch (IOException e) {
                    //an exception here marks connection loss
                    // XXX send message to UI Activity
                    break;
                }
            }
        }
        public void write(byte[] buffer) {
            try {
                //write the data to socket stream
                mmOutStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
