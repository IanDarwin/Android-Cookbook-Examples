package com.darwinsys.gcmclient;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.darwinsys.gcmclient.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/** A simple(?) demonstration of GCM messaging; in this demo the app only
 * receives a message from GCM, which is originated by the "GCMMockServer" project.
 * @author Ian Darwin with help from Google documentation
 */
public class GcmMainActivity extends Activity {
	
	static final String TAG = "com.darwinsys.gcmdemo";
	private static final String TAG_CONFORMANCE = "RegulatoryCompliance";

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	/** The Sender Id is the Project ID number from the Google Developer's Console */
	private static final String GCM_SENDER_ID = "117558675814";
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    /** A Context to be used in inner classes */
    private Context context;
    /** The log textview */
    private TextView logTv;
    /** In real life this would be in the Application singleton class */
    private Executor threadPool = Executors.newFixedThreadPool(1);
    /** the GCM instance */
	private GoogleCloudMessaging gcm;
	/** The registration Id we get back from GCM the first time we register */
	private String registrationId;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "MainActivity.onCreate()");
		setContentView(R.layout.activity_main);
		logTv = (TextView) findViewById(R.id.logView);
		context = getApplicationContext();
		
		logTv.setText("Start\n");
		
		// GCM stuff
		if (checkForGcm()) {
			logTv.append("Passed checkforGCM\n");
			gcm = GoogleCloudMessaging.getInstance(this);
			registrationId = getRegistrationId(this);
			if (registrationId.isEmpty()) {
				threadPool.execute(new Runnable() {
					public void run() {
						final String regn = registerWithGcm();
						Log.d(TAG, "New Registration = " + regn);
						setMessageOnUiThread(regn + "\n");
					}
				});
			} else {
				final String mesg = "Previous Registration = " + registrationId;
				Log.d(TAG, mesg);
				logTv.append(mesg + "\n");
			}
		} else {
			logTv.append("Failed checkforGCM");
		}
	}
	
	void setMessageOnUiThread(final String mesg) {
		runOnUiThread(new Runnable() {
			public void run() {
				logTv.append(mesg);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "MainActivity.onResume()");
		checkForGcm();
	}
	
	boolean checkForGcm() {
		int ret = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == ret) {
			Log.d(TAG, "MainActivity.checkForGcm(): SUCCESS");
			return true;
		} else {
			Log.d(TAG, "MainActivity.checkForGcm(): FAILURE");
			if (GooglePlayServicesUtil.isUserRecoverableError(ret)) {
				GooglePlayServicesUtil.getErrorDialog(ret, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Toast.makeText(this,
						"Google Message Not Supported on this device; you will not get update notifications!!",
						Toast.LENGTH_LONG).show();
				Log.d(TAG_CONFORMANCE, "User accepted to run the app without update notifications!");
			}
			return false;
		}
	}
	
	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing registration ID.
	 * @author Adapted from  the Google Cloud Messaging example
	 */
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getSharedPreferences(GcmMainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	        Log.i(TAG, "Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must remove the registration ID
	    // since the existing regID is not guaranteed to work with the new app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i(TAG, "App version changed.");
	        return "";
	    }
	    return registrationId;
	}
	
	/**
	 * Get the app version, which comes ultimately from the AndroidManifest
	 * (you don't have to keep your own "version number" in the app too!).
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
	    } catch (NameNotFoundException e) {
	        // This is a "CANTHAPPEN", so why is it a checked exception?
	        final String mesg = "CANTHAPPEN: could not get my own package name!?!?: " + e;
	        Log.wtf(TAG, mesg);
			throw new RuntimeException(mesg);
	    }
	}
	
	/**
	 * Register this app, the first time on this device, with Google Cloud Messaging Service
	 * @return The registration ID
	 * @author Adapted from  the Google Cloud Messaging example
	 */
	private String registerWithGcm() {
		String mesg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            registrationId = gcm.register(GCM_SENDER_ID);
            mesg = "Device registered! My registration =" + registrationId;

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            sendRegistrationIdToMyServer();

            // For this demo: we don't need to send it because the device
            // will send upstream messages to a server that echo back the
            // message using the 'from' address in the message.

            // Persist the regID - no need to register again.
            storeRegistrationId(context, registrationId);
        } catch (IOException ex) {
            mesg = "Error :" + ex.getMessage();
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
            Toast.makeText(context, mesg, Toast.LENGTH_LONG).show();
            throw new RuntimeException(mesg);
        }
        return mesg;

	}
	
	private void sendRegistrationIdToMyServer() {
		// TODO Auto-generated method stub	
	}
	
	/**
	 * You need to save the long RegistrationId string someplace; this version
	 * stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 * @param context application's context.
	 * @param regId registration ID
	 * @author Adapted from  the Google Cloud Messaging example
	 */
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getSharedPreferences(GcmMainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	    int appVersion = getAppVersion(context);
	    Log.i(TAG, "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
