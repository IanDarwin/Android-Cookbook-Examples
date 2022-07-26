package com.androidcookbook.cameraintent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

/** This simple demo shows getting results back from another Activity
 * (in this case, in another app) using the API 30-era API
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CameraLaunchingActivity";
    private final static int ACTION_TAKE_PICTURE = 123;

    private File pictureFile;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Must register this before the Activity is started, so just make it a field.
    final ActivityResultLauncher<Uri> mGetContent = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            success -> {
                // Handle the returned Uri
                if (success) {
                    final String message = getString(R.string.picture_saved) + " " + pictureFile.length();
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    Log.d(TAG, message);
                } else {
                    Log.d(TAG, "Picture taking failed");
                }
            }
    );

    public void takePicture(View v) {
        Log.d(TAG, "Starting Camera Activity");
        try {
            // Use an Intent to get the Camera app going.
            Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            // Set up file to save image into.
            File baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            pictureFile = new File(baseDir, "picture1234.jpg");
            uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", pictureFile);
            // And away we go!
			mGetContent.launch(uri);

        } catch (Exception e) {
            final String mesg = getString(R.string.cant_start_activity) + ": " + e;
            Toast.makeText(this, mesg, Toast.LENGTH_LONG).show();
            Log.d(TAG, mesg);
        }
    }
}
