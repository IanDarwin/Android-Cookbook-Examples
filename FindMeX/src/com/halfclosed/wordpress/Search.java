package com.halfclosed.wordpress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

/**
 * This activity is the search screen. The user
 * builds a query via the GUI elements, and
 * the application then uses the query to process
 * search results from JustDial, post them and
 * retrieve distance data from Google Maps, and then
 * display the Results activity.
 * 
 * @author Emaad Ahmed Manzoor
 */
public class Search extends Activity {
    
    public static String searchText;
    public static String city;
    public static String locality;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        String[] cities = getResources().getStringArray(R.array.cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_dropdown_item_1line, cities );
        final AutoCompleteTextView citiesView = (AutoCompleteTextView) findViewById(R.id.city_autocompletetextview);
        citiesView.setAdapter(adapter);     
        
        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new OnClickListener() {        
            public void onClick(View v) {
                
                searchText = ((TextView)findViewById(R.id.search_edittext)).getText().toString();
                city = citiesView.getText().toString();
                locality = ((TextView)findViewById(R.id.locality_edittext)).getText().toString();
                
                Intent startProgress = new Intent(Search.this, Progress.class);
                Bundle params = new Bundle();
                params.putString("city", city);
                params.putString("locality", locality);
                params.putString("searchtext", searchText);
                startProgress.putExtras(params);
                startProgress.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startProgress);
                
            }
        });
        
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        Log.d("EMAaD", "Keycode:  keycode" + keyCode + " Event: " + event.toString()  );
        return true;
    }
    
}