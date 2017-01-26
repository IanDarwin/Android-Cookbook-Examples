package com.example.hellostudiotesting;

import android.app.Application;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.widget.TextView;

/**
 * A Simple test of the Main Activity
 */
public class MainTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainTest() {
        super("my.pkg.app", MainActivity.class);
    }

    public void test() {
        TextView textView = (TextView) getActivity().findViewById(R.id.textView);

        assertEquals("Hello World!", textView.getText());
    }
}