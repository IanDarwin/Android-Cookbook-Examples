package com.darwinsys.pdfgen;

import android.media.audiofx.EnvironmentalReverb;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final static String TAG = PDFGen.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = findViewById(R.id.button);
        b.setOnClickListener(v -> {
            File downloadDir = Environment.getExternalStoragePublicDirectory("Download");
            File file = new File(downloadDir, "output.pdf");
            View view = findViewById(R.id.view_layout);
            PDFGen.write(file, view);
            String text = "PDF File saved as " + file.getAbsolutePath();
            Log.d(TAG, text);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        });
    }
}