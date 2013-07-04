package com.darwinsys.uniqueid;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

/** Unique Identifiers for device. Sources:
 * 1) AndroidCookbook.com
 * 2) StackOverflow
 * 3) developer.samsung.com
 * @author Ian Darwin
 */
public class MainActivity extends Activity {

	private static final String TAG = "UniqueId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// Get "Device Serial Number". The Android SystemProperties is apparently not for public use,
		// as it exists on-device but is NOT exposed in the SDK, so treat with a grain of salt!
		String serialNumber = "unknown";
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class, String.class);
			serialNumber = (String) get.invoke(c, "ro.serialno", serialNumber);
		} catch (Exception e) {
			Log.e(TAG, "Failed to get serial number", e);
		}
		((TextView) findViewById(R.id.serial_number)).setText(serialNumber);

		// Get "Android ID". According to the JavaDoc:
		// "A 64-bit number (as a hex string) that is 
		// randomly generated on the device's first boot 
		// and should remain constant for the lifetime 
		// of the device. (The value may change if a 
		// factory reset is performed on the device.)"
		String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		((TextView) findViewById(R.id.android_id)).setText(androidId);

		// Get the mobile device id (IMEI or similar) if any
		String imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		((TextView) findViewById(R.id.imei)).setText(imei);
	}
}
