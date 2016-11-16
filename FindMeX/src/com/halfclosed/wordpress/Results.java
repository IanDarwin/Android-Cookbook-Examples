package com.halfclosed.wordpress;

import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This activity shows the progress of
 * fetching data from the server and
 * doing the necessary calculations.
 * 
 * @author Emaad Ahmed Manzoor
 *
 */
public class Results extends ListActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.results); Not needed with ListActivity
        
        Bundle extras = getIntent().getExtras();
        String[] results = extras.getStringArray("results");
        final String[] addresses = extras.getStringArray("addresses");
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results));
        
        getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Results.this, addresses[position], Toast.LENGTH_SHORT).show();
            }
            
        });

    }
}