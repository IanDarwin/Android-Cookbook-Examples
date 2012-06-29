package com.example.numberpicker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.darwinsys.android.NumberPickerLogic;
import com.example.play.R;

public class NumberPickersActivity extends Activity {
	
	public final String TAG = getClass().getName();
	
	int minimum = Integer.MIN_VALUE;
	int maximum = Integer.MAX_VALUE;
	EditText number;
	NumberPickerLogic logic;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        number = (EditText) findViewById(R.id.number);
        logic = new NumberPickerLogic(number);
        View ncj = findViewById(R.id.ncj);
        Log.d(TAG, "ncj = " + ncj);
    }
    
    public void increment(View v) {
    		logic.increment();
    }
    
    public void decrement(View v) {
    		logic.decrement();
    }
}