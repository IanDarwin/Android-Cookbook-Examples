package com.example.javascriptdatademo;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

public class ArrayApplication extends Application {
	
	static final String TAG = "JavascriptDataDemo";

	double[] data = new double[] {42.6, 24, 17, 15.4};
	
	/** This passes our data out to the JS */
	@JavascriptInterface
	public String getData() {
		Log.d(TAG, "getData() called");
		return a1dToJson(data).toString();
	}
	
	/** Allow the JavaScript to pass some data in to us. */
	@JavascriptInterface
	public void setData(String newData) throws JSONException {
		Log.d(TAG, "MainActivity.setData()");
		JSONArray streamer = new JSONArray(newData);
		data = new double[streamer.length()];
		for (int i = 0; i < streamer.length(); i++) {
			Double n = streamer.getDouble(i);
			data[i] = n;
		}
	}
	
	private Activity activity;
	
	public Context getActivity() {
		return activity;
	}

	public void setActivity(Activity app) {
		this.activity = app;
	}

	@JavascriptInterface
	public void finish() {
		Log.d(TAG, "ArrayApplication.finish()");
		activity.finish();
	}
	
	/** Sorry for not using the standard org.json.JSONArray but even in Android 4.2 it lacks
	 * the JSONArray(Object[]) constructor, making it too painful to use.
	 */
	private String a1dToJson(double[] data) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < data.length; i++) {
			double d = data[i];
			if (i > 0)
				sb.append(",");
			sb.append(d);
		}
		sb.append("]");
		return sb.toString();
	}
}
