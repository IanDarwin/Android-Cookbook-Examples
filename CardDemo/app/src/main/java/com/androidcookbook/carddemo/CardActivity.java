package com.androidcookbook.carddemo;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_layout);

        // Dynamically set the image and text

        ImageView img = (ImageView) findViewById(R.id.house_front_view);
        Drawable d = ContextCompat.getDrawable(this,R.drawable.fixer_upper_1);
        img.setImageDrawable(d);

        TextView descr = (TextView) findViewById(R.id.house_descr);
        descr.setText("Beautiful fixer-upper! Only used on weekends by respectable Hobbit couple!");

    }
}
