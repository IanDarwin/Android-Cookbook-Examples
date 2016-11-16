package com.halfclosed.wordpress;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This activity shows the progress of
 * fetching data from the server and
 * doing the necessary calculations.
 * 
 * @author Emaad Ahmed Manzoor
 *
 */
public class Progress extends Activity {
    
    private LocationManager lm;
    private String city;
    private String locality;
    private String searchText;
    private TextView progressTextView;
    private String[] myLocation = new String[2];
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress);
        
        Bundle extras = getIntent().getExtras();
        city = extras.getString("city");
        locality = extras.getString("locality");
        searchText = extras.getString("searchtext");
        
        progressTextView = (TextView) findViewById(R.id.progress_text);
        
        /* Get current GPS coordinates */
        
        
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);        
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                
            }
            
            @Override
            public void onProviderEnabled(String provider) {
                
            }
            
            @Override
            public void onProviderDisabled(String provider) {
                
            }
            
            @Override
            public void onLocationChanged(Location location) {
                
            }
        });
            
        /* Set a mock location for debugging purposes */
        setMockLocation(15.387653, 73.872585, 500);
        //setMockLocation(50, 36, 500);
        
        /* Calculate GPS coordinates */
        class GPSTask extends AsyncTask<Void, Void, Void> {
            
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressTextView.setText("Getting location...");
            }
            
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                while (true) {
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                            myLocation[0] = Double.toString(location.getLatitude());
                            myLocation[1] = Double.toString(location.getLongitude());
                            break;
                    }
                }
                return null;
            }
            
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                progressTextView.setText(progressTextView.getText() + "\nLat: " + myLocation[0] + " Long: " + myLocation[1]);
                startJustDialTask();
            }                 
        }
        new GPSTask().execute((Void)null);  
    }
    
    private void startJustDialTask() {
        /* Fetch JustDial search results */
        String[] searchParams = new String[] {searchText, city, locality};
        
        class JustDialTask extends AsyncTask<String[], Integer, JSONArray> {
                        
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressTextView.setText(progressTextView.getText() + "\nGetting search data...");
            }
            
            @Override
            protected JSONArray doInBackground(String[]... params) {
                
                StringBuilder responseBuilder = new StringBuilder();
                String query = searchText + " " + locality + " " + city;
                HashMap<String, String> results = null;
                JSONArray  resultArray = null;
                
                /* Get search results as JSON*/
                try {
                    URL url = new  URL("http://ajax.googleapis.com/ajax/services/search/local?v=1.0&q="
                                        + URLEncoder.encode(query, "UTF-8"));
    
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String inputLine;
                    
                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }
                    
                    in.close();
                } catch (MalformedURLException me) {
                        me.printStackTrace();
                } catch (UnsupportedEncodingException ue) {
                        ue.printStackTrace();
                } catch (IOException ie) {
                        ie.printStackTrace();
                }
                
                /* Parse JSON and store the data in an array */
                try {
                    JSONObject  json = new JSONObject(responseBuilder.toString());
                    resultArray = json.getJSONObject("responseData").getJSONArray("results");                                       
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                
                return resultArray;
            }
            
            @Override
            protected void onPostExecute(JSONArray result) {
                super.onPostExecute(result);
                progressTextView.setText(progressTextView.getText() + "\nReceived search data: " + result.length() + " matches.");
                getDistanceData(result);
            }           
        };       
       new JustDialTask().execute(searchParams);
    }
    
    private void getDistanceData(final JSONArray resultArray) {
        
        class DistanceTask extends AsyncTask<JSONArray, Integer, HashMap<String, String>> {
            
            HashMap<String, String> results = new HashMap<String, String>();
            
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressTextView.setText(progressTextView.getText() + "\nGetting distance data.\nThis might take a while...");
            }

            @Override
            protected HashMap<String, String> doInBackground(JSONArray... params) {
                
                StringBuilder responseBuilder = new StringBuilder();
                
                for (int i = 0; i < resultArray.length(); i++) {
                    String resultName = null, addressString = null;
                    try {
                        resultName = resultArray.getJSONObject(i).getString("titleNoFormatting");
                        addressString = resultArray.getJSONObject(i).getJSONArray("addressLines").join(" ").replace('"', ' ');
                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    Log.d("ADDRESS STRING", addressString);
                    
                    /* Get the coordinates of the address */
                    Geocoder g = new Geocoder(Progress.this);
                    List<Address> addresses;
                    String lat = "", longi = "", distance = "";
                    try {
                        addresses = g.getFromLocationName(addressString, 1);
                        if (addresses.size() > 0) {
                            lat = Double.toString(addresses.get(0).getLatitude());
                            longi = Double.toString(addresses.get(0).getLongitude());
                            
                            /* Find the distance between you and the location */
                            responseBuilder = new StringBuilder();
                            try {
                                URL url = new  URL("http://maps.google.com/maps/api/directions/json?origin=" 
                                                    + lat + "," + longi
                                                    + "&destination="
                                                    + myLocation[0] + "," + myLocation[1]
                                                    + "&sensor=false");
                                Log.d("URL", url.toString());
                                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                                String inputLine;
                                
                                while ((inputLine = in.readLine()) != null) {
                                    responseBuilder.append(inputLine);
                                }
                                
                                in.close();
                            } catch (MalformedURLException me) {
                                    me.printStackTrace();
                            } catch (UnsupportedEncodingException ue) {
                                    ue.printStackTrace();
                            } catch (IOException ie) {
                                    ie.printStackTrace();
                            }
                            
                            JSONObject json = new JSONObject(responseBuilder.toString());
                            Log.d("DISTANCE JSON", json.toString());
                            if (json.getString("status").equalsIgnoreCase("NOT_FOUND")) {
                                distance = "";
                            } else {
                                distance = json.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text");
                            }
                              Log.d("DISTANCE", distance);
                        }
                    } catch (Exception e) {}
                    
                    resultName = resultName + " " + distance;
                    results.put(resultName, addressString);
                }                                      
                
            return results;
        }
            
        @Override
        protected void onPostExecute(HashMap<String, String> result) {
                super.onPostExecute(result);
                progressTextView.setText(progressTextView.getText() + "\nRetreived distance data.");
                
                Intent showResults = new Intent(Progress.this, Results.class);
                Bundle params = new Bundle();
                
                String[] resultStrings = new String[result.keySet().size()];
                result.keySet().toArray(resultStrings);
                Log.d("RESULTSSTRING", resultStrings.toString());
                params.putStringArray("results", resultStrings);
                
                String[] addressStrings = new String[result.values().size()];
                result.values().toArray(addressStrings);
                Log.d("ADDRESS", addressStrings.toString());
                params.putStringArray("addresses", addressStrings);
                
                showResults.putExtras(params);
                showResults.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(showResults);
                
                finish();
            }
        }
        new DistanceTask().execute(resultArray);
    }

    private void setMockLocation(double latitude, double longitude, float accuracy) {
        // This coding style is a way of handling a long list of boolean parameters
        lm.addTestProvider (LocationManager.GPS_PROVIDER,
                "requiresNetwork" == "",
                "requiresSatellite" == "",
                "requiresCell" == "",
                "hasMonetaryCost" == "",
                "supportsAltitude" == "",
                "supportsSpeed" == "",
                "supportsBearing" == "",
                android.location.Criteria.POWER_LOW,
                android.location.Criteria.ACCURACY_FINE);      

        Location newLocation = new Location(LocationManager.GPS_PROVIDER);

        newLocation.setLatitude(latitude);
        newLocation.setLongitude(longitude);
        newLocation.setAccuracy(accuracy);
        newLocation.setTime(System.currentTimeMillis());
        
        lm.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
        lm.setTestProviderStatus(LocationManager.GPS_PROVIDER,LocationProvider.AVAILABLE,null,System.currentTimeMillis());      
        lm.setTestProviderLocation(LocationManager.GPS_PROVIDER, newLocation);      
    }

}

class HttpHelper {
    
    public static String request(HttpResponse response){
        String result = "";
        try{
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                str.append(line + "\n");
            }
            in.close();
            result = str.toString();
        }catch(Exception ex){
            result = "Error";
        }
        return result;
    }
}

/**
 * Utility class for performing HTTP GET and HTTP POST requests.
 *
 * @author craignewton <newtondev@gmail.com>
 *
 */
class CustomHttpClient {
   
    /** The time it takes for our client to timeout */
    public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
   
    /** Single instance of our HttpClient */
    private static HttpClient mHttpClient;
   
    /**
     * Get our single instance of our HttpClient object.
     *
     * @return an HttpClient object with connection parameters set
     */
    private static HttpClient getHttpClient() {
        if (mHttpClient == null) {
            mHttpClient = new DefaultHttpClient();
            final HttpParams params = mHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
            ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
        }
        return mHttpClient;
    }
   
    /**
     * Performs an HTTP Post request to the specified url with the
     * specified parameters.
     *
     * @param url The web address to post the request to
     * @param postParameters The parameters to send via the request
     * @return The result of the request
     * @throws Exception
     */
    public static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();            
            HttpPost request = new HttpPost(url);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
           
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
           
            String result = sb.toString();
            return result;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}