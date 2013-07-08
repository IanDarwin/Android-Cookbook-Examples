package com.example.viewpagerdemo;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Displays the user-chosen country-specific page to view.
 * @author Adapted from http://www.programmingmobile.com/2013/04/android-tutorial-using-viewpager.html
 *
 */
public class CountryPageAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;

	public CountryPageAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;

	}

	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position);

	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}

}
