package com.androidcookbook.sensorshakedetection;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Detect if the user has shaken the device.
 * @author Thomas Manthey
 */
public class ShakeActivity extends Activity {
	/* The connection to the hardware */
	private SensorManager mySensorManager;

	/* Here we store the current values of acceleration, one for each axis */
	private float xAccel;
	private float yAccel;
	private float zAccel;

	/* And here the previous ones */
	private float xPreviousAccel;
	private float yPreviousAccel;
	private float zPreviousAccel;

	/* Used to suppress the first shaking */
	private boolean firstUpdate = true;

	/* What acceleration difference would we assume as a rapid movement? */
	private final float shakeThreshold = 1.5f;
	
	/* Has a shaking motion been started (one direction) */
	private boolean shakeInitiated = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mySensorManager.registerListener(mySensorEventListener, mySensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	/* The SensorEventListener lets us wire up to the real hardware events */
	private final SensorEventListener mySensorEventListener = new SensorEventListener() {

		public void onSensorChanged(SensorEvent se) {
			updateAccelParameters(se.values[0], se.values[1], se.values[2]);
			if ((!shakeInitiated) && isAccelerationChanged()) {
				shakeInitiated = true;
			} else if ((shakeInitiated) && isAccelerationChanged()) {
				executeShakeAction();
			} else if ((shakeInitiated) && (!isAccelerationChanged())) {
				shakeInitiated = false;
			}
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			/* not used in this example */
		}
	};

	/* Store acceleration values from sensor */
	private void updateAccelParameters(float xNewAccel, float yNewAccel, float zNewAccel) {
		/* we have to suppress the first change of acceleration, as
		 * it results from first values being initialized with 0
		 */
		if (firstUpdate) {
			xPreviousAccel = xNewAccel;
			yPreviousAccel = yNewAccel;
			zPreviousAccel = zNewAccel;
			firstUpdate = false;
		} else {
			xPreviousAccel = xAccel;
			yPreviousAccel = yAccel;
			zPreviousAccel = zAccel;
		}
		xAccel = xNewAccel;
		yAccel = yNewAccel;
		zAccel = zNewAccel;
	}

	/*
	 * If the values of acceleration have changed on at least two axes, 
	 * we are probably in a shake motion 
	 */
	private boolean isAccelerationChanged() {
		float deltaX = Math.abs(xPreviousAccel - xAccel);
		float deltaY = Math.abs(yPreviousAccel - yAccel);
		float deltaZ = Math.abs(zPreviousAccel - zAccel);
		return (deltaX > shakeThreshold && deltaY > shakeThreshold)
				|| (deltaX > shakeThreshold && deltaZ > shakeThreshold)
				|| (deltaY > shakeThreshold && deltaZ > shakeThreshold);
	}


	/** 
	 * Do the action that we wanted to do when shaking of the device is detected.
	 */
	private void executeShakeAction() {
		Toast.makeText(this, "Shaking detected!", Toast.LENGTH_SHORT).show();
	}
}
