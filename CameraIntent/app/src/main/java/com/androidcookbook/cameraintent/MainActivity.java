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

import java.io.File;

public class MainActivity extends Activity {
    private static final String TAG = "CameraLaunchingActivity";
    private final static int ACTION_TAKE_PICTURE = 123;

    private File pictureFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void takePicture(View v) {
        Log.d(TAG, "Starting Camera Activity");
        try {
            // Use an Intent to get the Camera app going.
            Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Set up file to save image into.
            File baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File pictureFile = new File(baseDir, "picture1234.jpg");
            imageIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(pictureFile));
            // And away we go!
            // startActivityForResult(imageIntent, ACTION_TAKE_PICTURE);
			ActivityResultLauncher<String> mGetContent = registerForActivityResult(new GetContent(),
				uri -> {
					// Handle the returned Uri
					final String message = getString(R.string.picture_saved) + " " + uri;
					Log.d(TAG, message);
					Toast.makeText(this, message, Toast.LENGTH_LONG).show();
				}
			);
			mGetContent.launch();
        } catch (Exception e) {
            Toast.makeText(this,
                    getString(R.string.cant_start_activity) + ": " + e,
                    Toast.LENGTH_LONG).show();
        }
    }
}
