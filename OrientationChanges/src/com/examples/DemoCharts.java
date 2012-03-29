package com.examples;

import java.util.ArrayList;

import net.droidsolutions.droidcharts.core.data.XYDataset;
import net.droidsolutions.droidcharts.core.data.xy.XYSeries;
import net.droidsolutions.droidcharts.core.data.xy.XYSeriesCollection;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class DemoCharts extends Activity
	{
		private static final String tag = "DemoCharts";
		private final String chartTitle = "My Daily Starbucks Allowance";
		private final String xLabel = "Week Day";
		private final String yLabel = "Allowance";

		/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);

				// Access the Extras from the Bundle
				Bundle params = getIntent().getExtras();

				// If we get no parameters, we do nothing
				if (params == null) { return; }

				// Get the passed parameter values
				String paramVals = params.getString("param");

				Log.d(tag, "Data Param:= " + paramVals);
				Toast.makeText(getApplicationContext(), "Data Param:= " + paramVals, Toast.LENGTH_LONG).show();

				ArrayList<ArrayList<Double>> dataVals = stringArrayToDouble(paramVals);

				XYDataset dataset = createDataset("My Daily Starbucks Allowance", dataVals);
				XYLineChartView graphView = new XYLineChartView(this, chartTitle, xLabel, yLabel, dataset);
				setContentView(graphView);
			}

		private String arrayToString(String[] data)
			{
				StringBuilder strBuilder = new StringBuilder();
				for (int i = 0; i < data.length; i++)
					{
						strBuilder.append(data[i]);
					}
				return strBuilder.toString();
			}

		private String cleanNumericString(String val)
			{
				return val.replaceAll("\\[", "").replaceAll("\\]", "").trim();
			}

		private ArrayList<ArrayList<Double>> stringArrayToDouble(String paramVals)
			{
				ArrayList<ArrayList<Double>> plotVals = new ArrayList<ArrayList<Double>>();
				if (paramVals.startsWith("[") && paramVals.endsWith("]"))
					{
						String[] vals = paramVals.substring(1, paramVals.length() - 1).split(" , ");
						for (String v : vals)
							{
								if (v.startsWith("[") && v.endsWith("]"))
									{
										String[] dataVals = v.split(",");

										String xvalStr = cleanNumericString(dataVals[0]);
										String yvalStr = cleanNumericString(dataVals[1]);
										Log.d(paramVals, xvalStr + " - " + yvalStr);

										// Convert to Numeric Values
										Double x = Double.parseDouble(xvalStr);
										Double y = Double.parseDouble(yvalStr);

										// Create (x,y) tuple for data point
										ArrayList<Double> list1 = new ArrayList<Double>();
										list1.add(x);
										list1.add(y);

										// Add to our final list
										plotVals.add(list1);
									}
								Log.d(tag, "Values to plot: " + plotVals.toString());
							}
					}
				return plotVals;
			}

		/**
		 * Creates a sample dataset.
		 * 
		 * @return a sample dataset.
		 */
		private XYDataset createDataset(String title, ArrayList<ArrayList<Double>> dataVals)
			{
				final XYSeries series1 = new XYSeries(title);
				for (ArrayList<Double> tuple : dataVals)
					{
						double x = tuple.get(0).doubleValue();
						double y = tuple.get(1).doubleValue();

						series1.add(x, y);
					}

				// Create a collection to hold various data sets
				final XYSeriesCollection dataset = new XYSeriesCollection();
				dataset.addSeries(series1);
				return dataset;
			}

		@Override
		public void onConfigurationChanged(Configuration newConfig)
			{
				super.onConfigurationChanged(newConfig);
				Toast.makeText(this, "Orientation Change", Toast.LENGTH_SHORT);

				// Lets get back to our DemoList view
				Intent intent = new Intent(this, DemoList.class);
				startActivity(intent);

				// Finish current Activity
				this.finish();
			}
	}