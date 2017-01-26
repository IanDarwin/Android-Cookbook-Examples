package com.androidcookbook.oauth2demo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.darwinsys.net.ConversationURL;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String AUTHDOM_RO = "View your tasks",
        AUTHDOM_RW = "Manage your tasks";
    private String authToken;
    private final static int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 1;

    String our_api_key, our_client_id, our_client_secret;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Someday that button will add tasks", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getTasks(View v) {
        getTasks();
    }

    public void getTasks() {

        Log.d(TAG, "MainActivity.getTasks()");

        AccountManager am = AccountManager.get(this);

        // We really need GET_ACCOUNT permission, so we can get the Google account
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.GET_ACCOUNTS},
                MY_PERMISSIONS_REQUEST_GET_ACCOUNTS);

            return; // Will have to try later.
        }
        final Account[] accounts = am.getAccounts();
        if (accounts.length == 0) {
            Toast.makeText(this, "Please create a Google account before using this app", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        for (Account acct : accounts) {
            Log.d(TAG, "Account " + acct.name + "(" + acct.type + ")");
            if (acct.type.equals("com.google")) {

                Bundle options = new Bundle();

                am.invalidateAuthToken(acct.type, authToken);

                // Get the auth authToken. Doesn't return a value; find it in the callbacks
                am.getAuthToken(
                        acct,                        // Android "Account" for Google access
                        AUTHDOM_RO,                     // Google-provided Auth scope for viewing tasks
                        options,                        // If any authenticator-specific options needed, pass here
                        MainActivity.this,
                        new OnTokenAcquired(),          // Success Callback
                        new Handler(new OnError()));    // Failure Callback
                break;
            }
        }
    }

    /**
     * This is part of getting permission just to access the user's Google account
     *
     * @param requestCode Passed in
     * @param permissions The list of permissions we asked for (only 1)
     * @param grantResults The results (only 1)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_ACCOUNTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Success, do the operation
                    getTasks();

                } else {
                    // Nothing we can do in the "permission denied" case??
                }
                return;
            default:
                Log.d(TAG, "Unexpected requestCode " + requestCode);
        }
    }

    /**
     * Callback indicates that OAuth2 authenticator has got an Auth Token for us,
     * so save it in a member property for use in the REST service code,
     * and invoke same.
     */
    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            Log.d(TAG, "OnTokenAcquired.run()");
            // Get the result of the operation from the AccountManagerFuture.
            try {
                Bundle bundle = result.getResult();
                // The authToken is a named value in the bundle. The name of the value
                // is stored in the constant AccountManager.KEY_AUTHTOKEN.
                authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                Log.d(TAG, "Got this authToken: " + authToken);

                doFetchTaskList();

            } catch (OperationCanceledException e) {
                // Nothing to do
            } catch (IOException e) {
                // handle this error!
                e.printStackTrace();
            } catch (AuthenticatorException e) {
                // handle this error!
                e.printStackTrace();
            }
        }
    }

    private class OnError implements Handler.Callback {
        @Override
        public boolean handleMessage(Message message) {
            Log.wtf(TAG, "ERROR: " + message.toString());
            Toast.makeText(MainActivity.this, message.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * Finally! We have an Auth Token, so go ahead and invokve the service
     */
    void doFetchTaskList() {
        // Now we have to fetch the list of Tasks for this user
        try {
            URL url =
                new URL("https://www.googleapis.com/tasks/v1/users/@me/lists/@default/tasks?key=" + our_api_key);
            Map<String,String> headers = new HashMap<>();
            headers.put("accept", "text/json");
            headers.put("authorization", "OAuth " + authToken);
            headers.put("client_id", our_client_id);
            headers.put("client_secret", our_client_secret);
            final String tasks = ConversationURL.converse(url, null, headers);
            Log.d(TAG, "Got this list of tasks: " + tasks);
            // Convert to List<Task> using some JSON API
            // Display list in a RecyclerView or ListView
        } catch (IOException e) {
            throw new RuntimeException("Get task list failed: " + e, e);
        }
    }
}
