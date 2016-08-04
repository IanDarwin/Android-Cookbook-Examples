package com.androidcookbook.fragmentsdemos;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

	public MainActivityFragment() {
		// Constructor with arguments may be needed in more sophisticated apps
	}

	/** Like Menus, Fragments must be inflated by the developer */
    @Override
    public View onCreateView(
				LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
