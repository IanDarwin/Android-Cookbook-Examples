package com.examples;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DemoList extends ListActivity implements OnItemClickListener {
	private static final String TAG = "DemoList";
	private ListView listview;
	private ArrayAdapter<String> listAdapter;

	// Our data for plotting
	private final double[][] data = { 
			{ 1, 1.0 }, 
			{ 2.0, 4.0 }, 
			{ 3.0, 10.0 },
			{ 4, 2.0 }, 
			{ 5.0, 20 }, 
			{ 6.0, 4.0 }, 
			{ 7.0, 1.0 }, 
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "DemoList.onCreate()");

		// Set the View Layer
		setContentView(R.layout.data_listview);

		// Get the Default declared ListView @android:list
		listview = getListView();

		// List for click events to the ListView items
		listview.setOnItemClickListener(this);

		// Get the data to
		List<String> dataList = getDataStringList(data);

		// Create an Adapter to for viewing the ListView
		listAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);

		// Bind the adapter to the ListView
		listview.setAdapter(listAdapter);

	}

	/**
	 * Convert the double[] to List<String> for display
	 * @param dataVals
	 * @return
	 */
	private List<String> getDataStringList(double[][] dataVals) {
		List<String> list = new ArrayList<String>();

		for (int i = 0; i < dataVals.length; i++) {
			String datum = "[" + String.valueOf(dataVals[i][0]) + ","
					+ String.valueOf(dataVals[i][1]) + "]";
			list.add(datum);
		}
		return list;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Toast.makeText(this, "Orientation Change", Toast.LENGTH_SHORT).show();

		// Create an Intent to switch view to the Chart page view
		Intent intent = new Intent(this, DemoCharts.class);

		// The type "double[][]" does not go through as a SerializableExtra, so wrap in List.
		List<double[]> passedData = new ArrayList<double[]>();
		for (double[] dd : data) {
			passedData.add(dd);
		}
		
		// Pass parameters along to the next page
		intent.putExtra("param", (Serializable)passedData);
		

		// Start the activity
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long duration) {
		// Upon clicking item in list pop a toast
		String msg = "#Item: " + String.valueOf(position) + " - "
				+ listAdapter.getItem(position);
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}
}
