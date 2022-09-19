package com.example.hello.command;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelloActivity extends Activity {
    /**
     * This method gets invoked when the activity is instantiated in
     * response to e.g., you clicked on the app's Icon in the Home Screen.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create a TextView for the current Activity
        TextView view = new TextView(this);
        // Make it say something
        view.setText("Hello World");
        // Put this newly-created view into the Activity
        // sort of like JFrame.setContentPane()
        setContentView(view);
    }
}
