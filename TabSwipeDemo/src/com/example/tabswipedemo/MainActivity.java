package com.example.tabswipedemo;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener{

	ActionBar mActionBar;
	ViewPager mViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Main view typically contains just the ViewPager
		setContentView(R.layout.activity_main);
		
		// Tell the ActionBar to display tabs
		mActionBar = getActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mActionBar.setHomeButtonEnabled(false);
		
		// Set up the tab navigation
		PagerAdapter mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		// Make swiping set the view
		mViewPager.setAdapter(mAdapter);
		// Make swiping update the tab view
		mViewPager.setOnPageChangeListener(new SwipedListener());
		
		// Now that listeners are in place we can safely add the tabs
		mActionBar.addTab(
				mActionBar.newTab()
				.setText(getString(R.string.Tab1Title))
				.setTabListener(this));
		mActionBar.addTab(
				mActionBar.newTab()
				.setText(getString(R.string.Tab2Title))
				.setTabListener(this));
		mActionBar.addTab(
				mActionBar.newTab()
				.setText(getString(R.string.Tab3Title))
				.setTabListener(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		System.out.println("MainActivity.onTabSelected()");
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		System.out.println("MainActivity.onTabUnselected()");
	}

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		System.out.println("MainActivity.onTabReselected()");
	}

	class TabsPagerAdapter extends FragmentPagerAdapter {
		 
	    public TabsPagerAdapter(android.support.v4.app.FragmentManager fm) {
	        super(fm);
	    }
	 
	    /**
	     * Consider caching instead of always instantiating;
	     * typical use case will require all three
	     */
	    @Override
	    public Fragment getItem(int index) {
	    	System.out.println("MainActivity.TabsPagerAdapter.getItem(): " + index);
	        switch (index) {
	        case 0:
	            // Leftmost (and default) is the Arrival
	        	return new ArrivalFragment();
	        case 1:
	            // Equipment Report activity
	            return new EquipmentFragment();
	        case 2:
	            // Attendees report activity
	            return new RegisterFragment();
	        }
	        return null;
	    }
	 
	    @Override
	    public int getCount() {
	        return 3;	// we always have 3 tabs.
	    }
	}
	
	class SwipedListener implements ViewPager.OnPageChangeListener {
	 
	    @Override
	    public void onPageSelected(int position) {
	        // on changing the page
	        // make respected tab selected
	        mActionBar.setSelectedNavigationItem(position);
	    }
	 
	    @Override
	    public void onPageScrolled(int arg0, float arg1, int arg2) {
	    	// not used
	    }
	 
	    @Override
	    public void onPageScrollStateChanged(int arg0) {
	    	// not used
	    }
	}
}
