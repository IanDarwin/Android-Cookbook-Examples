package com.androidcookbook.sensorupordown;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    private TextView mFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFace = (TextView) findViewById(R.id.faceTextView);
        SensorManager sensorManager =  (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<android.hardware.Sensor> sensorList =
                sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(accelerometerListener, sensorList.get(0), 0, null);
    }

    private SensorEventListener accelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float z = event.values[2];
                if (z >9 && z < 10)
                    mFace.setText("FACE UP");
                else if (z > -10 && z < -9)
                    mFace.setText("FACE DOWN");
            }
            @Override
            public void onAccuracyChanged(Sensor arg0, int arg1) {
                // empty
            }
    };
}
