package com.example.foo;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.example.foo.HelloWorldActivityTest \
 * com.example.foo.tests/android.test.InstrumentationTestRunner
 */
public class HelloWorldActivityTest extends ActivityInstrumentationTestCase2<HelloWorldActivity> {

    public HelloWorldActivityTest() {
        super("com.example.foo", HelloWorldActivity.class);
    }

}
