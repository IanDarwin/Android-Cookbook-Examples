package com.androidcookbook.volleydemo;

import org.json.JSONArray;
import org.json.JSONException;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Simple REST request using Volley library, a quasi-supported Google API for networking
 * See http://developer.android.com/training/volley/index.html
 */
public class VolleyDemo extends Activity {

	private RequestQueue queue;
	private TextView mTextView;
	private EditText mSearchBox;

	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.main);
		mSearchBox = (EditText) findViewById(R.id.searchbox);
		mTextView = (TextView) findViewById(R.id.text);

		// Set up the Volley queue for REST processing
		queue = Volley.newRequestQueue(this);
	}

	/**
	 * The action begins here
	 * @param v The Interface that launched a thousand queries
	 */
	public void fetchResults(View v) {

		String host ="https://suggestqueries.google.com/";
		// Amusingly, client=firefox makes the output come back in JSON
		String baseUrl ="complete/search?output=toolbar&hl=en&client=firefox&q=";
		String listUrl = mSearchBox.getText().toString();

		if (listUrl.length() == 0) {
			Toast.makeText(this, "Input required!", Toast.LENGTH_SHORT).show();
			return;
		}

		// Create a String Request to get information from the provided URL.
		String requestUrl = host + baseUrl + listUrl;
		JsonArrayRequest request = new JsonArrayRequest(
				requestUrl, successListener, failListener);

		// Queue the request to do the sending and receiving
		queue.add(request);
	}
	
	/**
	 * What we get back from this particular web service is a JSON array containing:
	 * 0) A JSON String containing the query string
	 * 1) A JSON ARRAY of strings with the results;
	 */
	final Response.Listener<JSONArray> successListener = new Response.Listener<JSONArray>() {
		@Override
		public void onResponse(JSONArray response) {
			try {
				String query = response.getString(0);
				mTextView.append("Original query: " + query + "\n");
				JSONArray rest = response.getJSONArray(1);
				mTextView.setText("We got " + rest.length() + " results:\n");
				for (int i = 0; i < rest.length(); i++) {
					mTextView.append(rest.getString(i) + "\n");
				}
			} catch (JSONException e) {
				mTextView.append("\n);");
				mTextView.append("FAIL: " + e);
				e.printStackTrace();
			}
		}
	};
	
	final Response.ErrorListener failListener = new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			mTextView.setText("That didn't work!\n" + 
				"Error: " + error + "\n" +
				"Detail:" + error.getMessage() + "\n" +
				"Cause: " + error.getCause());
			error.printStackTrace();
		}
	};
}
