package com.oreilly.recipe12;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Shows Menu Action Handling.
 * @author Rachee Singh
 */
public class MenuAction extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.icon1:
				Toast.makeText(this, "Icon 1 Beep Bop!", Toast.LENGTH_LONG).show();
				break;
			case R.id.icon2:
				Toast.makeText(this, "Icon 2 Beep Bop!", Toast.LENGTH_LONG).show();
				break;
			case R.id.icon3:
				Toast.makeText(this, "Icon 3 Beep Bop!", Toast.LENGTH_LONG).show();
				break;
			case R.id.icon4 :
				Toast.makeText(this, "Icon 4 Beep Bop!", Toast.LENGTH_LONG).show();
				break;
		}
		return true;
	}
}
