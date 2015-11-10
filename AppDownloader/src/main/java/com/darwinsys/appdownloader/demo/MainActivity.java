package com.darwinsys.appdownloader.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    final static String APK_NAME = "MyDemoApp.apk";
    public static final String TAG = MainActivity.class.getSimpleName();

	/** The URL for the target app that we want to download and install */
    public static final String HTTP_DEV_MY_INSTALLEE_URL =
            "http://darwinsys.com/tmp/" + APK_NAME;

    final String downloadPath = Environment.getExternalStorageDirectory() + "/download/";
    final File file = new File(downloadPath);
    final File outputFile = new File(file, APK_NAME);

    public void doDelete(View v) {
        if (outputFile.exists()) {
            if (outputFile.delete()) {
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to Delete", Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText(this, "NO FILE", Toast.LENGTH_SHORT).show();
        }
    }

    public void doDownload(View v) {
        new MyInstallTask().execute(HTTP_DEV_MY_INSTALLEE_URL);
    }

    public class MyInstallTask extends AsyncTask<String, Integer, Boolean> {

        /** Show spinner progresss */
        @Override
        protected void onPreExecute() {
            MainActivity.this.setProgressBarIndeterminateVisibility(true);
        }

        // Do the download
        @Override
        protected Boolean doInBackground(String... strings) {
            String apkUrl = strings[0];
            try {

                if (!outputFile.exists()) {
                    notifyFromBackground("Downloading...");
                    URL url = new URL(apkUrl);
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setDoOutput(true);
                    c.connect();

                    file.mkdirs();
                    FileOutputStream fos = new FileOutputStream(outputFile);

                    InputStream is = c.getInputStream();

                    byte[] buffer = new byte[2048];
                    int len1 = 0;
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);
                    }
                    fos.close();
                    is.close();
                }

                // Do the install
                notifyFromBackground("Installing...");
                Intent promptInstall = new Intent(Intent.ACTION_VIEW)
                        .setDataAndType(Uri.fromFile(outputFile),
                                "application/vnd.android.package-archive");
                startActivity(promptInstall);

            } catch (Exception e) {
                Log.e(TAG, "Caught unexpected IOException: " + e, e);
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            MainActivity.this.setProgressBarIndeterminateVisibility(false);
        }
    }

    /** Really should use a spinner to notify of progress; this will do for a quick demo */
    private void notifyFromBackground(final String mesg) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, mesg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
