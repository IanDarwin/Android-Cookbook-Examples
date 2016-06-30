package com.darwinsys.fileprovider.requesterdemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RequestingActivity extends AppCompatActivity {

    private static final int ACTION_GET_FILE = 1;
    private Intent mRequestFileIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRequestFileIntent = new Intent(Intent.ACTION_PICK);
        mRequestFileIntent.setType("text/plain");

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(mRequestFileIntent, ACTION_GET_FILE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        if (requestCode == ACTION_GET_FILE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    // get the file
                    Uri returnUri = resultIntent.getData();
                    final InputStream is = getContentResolver().openInputStream(returnUri);
                    final BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    TextView fileViewTextArea = (TextView) findViewById(R.id.fileView);
                    fileViewTextArea.setText(""); // reset each time
                    while ((line = br.readLine()) != null) {
                        fileViewTextArea.append(line);
                        fileViewTextArea.append("\n");
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "IO Error: " + e, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Request denied or canceled", Toast.LENGTH_LONG).show();
            }
            return;
        }
        // For any other activity, we can do nothing...
        super.onActivityResult(requestCode, resultCode, resultIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
