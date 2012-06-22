package com.example.numberpicker;

import com.example.play.R;
import com.example.play.R.id;
import com.example.play.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class NumberPickersActivity extends Activity {
	
	public final String TAG = getClass().getName();
	
	int minimum = Integer.MIN_VALUE;
	int maximum = Integer.MAX_VALUE;
	EditText number;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        number = (EditText) findViewById(R.id.number);
        View ncj = findViewById(R.id.ncj);
        Log.d(TAG, "ncj = " + ncj);
    }
    
    public void increment(View v) {
    		increment();
    }
    
    public void decrement(View v) {
    		decrement();
    }
    
	public void increment() {
		final int newValue = clamp(getValue() + 1);
		setValue(newValue);
	}

	public void decrement() {
		final int newValue = clamp(getValue() - 1);
		setValue(newValue);
	}

	int clamp(int newValue) {
		if (newValue < minimum) {
			newValue = minimum;
		}
		if (newValue > maximum) {
			newValue = maximum;
		}
		return newValue;
	}

	/** Return the integer value of the clicker. */
	public int getValue() {
		return Integer.parseInt(number.getText().toString());
	}

	public void setValue(int value) {
		number.setText(Integer.toString(value));
	}

}