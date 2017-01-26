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
			println("Failed to write " + DATA_FILE_NAME + " due to " + e);
		}

		File where = getFilesDir(); // Absolute path to directory for our app's internal storage
		println("Our private dir is " + where.getAbsolutePath());

		try (BufferedReader is = new BufferedReader(new InputStreamReader(openFileInput(DATA_FILE_NAME)))) {
			String line = is.readLine();
			println("Read the string " + line);
		} catch (IOException e) {
			println("Failed to read back " + DATA_FILE_NAME + " due to " + e);
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
			println("Failed to create file x due to " + e);
		}
		String[] files = fileList();
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
			albumDir.mkdirs();
			if (!albumDir.isDirectory()) {
				println("Unable to create music album");
			} else {
				println("Music album exists as " + albumDir);
			}
			// Only set this if you do NOT want the media indexer to find
			// the files in your new "album" subdirectory
			boolean hideAlbumFromMediaIndexer = false;
			if (hideAlbumFromMediaIndexer) {
				try {
					new File(albumDir, ".nomedia").createNewFile();
					println("Created .nomedia file in " + albumDir.getAbsolutePath());
				} catch (IOException e) {
					println("Failed to create .nomedia file in " + albumDir.getAbsolutePath() + " due to " + e);
				}
			}

			// BTW is it on real or emulated SD? On API 21 or higher we could do:
			//if (Environment.isExternalStorageEmulated(albumDir)) {
			//	println("BTW this is on emulated storage");
			//}

			// Then we could create files in the album using, for example,
			final File trackFile = new File(albumDir, "Track 1.mp3");
			try (OutputStream is = new FileOutputStream(trackFile)) {
				// Write some music data to the file here...
				println("Assume we wrote some data to " + trackFile + " here");
			} catch (IOException e) {
				println("Failed to create Track file, due to " + e);
			} finally {
				// clean up after demo - not in production!
				if (trackFile.exists()) {
					println("Cleaning up");
					trackFile.delete();
				}
			}

			// Finally we'll create an "application private" file on /sdcard
			// Note that these are accessible to all other applications!
			final File privateDir = getExternalFilesDir(null);
			File semiPrivateFile = new File(privateDir, "fred.jpg");
			try (OutputStream is = new FileOutputStream(semiPrivateFile)) {
				println("Assume we are writing to " + semiPrivateFile);
			} catch (IOException e) {
				println("Failed to create " + semiPrivateFile + " due to " + e);
			} finally {
				if (semiPrivateFile.exists()) {
					semiPrivateFile.delete();
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
