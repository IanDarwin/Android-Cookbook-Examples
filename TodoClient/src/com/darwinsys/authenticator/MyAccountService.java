package com.darwinsys.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.darwinsys.todoclient.MainActivity;

/**
 * This is used if the user invokes the "Add Account" dialog.
 * Instantiate the Authenticator and return the corresponding IBinder.
 * @author Ian Darwin
 *
 */
public class MyAccountService extends Service {
	
	private static final String TAG = MainActivity.TAG;

	MyAuthenticator mAuthenticator;
	
	public MyAccountService() {
		Log.d(TAG, "MyAccountService()");
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG, "MyAccountService.onCreate()");
		mAuthenticator = new MyAuthenticator(this);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "MyAccountService.onBind()");
		return mAuthenticator.getIBinder();
	}

}
