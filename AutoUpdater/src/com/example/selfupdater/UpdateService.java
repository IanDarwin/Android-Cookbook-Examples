package com.example.selfupdater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Date;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class UpdateService extends IntentService {

	public static final String TAG = UpdateService.class.getSimpleName();

	private static final String SERVER_NAME = "darwinsys.com";
	
	private static final String PATH_TO_APK = "/icheckin/iCheckIn.apk";
	
	public static final boolean DEBUG = false;

	protected long ADAY = DEBUG ? 50000 : (24 * 60 * 60 * 1000);
	
	public UpdateService() {
		super(TAG);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	protected void onHandleIntent(Intent intent) {

		Log.d(TAG, "Starting One-time Service Runner");

		/* Now the simple arithmetic: if web package updated after
		 * last time app was updated, then it's time
		 * again to update!
		 */
		final long appUpdatedOnDevice = getAppUpdatedOnDevice();
		final long webPackageUpdated = getWebPackageUpdated();
		if (appUpdatedOnDevice == -1 || webPackageUpdated == -1) {
			return; // FAIL, try another day
		}
		if (webPackageUpdated > appUpdatedOnDevice) {
			triggerUpdate();
		}
	}
	
	public long getAppUpdatedOnDevice() {
		PackageInfo packageInfo = null;
		try {
			packageInfo = getPackageManager()
				.getPackageInfo(getClass().getPackage().getName(), PackageManager.GET_PERMISSIONS);
		} catch (NameNotFoundException e) {
			Log.d(TAG, "CANTHAPPEN: Failed to get package info for own package!");
			return -1;
		}
		return packageInfo.lastUpdateTime;
	}
	
	public static long getWebPackageUpdated() {
		Socket http = null;
		try {
			http = new Socket(SERVER_NAME, 80);
			PrintStream wout = new PrintStream(http.getOutputStream());
			wout.print("HEAD " + PATH_TO_APK + " HTTP/1.0\r\n\r\n");
			wout.flush();
			BufferedReader win = 
				new BufferedReader(new InputStreamReader(http.getInputStream()));
			String line;
			while ((line = win.readLine()) != null) {
				if (line.startsWith("404")) {
					Log.d(TAG, line);
					return -1;
				}

				if (line.toLowerCase().startsWith("last-modified:")) {
					Log.d(TAG, "Last-Modified Header: " + line);
					String aDateTime = line.substring(line.indexOf(' ') + 1);
					return Date.parse(aDateTime);
				}
			}
		} catch (IOException e) {
			Log.d(TAG, "Failed to CHECK for update: " + e);
			return -1;
		} finally {
			if (http != null)
				try {
					http.close();
				} catch (IOException e) {
					// Stupid checked exception in close! WGARA?
				}
		}
		Log.d(TAG, "Didn't find modified header");
		return -1;
	}

	protected void triggerUpdate() {
		Log.d(TAG, "UpdateService.triggerUpdate()");
		final Intent intent = new Intent(this, UpdateActivity.class);
		intent.setData(Uri.parse("http://" + SERVER_NAME + PATH_TO_APK));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
