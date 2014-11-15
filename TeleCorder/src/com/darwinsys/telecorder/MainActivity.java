package com.darwinsys.telecorder;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * This might mutate into a usable call recorder, when:
 * (a) it works, and
 * (b) somebody adds call-started, call-ended broadcast receiver
 * This version was borrowed from "Pratt"'s answer at
 * http://stackoverflow.com/questions/18887636/how-to-record-phone-calls-in-android/21331756#21331756
 * @author Ian Darwin
 */
public class MainActivity extends Activity {
	private final static String TAG = TeleService.class.getSimpleName();
    private static final int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, "MainActivity.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startTeleService();

//        try {
//            // Initiate DevicePolicyManager.
//            mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//            mAdminName = new ComponentName(this, DeviceAdminDemo.class);
//
//            if (!mDPM.isAdminActive(mAdminName)) {
//                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
//                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to secure your application.");
//                startActivityForResult(intent, REQUEST_CODE);
//            } else {
//                startTeleService();
//            }
//        } catch (Exception e) {
//        	Log.e(TAG, "Something bad hoppen: " + e, e);
//        } 
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_CODE == requestCode) {
                startTeleService();
        }
    }

	/**
	 * 
	 */
	private void startTeleService() {
		Log.d(TAG, "MainActivity: Starting Service");
		Intent intent = new Intent(MainActivity.this, TeleService.class);
		startService(intent);
	}

}

