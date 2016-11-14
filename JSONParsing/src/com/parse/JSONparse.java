package com.parse;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Simplistic demo of using JSON - real example would use network
 * connection and a threading solution
 * @author Rachee Singh
 */
public class JSONparse extends Activity {
	
	TextView json;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try {
			String jsonString = getJsonString();
			JSONObject jsonObject = new JSONObject(jsonString);
			String name = jsonObject.getString("name"); 
			String age = jsonObject.getString("age");
			String address = jsonObject.getString("address");
			String phone = jsonObject.getString("phone");
			String jsonText=name + "\n" + age + "\n" + address + "\n" + phone;
	    	json= (TextView)findViewById(R.id.json);
	    	json.setText(jsonText);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }

    /** Mock up some JSON data */
	private String getJsonString() {
		JSONObject string = new JSONObject();
		try {
			string.put("name", "John Doe");
			string.put("age", new Integer(25));
			string.put("address", "75 Ninth Avenue 2nd and 4th Floors New York, NY 10011");
			string.put("phone", "8367667829");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return string.toString();
	}
}