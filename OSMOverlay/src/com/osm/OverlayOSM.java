package com.osm;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class OverlayOSM extends Activity {

    private MapView mapView;
    private MapController mapController;
    private ItemizedOverlay<OverlayItem> myLocationOverlay;
    private ResourceProxy mResourceProxy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mapView = (MapView) this.findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapController = this.mapView.getController();
        mapController.setZoom(14);
        GeoPoint mapCenter = new GeoPoint(53554070, -2959520); 
        mapController.setCenter(mapCenter);
        List<OverlayItem> overlays = new ArrayList<OverlayItem>();
        GeoPoint overlayPoint = new GeoPoint(53554070 + 1000, -2959520 + 1000); 
        Drawable newMarker = this.getResources().getDrawable(R.drawable.marker_default);
        OverlayItem overlayItem = new OverlayItem("New Overlay", "Overlay Description", overlayPoint);
        overlayItem.setMarker(newMarker);
		overlays.add(overlayItem);

        mResourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
 
        this.myLocationOverlay = new ItemizedIconOverlay<OverlayItem>(overlays, newMarker, null, mResourceProxy);
        this.mapView.getOverlays().add(this.myLocationOverlay);
        mapView.invalidate();
    }

}
