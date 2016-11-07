package com.darwinsys.mapdemos;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends Activity {
	
	public static final String TAG = MainActivity.TAG;
	private GoogleMap map;
	final LatLng CENTER = new LatLng(43.661049, -79.400917);
	
	class Data {
		public Data(float lng, float lat, String title, String snippet) {
			super();
			this.lat = (float)lat;
			this.lng = (float)lng;
			this.title = title;
			this.snippet = snippet;
		}
		float lat;
		float lng;
		String title;
		String snippet;
	}
	
	Data[] data = {
			new Data(-79.400917f,43.661049f, "New New College Res",
					"Residence building (new concrete high-rise)"),
			new Data(-79.394524f,43.655796f, "Baldwin Street",
					"Here be many good restaurants!"),
			new Data(-79.402206f,43.657688f, "College St",
					"Many discount computer stores if you forgot a cable or need to buy hardware."),    
			new Data(-79.390381f,43.659878f, "Queens Park Subway",
					"Quickest way to the north-south (Yonge-University-Spadina) subway/metro line"),
			new Data(-79.403732f,43.666801f, "Spadina Subway",
					"Quickest way to the east-west (Bloor-Danforth) subway/metro line"),
			new Data(-79.399696f,43.667873f, "St George Subway back door",
					"Token-only admittance, else use Spadina or Bedford entrances!"),
			new Data(-79.384163f,43.655083f, "Eaton Centre (megamall)",
					"One of the largest indoor shopping centres in eastern Canada. Runs from Dundas to Queen."),
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "MapsActivity.onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        if (map == null) {
            String message = "Map Fragment Not Found or no Map in it!";
            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.DURATION_LONG).show();
            return;
        }

		for (Data d : data) {
		    LatLng location = new LatLng(d.lat, d.lng);
			map.addMarker(new MarkerOptions().position(location)
		          .title(d.title)
		          .snippet(d.snippet));
		}
		
		// Let the user see indoor maps if available.
		map.setIndoorEnabled(true);
		
		// Enable my-location stuff
		map.setMyLocationEnabled(true);
		
		map.moveCamera(CameraUpdateFactory.zoomTo(14));
	    map.animateCamera(CameraUpdateFactory.newLatLng(CENTER), 1750, null);
	}
}
