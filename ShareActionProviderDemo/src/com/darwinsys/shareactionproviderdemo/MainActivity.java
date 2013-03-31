package com.darwinsys.shareactionproviderdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

/**
 * Simple demo of the ShareActionProvider menuitem - requires 4.0 or later!!
 * @author Ian Darwin
 */
public class MainActivity extends Activity {
	
	private ShareActionProvider mShareActionProvider;
	
	private Intent mShareIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mShareIntent = new Intent();
		mShareIntent.setAction(Intent.ACTION_SEND);
		mShareIntent.setType("text/plain");
		mShareIntent.putExtra(Intent.EXTRA_TEXT, "From me to you, this text is new.");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		// Find the MenuItem that we know has the ShareActionProvider
	    MenuItem item = menu.findItem(R.id.menu_item_share);

	    // Get its ShareActionProvider
	    mShareActionProvider = (ShareActionProvider) item.getActionProvider();

	    // Connect the dots: give the ShareActionProvider its Share Intent
	    if (mShareActionProvider != null) {
	        mShareActionProvider.setShareIntent(mShareIntent);
	    }
	        
	    // Return true so Android will know we want to display the menu
		return true;
	}
}
