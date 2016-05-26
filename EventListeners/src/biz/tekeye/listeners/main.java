package biz.tekeye.listeners;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class main extends Activity implements OnClickListener {
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    //Method 1 attach an instance of HandleClick class to the Button
	    findViewById(R.id.button1).setOnClickListener(new HandleClick());
	    //Method 2 use the handleClick variable to attach the event listener
	    findViewById(R.id.button2).setOnClickListener(handleClick);
	    //Method 3 anonymous inner class to attach the event listener
	    findViewById(R.id.button3).setOnClickListener(new OnClickListener(){
	    	public void onClick(View arg0) {
	    		Button btn = (Button)arg0;
	    		TextView tv = (TextView) findViewById(R.id.textview3);
	    		tv.setText("You pressed " + btn.getText());
	    	}
	    });
	    //Method 4 Implementation in Activity
	    findViewById(R.id.button4).setOnClickListener(this);
	    //Method 5 set up via layout attribute onClick assigned to HandleClickByAttribute
	  }
	  //Method 1 a call implementing onClickListener
	  private class HandleClick implements OnClickListener{
	    public void onClick(View arg0) {
	      Button btn = (Button)arg0;    //cast view to a button
	      // get a reference to the TextView
	      TextView tv = (TextView) findViewById(R.id.textview1);
	      // update the TextView text
	      tv.setText("You pressed " + btn.getText());
	    }
	  }
	  //Method 2 a variable declared as a type of interface
	  private OnClickListener handleClick = new OnClickListener(){
	    public void onClick(View arg0) {
	      Button btn = (Button)arg0;
	      TextView tv = (TextView) findViewById(R.id.textview2);
	      tv.setText("You pressed " + btn.getText());
	    }
	  };
	  //Method 4 Implementation on the onClickListener by the Activity
	  public void onClick(View arg0) {
		  Button btn = (Button)arg0;
		  TextView tv = (TextView) findViewById(R.id.textview4);
		  tv.setText("You pressed " + btn.getText());
	  }
	  public void HandleClickByAttribute(View arg0) {
    	Button btn = (Button)arg0;
        TextView tv = (TextView) findViewById(R.id.textview5);
    	tv.setText("You pressed " + btn.getText());
	  }
}