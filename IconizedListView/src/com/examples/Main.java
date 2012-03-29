package com.examples;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class Main extends Activity {

	private List<Country> countryList= new ArrayList<Country>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the View layer
		setContentView(R.layout.listview);
		setTitle("TestIconizedListView");

		// Create Parser for raw/countries.xml
		CountryParser countryParser = new CountryParser();
		InputStream inputStream = getResources().openRawResource(
				R.raw.countries);
		
		// Parse the inputstream
		countryParser.parse(inputStream);

		// Get Countries
		List<Country> countryList = countryParser.getList();
		
		
		// Create a customized ArrayAdapter
		CountryArrayAdapter adapter = new CountryArrayAdapter(
				getApplicationContext(), R.layout.country_listitem, countryList);
		
		// Get reference to ListView holder
		ListView lv = (ListView) this.findViewById(R.id.countryLV);
		
		// Set the ListView adapter
		lv.setAdapter(adapter);
	}
}
