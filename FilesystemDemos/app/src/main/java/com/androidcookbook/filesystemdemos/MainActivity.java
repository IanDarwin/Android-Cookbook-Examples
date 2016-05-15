package com.androidcookbook.filesystemdemos;

import java.io.*;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;
import android.os.Environment;
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

		println("Internal Storage:");

		try (FileOutputStream os = openFileOutput(DATA_FILE_NAME, Context.MODE_PRIVATE)) {
			os.write(message.getBytes());
			println("Wrote the string " + message + " to file " + DATA_FILE_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}

		File where = getFilesDir(); // Absolute path to directory for our app's internal storage
		println("Our private dir is " + where.getAbsolutePath());

		try (BufferedReader is = new BufferedReader(new InputStreamReader(openFileInput(DATA_FILE_NAME)))) {
			String line = is.readLine();
			println("Read the string " + line);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Let's save the file in our temporary "cache directory" instead.
		// Such files *might* get creamed by Android if it needs the disk space
		// We'd need to keep our usage here to some reasonable amount by pruning old caches.

		File cacFile = new File(getCacheDir(), "myCache.dat");
		println("A cache file is at " + cacFile.getAbsolutePath());

		File tmpDir = getDir("tmp2", 0);	// Creates and/or returns Dir within FilesDir
		println("A sub-folder is at " + tmpDir.getAbsolutePath());
		try {
			final boolean newFile = new File(tmpDir, "x").createNewFile();
			println("Created file x");
		} catch (IOException e) {
			e.printStackTrace();
			println("Failed to create file x");
		}
		String[] files = fileList();
		if (!deleteFile("tmp2")) {
			println("Delete failed");
		}
		for (String f : files) {
			println("Found " + f);
		}
		println("");

		println("External Storage:");
		boolean readOnly = false, mounted = false;
		String state = Environment.getExternalStorageState();
		println("External storage state = " + state);
		if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			mounted = true;
			readOnly = true;
			println("External storage is read-only!!");
		} else if (state.equals(Environment.MEDIA_MOUNTED)) {
			mounted = true;
			readOnly = false;
			println("External storage is usable");
		} else {
			println("External storage NOT USABLE");
		}

		if (mounted) {
			// Let's create a sharable file
			// This needs permission
			// Get the external storage folder for Music
			final File externalStoragePublicDirectory =
					Environment.getExternalStoragePublicDirectory(
							Environment.DIRECTORY_MUSIC);
			// Get the directory for the user's public pictures directory.
			// We want to use it for example to create a new music album.
			File albumDir = new File(externalStoragePublicDirectory, "Jam Session 2017");
			if (!albumDir.isDirectory() || !albumDir.mkdirs()) {
				println("Unable to create music album");
			} else {
				println("Music album exists as " + albumDir);
			}
			boolean hideAlbumFromMediaIndexer = false;
			if (hideAlbumFromMediaIndexer) {
				try {
					new File(albumDir, ".nomedia").createNewFile();
					println("Created .nomedia file in " + albumDir.getAbsolutePath());
				} catch (IOException e) {
					println("Failed to create .nomedia file in " + albumDir.getAbsolutePath() + " due to " + e);
				}
			}
			;
			// Then we could create files in the album using, for example,
			final File trackfile = new File(albumDir, "Track 1.mp3");
			try (InputStream is = new FileInputStream(trackfile)) {
				// Write some music data to the file here...
				println("Assume we wrote some data to the file here");
			} catch (IOException e) {
				println("Failed to create Track file, due to " + e);
			} finally {
				if (trackfile.exists()) {
					trackfile.delete(); // clean up after demo - not in production!
				}
			}
		}

		// The End
		println("");
		println("All done!");
    }

	private void println(String s) {
		infoBox.append(s);
		infoBox.append("\n");
	}
}
