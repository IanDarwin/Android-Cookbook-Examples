package com.darwinsys.fileprovider.providerdemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The back-end app, part of the FileProviderDemo.
 * There is only one file provided; in a real app there would
 * probably be a file chooser UI or other means of selecting a file.
 */
public class ProvidingActivity extends AppCompatActivity {

    private File mRequestFile;
    private Intent mResultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResultIntent = new Intent("com.darwinsys.fileprovider.ACTION_RETURN_FILE");
        setContentView(R.layout.activity_providing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // The Layout provides a text field with text like
        // "If you agree to provide the file, press the Agree button"

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provideFile();
            }
        });

        mRequestFile = new File(getFilesDir(), "secrets/demo.txt");

        // On first run of the application, create the "hidden" file in internal storage
        if (!mRequestFile.exists()) {
            mRequestFile.getParentFile().mkdirs();
            try (PrintWriter pout = new PrintWriter(mRequestFile)) {
                pout.println("This is the revealed text");
                pout.println("And then some.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The provider application has to return a Uri wrapped in an Intent,
     * along with permission to read that file.
     */
    private void provideFile() {

        // The approved target is one hard-coded file in our directory
        mRequestFile = new File(getFilesDir(), "secrets/demo.txt");
        Uri fileUri = FileProvider.getUriForFile(this,
                "com.darwinsys.fileprovider",
                mRequestFile);

        // The requester is in a different app so can't normally read our files!
        mResultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Attach that to the result Intent
        mResultIntent.setData(fileUri);

        mResultIntent.setDataAndType(fileUri, getContentResolver().getType(fileUri));

        // Set the result to be "success" + the result
        setResult(Activity.RESULT_OK, mResultIntent);
        finish();
    }
}
