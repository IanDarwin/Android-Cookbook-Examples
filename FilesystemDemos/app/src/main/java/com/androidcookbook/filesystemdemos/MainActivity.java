package com.androidcookbook.filesystemdemos;

import java.io.*;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	final static String DATA_FILE_NAME = "sample.txt";
	final static String message = "Hello, world!";
	private TextView infoBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		infoBox = (TextView) findViewById(R.id.infobox);

		try (FileOutputStream os = openFileOutput(DATA_FILE_NAME, Context.MODE_PRIVATE)) {
			os.write(message.getBytes());
			infoBox.append("Wrote the string " + message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		File where = getFilesDir(); // Absolute path to directory for our app's internal storage
		infoBox.append("Our private dir is " + where.getAbsolutePath() + "\n");

		try (BufferedReader is = new BufferedReader(new InputStreamReader(openFileInput(DATA_FILE_NAME)))) {
			String line = is.readLine();
			infoBox.append("Read the string " + line + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Let's save the file in our temporary "cache directory" instead.
		// Such files *might* get creamed by Android if it needs the disk space
		// We'd need to keep our usage here to some reasonable amount by pruning old caches.

		File cacFile = new File(getCacheDir(), "myCache.dat");
		infoBox.append("A cache file is at " + cacFile.getAbsolutePath() + "\n");

		File tmpDir = getDir("tmp2", 0);	// Creates and/or returns Dir within FilesDir
		infoBox.append("A sub-folder is at " + tmpDir.getAbsolutePath() + "\n");
		try {
			final boolean newFile = new File(tmpDir, "x").createNewFile();
			infoBox.append("Created file x" + "\n");
		} catch (IOException e) {
			e.printStackTrace();
			infoBox.append("Failed to create file x" + "\n");
		}
		String[] files = fileList();
		if (!deleteFile("tmp2")) {
			infoBox.append("Delete failed" + "\n");
		}
		for (String f : files) {
			infoBox.append("Found " + f + "\n");
		}
		infoBox.append("\n");
		infoBox.append("All done!");
    }
}
