package android.app.hello.phonegap;


import android.os.Bundle;

//Add phonegap.jar to build path
import org.apache.cordova.DroidGap;

public class HomeScreen extends DroidGap {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        //set the URL from assets which is to be loaded.
        super.loadUrl("file:///android_asset/www/helloworld.html");
       
    }
}