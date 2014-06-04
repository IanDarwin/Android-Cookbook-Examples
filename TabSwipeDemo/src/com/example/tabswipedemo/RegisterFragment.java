package com.example.tabswipedemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RegisterFragment extends Fragment {
	
	@Override
    public View onCreateView(LayoutInflater inflater, 
    		ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView =
        	inflater.inflate(R.layout.fragment_register, container, false);
         
        return rootView;
    }
}
