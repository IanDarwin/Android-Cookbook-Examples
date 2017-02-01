package com.sym.dateformatdemo;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

public class MainActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView textView1 = (TextView) findViewById(R.id.textview1);
        TextView textView2 = (TextView) findViewById(R.id.textview2);
        TextView textView3 = (TextView) findViewById(R.id.textview3);
        TextView textView4 = (TextView) findViewById(R.id.textview4);
        TextView textView5 = (TextView) findViewById(R.id.textview5);
        String delegate = "MM/dd/yy hh:mm a"; // 09/21/2011 02:17 pm
        Date noteTS = new Date();
        textView1.setText("Found Time :: "+DateFormat.format(delegate,noteTS));
        delegate = "MMM dd, yyyy h:mm aa"; // Sep 21,2011 02:17 pm
        textView2.setText("Found Time :: "+DateFormat.format(delegate,noteTS));
        delegate = "MMMM dd, yyyy h:mmaa"; //September 21,2011 02:17pm
        textView3.setText("Found Time :: "+DateFormat.format(delegate,noteTS));
        delegate = "E, MMMM dd, yyyy h:mm:ss aa";//Wed, September 21,2011 02:17:48 pm
        textView4.setText("Found Time :: "+DateFormat.format(delegate,noteTS));
        delegate =
                "EEEE, MMMM dd, yyyy h:mm aa"; //Wednesday, September 21,2011 02:17:48 pm
        textView5.setText("Found Time :: "+DateFormat.format(delegate,noteTS));
    }
}

