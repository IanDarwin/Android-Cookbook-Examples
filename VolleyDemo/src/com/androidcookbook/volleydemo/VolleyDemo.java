package com.androidcookbook.volleydemo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Simple REST request using Volley library, a quasi-supported Google API for networking
 */
public class VolleyDemo extends Activity {

	RequestQueue queue;
	TextView mTextView;

	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.main);
		mTextView = (TextView) findViewById(R.id.text);

		// Set up the Volley queue for REST processing
		queue = Volley.newRequestQueue(this);
	}

	public void fetchResults(View v) {

		// Create a RequestQueue.
		RequestQueue queue = Volley.newRequestQueue(this);

		String host ="http://androidcookbook.com/";
		String baseUrl ="seam/resource/rest/";
		String listUrl = "chapter/list";

		final Response.Listener<String> successListener = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// Display the first 500 characters of the response string.
				mTextView.setText("Response is: "+ response.substring(0,500));
			}
		};
		final Response.ErrorListener failListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mTextView.setText("That didn't work!");
			}
		};

		// Create a String Request to get information from the provided URL.
		String requestUrl = host + baseUrl + listUrl;
		StringRequest stringRequest =
			new StringRequest(Request.Method.GET, requestUrl, successListener, failListener);

		// Queue the request to do the sending and receiving
		queue.add(stringRequest);
	}
}
