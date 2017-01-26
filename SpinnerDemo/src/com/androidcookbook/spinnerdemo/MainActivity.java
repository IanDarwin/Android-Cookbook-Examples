package com.androidcookbook.spinnerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		// Spinner 1 gets its labels automatically from an XML array
        Spinner contextChooser1 = (Spinner) findViewById(R.id.contextChooser1);
        contextChooser1.setOnItemSelectedListener(listener);

		// Spinner 2 gets its labels programmatically from the XML array
		Spinner contextChooser2 = (Spinner) findViewById(R.id.contextChooser2);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
			this, R.array.reading_context_names, android.R.layout.simple_spinner_item);
        contextChooser2.setAdapter(adapter2);
        contextChooser2.setOnItemSelectedListener(listener);

		// Spinner 3 gets its labels programmatically from a Java language enum
        // Relies on enum's toString method; if need to use another method, would
        // require a loop here to extract displayable names.
		Spinner contextChooser3 = (Spinner) findViewById(R.id.contextChooser3);
		ArrayAdapter<ReadingContext> adapter3 = new ArrayAdapter<ReadingContext>(
			this, android.R.layout.simple_spinner_item, ReadingContext.values());
        contextChooser3.setAdapter(adapter3);
        contextChooser3.setOnItemSelectedListener(listener);

    }
    
    OnItemSelectedListener listener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> spinner, View view,
                int pos, long id) {
        	String viewName = "a spinner";
        	if (view != null) {
        		switch (spinner.getId()) {
        		case R.id.contextChooser1: viewName = "Chooser 1"; break;
        		case R.id.contextChooser2: viewName = "Chooser 2"; break;
        		case R.id.contextChooser3: viewName = "Chooser 3"; break;
        		}
        	}
            Toast.makeText(MainActivity.this,
                "You selected " + spinner.getSelectedItem() + " from " + 
                	viewName, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> spinner) {
            Toast.makeText(MainActivity.this,
                "Nothing selected.", Toast.LENGTH_SHORT).show();
        }
    };

}
