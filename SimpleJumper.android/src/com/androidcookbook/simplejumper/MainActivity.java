package com.androidcookbook.simplejumper;

import org.flixel.FlxAndroidApplication;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends FlxAndroidApplication {
    public MainActivity() {
		super(new FlixelGame());
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // ORIENTATION
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
   }
}