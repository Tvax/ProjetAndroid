package com.example.teiva.testdrawer.map;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import static org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay.backgroundColor;
import static org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay.fontColor;
import static org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay.fontSizeDp;

/**
 * Lel
 * Created by teiva on 29/01/18.
 */

public class PointOfInterest {
    private GeoPoint geoPoint;
    private MapView mapView;
    private Marker marker;

    private String description;
    public void setDescription(String description){
        this.description = description;
    }

    //a voir ca se trouve c'est pas ca qu'il faut utiliser
    //Image imageHint;
    //String description;

    public Marker getMarker(){
        return marker;
    }

    public PointOfInterest(GeoPoint geoPoint, MapView mapView){
        this.geoPoint = geoPoint;
        this.mapView = mapView;
        setDefaultMarker();
    }

    public PointOfInterest(GeoPoint geoPoint, MapView mapView, String description){
        this(geoPoint, mapView);
        this.description = description;
    }



    private void setDefaultMarker() {
        Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;
        //build the PointOfInterest
        marker = new Marker(mapView);
        marker.setTextLabelBackgroundColor(backgroundColor);
        marker.setTextLabelFontSize(fontSizeDp);
        marker.setTextLabelForegroundColor(fontColor);
        marker.setTitle("hello world");
        //must set the icon to null last
        marker.setIcon(null);
        marker.setPosition(geoPoint);
    }

    public Marker getNewMarker() {
        if(marker == null){
            setDefaultMarker();
        }
        return marker;
    }
}
