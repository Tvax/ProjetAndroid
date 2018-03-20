package com.example.vanlife.model.map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.vanlife.R;
import com.example.vanlife.view.PointOfInterestFragment;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 *
 * Created by teiva on 29/01/18.
 */

public class PointOfInterest{
    private final static String ID = "ID";
    private final static String DESCRIPTION = "DESCRIPTION";
    private final static String TYPE = "TYPE";
    private final static String GEOPOINT = "GEOPOINT";
    private final static String REGEX_RM = "[-.]";
    private final static String NULL_CHAR = "";

    private Marker marker;
    private Type type;
    private String id;

    private void setId(double lat, double lng) {
        this.id = String.valueOf(lat + lng).replaceAll(REGEX_RM, NULL_CHAR);
    }

    public String getId() {return id; }

    Marker getMarker(){
        return marker;
    }

    public Type getType(){return type;}
    double getLat(){return marker.getPosition().getLatitude();}
    double getLong(){return marker.getPosition().getLongitude();}


    private PointOfInterest(GeoPoint geoPoint, Type type){
        this.type = type;
        setId(geoPoint.getLatitude(), geoPoint.getLongitude());
    }

    public PointOfInterest(GeoPoint geoPoint, MapView mapView, Type type, String description, Context context, android.support.v4.app.FragmentManager supportFragmentManager){
        this(geoPoint, type);
        setBasicMarkerOptions(context, geoPoint, mapView, supportFragmentManager, description);
    }

    private void setBasicMarkerOptions(final Context context, final GeoPoint geoPoint, MapView mapView, final FragmentManager fragmentManager, final String description) {
        Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;
        marker = new Marker(mapView);
        marker.setSubDescription(description);


        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                PointOfInterestFragment fragment = new PointOfInterestFragment();

                Bundle bundle = new Bundle();

                bundle.putSerializable(GEOPOINT, geoPoint);
                bundle.putString(TYPE, context.getString(type.getDescription()));
                bundle.putString(DESCRIPTION, description);
                bundle.putString(ID, id);

                fragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_placeholder, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
                return false;
            }
        });

        marker.setPosition(geoPoint);
        //ne pas enlever default title sinon bug

        marker.setIcon(context.getResources().getDrawable(type.getType()));
    }
}