package com.examples;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DemoList extends ListActivity implements OnItemClickListener
	{
		private static final String tag = "DemoList";
		private ListView listview;
		private ArrayAdapter<String> listAdapter;

		// Want to pass data values as parameters to next Activity/View/Page
		private String params;

		// Our data for plotting
		private final double[][] data = { { 1, 1.0 }, { 2.0, 4.0 }, { 3.0, 10.0 }, { 4, 2.0 }, { 5.0, 20 }, { 6.0, 4.0 }, { 7.0, 1.0 }, };

		@Override
		public void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);

				// Set the View Layer
				setContentView(R.layout.data_listview);

				// Get the Default declared ListView @android:list
				listview = getListView();

				// List for click events to the ListView items
				listview.setOnItemClickListener(this);

				// Get the data to
				ArrayList<String> dataList = getDataStringList(data);

				// Create an Adapter to for viewing the ListView
				listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);

				// Bind the adapter to the ListView
				listview.setAdapter(listAdapter);

				// Set the parameters to pass to the next view/ page
				setParameters(data);
			}

		private String doubleArrayToString(double[][] dataVals)
			{
				StringBuilder strBuilder = new StringBuilder();
				for (int i = 0; i < dataVals.length; i++)
					{
						String datum = "[" + String.valueOf(dataVals[i][0]) + "," + String.valueOf(dataVals[i][1]) + "]";

						if (i < dataVals.length - 1)
							{
								strBuilder.append(datum + " , ");
							}
						else
							{
								strBuilder.append(datum);
							}
					}
				return strBuilder.toString();
			}

		/**
		 * Sets parameters for the Bundle
		 * 
		 * @param dataList
		 */
		private void setParameters(double[][] dataVals)
			{
				params = toJSON(dataVals);
			}

		public String getParameters()
			{
				return this.params;
			}

		/**
		 * Need todo JSONArray
		 * 
		 * @param dataVals
		 * @return
		 */
		private String toJSON(double[][] dataVals)
			{
				StringBuilder strBuilder = new StringBuilder();

				strBuilder.append("[");
				strBuilder.append(doubleArrayToString(dataVals));
				strBuilder.append("]");
				return strBuilder.toString();
			}

		/**
		 * 
		 * @param dataVals
		 * @return
		 */
		private ArrayList<String> getDataStringList(double[][] dataVals)
			{
				ArrayList<String> list = new ArrayList<String>();

				// TODO: CONVERT INTO JSON FORMAT
				for (int i = 0; i < dataVals.length; i++)
					{
						String datum = "[" + String.valueOf(dataVals[i][0]) + "," + String.valueOf(dataVals[i][1]) + "]";
						list.add(datum);
					}
				return list;
			}

		@Override
		public void onConfigurationChanged(Configuration newConfig)
			{
				super.onConfigurationChanged(newConfig);

				// Create an Intent to switch view to the next page view
				Intent intent = new Intent(this, DemoCharts.class);

				// Pass parameters along to the next page
				intent.putExtra("param", getParameters());

				// Start the activity
				startActivity(intent);

				Log.d(tag, "Orientation Change...");
				Log.d(tag, "Params: " + getParameters());
			}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long duration)
			{
				// Upon clicking item in list pop a toast
				String msg = "#Item: " + String.valueOf(position) + " - " + listAdapter.getItem(position);
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			}
	}
