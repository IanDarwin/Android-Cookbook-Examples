package com.androidcookbook.wheelpickerdemo;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class WheelDemoActivity extends Activity {

    private final static String TAG = "WheelDemo";

    private final static String[] wheelMenu1 = {
            "Right Arm", "Left Arm",
            "R-Abdomen", "L-Abdomen",
            "Right Thigh", "Left Thigh"};
    private final static String[] wheelMenu2 = {
            "Upper", "Middle", "Lower"
    };
    private final static String[] wheelMenu3 = {"R", "L"};

    // Wheel scrolled flag
    private boolean wheelScrolled = false;
    private TextView resultText;
    private EditText text1;
    private EditText text2;
    private EditText text3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_picker);
        initWheel(R.id.p1, wheelMenu1);
        initWheel(R.id.p2, wheelMenu2);
        initWheel(R.id.p3, wheelMenu3);
        text1 = (EditText) this.findViewById(R.id.r1);
        text2 = (EditText) this.findViewById(R.id.r2);
        text3 = (EditText) this.findViewById(R.id.r3);
        resultText = (TextView) this.findViewById(R.id.resultText);
    }

    /**
     * Wheel scrolled listener
     */
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
        }
        @Override
        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;
            updateStatus();
        }
    };

    /**
     * Wheel changed listener
      */
    private final OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            Log.d(TAG, "onChanged, wheelScrolled = " + wheelScrolled);
            if (!wheelScrolled) {
                updateStatus();
            }
        }
    };

    /**
     * Updates entered status
     */
    private void updateStatus() {
        text1.setText(wheelMenu1[((WheelView) findViewById(R.id.p1)).getCurrentItem()]);
        text2.setText(wheelMenu2[((WheelView) findViewById(R.id.p2)).getCurrentItem()]);
        text3.setText(wheelMenu3[((WheelView) findViewById(R.id.p3)).getCurrentItem()]);
        resultText.setText(
                wheelMenu1[((WheelView) findViewById(R.id.p1)).getCurrentItem()] + " - " +
                        wheelMenu2[((WheelView) findViewById(R.id.p2)).getCurrentItem()] + " - " +
                        wheelMenu3[((WheelView) findViewById(R.id.p3)).getCurrentItem()]);
    }
    /**
     * Initializes one wheel
     * @param id
     * the wheel widget Id
     */
    private void initWheel(int id, String[] wheelMenu1) {
        WheelView wheel = (WheelView) findViewById(id);
        wheel.setViewAdapter(new ArrayWheelAdapter<String>(this, wheelMenu1));
        wheel.setVisibleItems(2);
        wheel.setCurrentItem(0);
        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
    }
}