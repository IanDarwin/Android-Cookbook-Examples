package com.halfclosed.wordpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

/**
 * This activity shows the splash screen and moves on
 * to the main activity after SPLASH_TIME milliseconds,
 * or if the user presses any key. If the user presses
 * the BACK key, the application terminates.
 * 
 * @author Emaad Ahmed Manzoor
 *
 */
public class Splash extends Activity {
    
	private static final String TAG = "FindMeX.Splash";
    /**
     * A counter for the time elapsed in milliseconds
     * since the splash screen was first displayed.
     */
    private long ms = 0;
    
    /**
     * The time duration for which the splash screen
     * must be displayed.
     */
    private final long SPLASH_TIME = 2000;
    
    /**
     * A flag to enable/disable the splash screen.
     */
    private boolean splashActive = true;
    
    /**
     * A flag to pause/resume the splash screen timer.
     */
    private boolean paused = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        Thread mythread = new Thread() {
            public void run() {
                try {
                    while (splashActive && ms < SPLASH_TIME) {
                        if (!paused)
                            ms = ms + 100;
                        sleep(100);
                    }
                } catch (Exception e) {
                	Log.d(TAG, "Splasher caught " + e);
                } finally {
                    startActivity(new Intent(Splash.this, Search.class));
                    finish();
                }
            }
        };
        
        mythread.start();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        paused = true;  
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        paused = false; 
    }
      
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        } else {
            splashActive = false;
        }
        return true;
    }
}