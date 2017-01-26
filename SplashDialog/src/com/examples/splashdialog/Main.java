package com.examples.splashdialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;

/** Show the Dialog path to Splash Screen.
 * @author Ian Darwin based on Ian Clifton's version
 */
public class Main extends Activity {
	private class StateSaver {
		private boolean showSplashScreen = true;
		// Other save state info here...
	}
	private static final int MAX_SPLASH_SECONDS = 10;
	private Dialog splashDialog;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        StateSaver data = (StateSaver) getLastNonConfigurationInstance();
        if (data != null) { // "all this has happened before"
        	if (data.showSplashScreen ) { // and we didn't already finish
        		showSplashScreen();
        	}
        	setContentView(R.layout.main);
        	// Do any UI rebuilding here using saved stated
        } else {
        	showSplashScreen();
        	setContentView(R.layout.main);
        	// Start any heavy-duty loading here, but on its own thread
        }               
    }
    
    @Override
    public Object onRetainNonConfigurationInstance() {
    	StateSaver data = new StateSaver();
    	// save important data into this object
    	
    	if (splashDialog != null) {
    		data.showSplashScreen = true;
    		removeSplashScreen();
    	}
    	return data;
    }

	private void removeSplashScreen() {
		if (splashDialog != null) {
			splashDialog.dismiss();
			splashDialog = null;
		}
	}

	private void showSplashScreen() {
		splashDialog = new Dialog(this);
		splashDialog.setContentView(R.layout.splashscreen);
		splashDialog.setCancelable(false);
		splashDialog.show();
		
		// Start background Handler to cancel it, to be save
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				removeSplashScreen();
			}	
		}, MAX_SPLASH_SECONDS * 1000);
	}
}