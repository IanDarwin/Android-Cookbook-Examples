package com.examples;

import java.util.List;

import net.droidsolutions.droidcharts.core.data.XYDataset;
import net.droidsolutions.droidcharts.core.data.xy.XYSeries;
import net.droidsolutions.droidcharts.core.data.xy.XYSeriesCollection;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class DemoCharts extends Activity {
	private static final String TAG = "DemoCharts";
	private final String chartTitle = "My Daily Starbucks Allowance";
	private final String xLabel = "Week Day";
	private final String yLabel = "Allowance";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "DemoCharts.onCreate()");

		// Get the passed parameter values
		List<double[]> wrappedList = (List<double[]>) getIntent().getSerializableExtra("param");
		Log.d(TAG, "passed extra " + wrappedList + " is of type " + wrappedList.getClass());
		double[][] data = (double[][]) wrappedList.toArray(new double[wrappedList.size()][]);

		Log.d(TAG, "Data Param:= " + data);

		XYDataset dataset = createDataset("My Daily Starbucks Allowance", data);
		XYLineChartView graphView = new XYLineChartView(this, chartTitle,
				xLabel, yLabel, dataset);
		setContentView(graphView);
	}

	/**
	 * Creates an XYDataset from the raw data passed in.
	 */
	private XYDataset createDataset(String title, double[][] dataVals) {
		final XYSeries series1 = new XYSeries(title);
		for (double[] tuple : dataVals) {
			series1.add(tuple[0], tuple[1]);
		}

		// Create a collection to hold various data sets
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		return dataset;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Toast.makeText(this, "Orientation Change", Toast.LENGTH_SHORT).show();

		// Let's get back to our DemoList view
		Intent intent = new Intent(this, DemoList.class);
		startActivity(intent);

		// Finish current Activity
		this.finish();
	}
}