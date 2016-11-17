package com.OSM;

import java.util.ArrayList;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class OSM extends Activity {

    private MapView mapView;
    private MapController mapController;
    private ItemizedOverlay<OverlayItem> myLocationOverlay;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mapView = (MapView) this.findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(15);
        GeoPoint mapCenter = new GeoPoint(53554070, -2959520); 
        GeoPoint overlayPoint = new GeoPoint(53554070 + 1000, -2959520 + 1000); 
        mapController.setCenter(mapCenter);
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        items.add(new OverlayItem("New Overlay", "Overlay Sample Description", overlayPoint));

        this.myLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items, null,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(OSM.this, "Overlay Titled: " + item.getTitle() + " Single Tapped" + "\n" + "Description: " + item, Toast.LENGTH_LONG).show();
                        return true; 
                    }
                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        Toast.makeText(OSM.this, "Overlay Titled: " + item.getTitle() + " Long pressed" + "\n" + "Description: " + item,Toast.LENGTH_LONG).show();
                        return false;
                    }
                });
        this.mapView.getOverlays().add(this.myLocationOverlay);
        mapView.invalidate();


    }

}
