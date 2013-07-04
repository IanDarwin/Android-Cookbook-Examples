package com.example.viewpagerdemo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		List<Fragment> fragments = getFragments();
		CountryPageAdapter pageAdapter = 
				new CountryPageAdapter(getSupportFragmentManager(), fragments); 
		ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
		pager.setAdapter(pageAdapter);
	}

	private List<Fragment> getFragments() {
		List<Fragment> frags = new ArrayList<Fragment>();
		frags.add(prepareFragment("Canada"));		
		frags.add(prepareFragment("U.S.A."));		
		frags.add(prepareFragment("Mejico"));		
		return frags;
	}

	/**
	 * Create and set up one CountryFragment.
	 * @param name Country name.
	 */
	Fragment prepareFragment(String name) {
		CountryFragment cf = new CountryFragment();
		Bundle args = new Bundle();
		args.putString(CountryFragment.COUNTRY_NAME, name);
		cf.setArguments(args);
		return cf;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
