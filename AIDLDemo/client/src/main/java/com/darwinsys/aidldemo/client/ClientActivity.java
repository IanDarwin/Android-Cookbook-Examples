package com.darwinsys.aidldemo.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.darwinsys.aidldemo.AIDLDemo;
import com.darwinsys.aidldemo.Expense;

import java.time.LocalDate;

import javax.net.ssl.SSLEngineResult;

public class ClientActivity extends AppCompatActivity {
    private final static String TAG = ClientActivity.class.getSimpleName();

    /** The remote service */
    AIDLDemo remote;

    /** True if the service is bound */
    boolean bound = false;

    /** Date Textfield */
    EditText dateTF;
    /** Description textfield */
    EditText descTF;
    /** Amount textField */
    EditText amountTF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateTF = findViewById(R.id.dateTF);
        dateTF.setText(LocalDate.now().toString());
        descTF = findViewById(R.id.descTF);
        amountTF = findViewById(R.id.amountTF);

        Button b = findViewById(R.id.submit_button);
        b.setOnClickListener((v) -> {
            try {
                submitExpense();
            } catch (RemoteException ex) {
                String message = "Submit failed " + ex;
                Log.d(TAG, message);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    void submitExpense() throws RemoteException {

        if (!bound) {
            Toast.makeText(this,"bindService not bound, cannot submit!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Expense exp = new Expense();
        exp.date = LocalDate.now().toString();
        exp.description = descTF.getText().toString();
        exp.amount = Double.parseDouble(amountTF.getText().toString());

        int serverPid = remote.getPid();
        int newId = remote.submitExpense(exp);
        Toast.makeText(this,
                "Sent Expense item to process " + serverPid + " as item# " + newId,
                Toast.LENGTH_LONG).show();

        descTF.setText("");
        amountTF.setText("");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        bindToRemoteService();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        unbindFromRemoteService();
    }

    private void bindToRemoteService() {
        Intent connectIntent = new Intent();
        connectIntent.setClassName(
                "com.darwinsys.aidldemo.server",
                "com.darwinsys.aidldemo.server.ExpenseService");
        bound = bindService(connectIntent, mConnection, Context.BIND_AUTO_CREATE);

        if (!bound) {
            Toast.makeText(this,"bindService call failed! ",
                    Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "Bound to remote ExpenseService");
        }
    }

    private void unbindFromRemoteService() {
        // XXX
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        // Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, String.format("Connected to %s of %s", className, service.getClass()));
            remote = AIDLDemo.Stub.asInterface(service);
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "Service disconnected");
            remote = null;
        }
    };

}