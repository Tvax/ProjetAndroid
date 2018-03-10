package com.example.teiva.testdrawer.model.map;

import android.content.Context;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 *
 * Created by teiva on 29/01/18.
 */

public class PointOfInterest {
    private Marker marker;
    private Type type;

    //type d'accomodation (WC, douche...)
    //private String type;

    //description du POI (Pratique, payant...)
    //private String description;

    //a voir ca se trouve c'est pas ca qu'il faut utiliser
    //Image imageHint;
    //String type;

    public Marker getMarker(){
        return marker;
    }
    public Type getType(){return type;}
    public double getLat(){return marker.getPosition().getLatitude();}
    public double getLong(){return marker.getPosition().getLongitude();}

    //default constuctor without paramateres for Firebase
    public PointOfInterest(){}


    private PointOfInterest(GeoPoint geoPoint, MapView mapView, Type type, Context context){
        this.type = type;
        setBasicMarkerOptions(context, geoPoint, mapView);
    }

    public PointOfInterest(GeoPoint geoPoint, MapView mapView, Type type, String description, Context context){
        this(geoPoint, mapView, type, context);
        marker.setSubDescription(description);
    }

    private void setBasicMarkerOptions(final Context context, GeoPoint geoPoint, MapView mapView) {
        Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;
        marker = new Marker(mapView);

        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(context, marker.getSubDescription(), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        //textsize
        //marker.setTextLabelBackgroundColor(type.Type());
        //marker.setTextLabelFontSize(100);
        //marker.setTextLabelForegroundColor(fontColor);
        //marker.setTitle("    ");

        marker.setPosition(geoPoint);
        //ne pas enlever default title sinon bug

        marker.setIcon(context.getResources().getDrawable(type.type()));
    }
}