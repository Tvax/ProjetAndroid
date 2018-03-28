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
 * Class representing a point of interest on map
 *
 * @author tefleury, thpradier
 */

public class PointOfInterest{
    /**
     * PointOfInterest id name
     */
    private final static String ID = "ID";
    /**
     * PointOfInterest description name
     */
    private final static String DESCRIPTION = "DESCRIPTION";
    /**
     * PointOfInterest type name
     */
    private final static String TYPE = "TYPE";
    /**
     * PointOfInterest geopoint name
     */
    private final static String GEOPOINT = "GEOPOINT";
    /**
     * regex
     */
    private final static String REGEX_RM = "[-.]";
    /**
     * empty string
     */
    private final static String NULL_CHAR = "";

    /**
     * point of interest geographical marker
     */
    private Marker marker;
    /**
     * point of interest type
     */
    private Type type;
    /**
     * point of interest id
     */
    private String id;

    /**
     * Create and set an id to a point of interest with latitude and longitude
     * 
     * @param lat point of interest latitude
     * @param lng point of interest longitude
     */
    private void setId(double lat, double lng) {
        this.id = String.valueOf(lat + lng).replaceAll(REGEX_RM, NULL_CHAR);
    }

    /**
     * Returns to point of interest id
     *
     * @return point of interest id
     */
    public String getId() {return id; }
    /**
     * Returns to point of interest geographical marker
     *
     * @return point of interest geographical marker
     */
    Marker getMarker(){ return marker; }
    /**
     * Returns to point of interest type
     *
     * @return point of interest type
     */
    public Type getType(){ return type; }
    /**
     * Returns to point of interest latitude
     *
     * @return point of interest latitude
     */
    double getLat(){return marker.getPosition().getLatitude();}
    /**
     * Returns to point of interest longitude
     *
     * @return point of interest longitude
     */
    double getLong(){return marker.getPosition().getLongitude();}

    /**
     * Constructor of PointOfInterest with parameters
     * 
     * @param geoPoint point of interest geographical point
     * @param type point of interest type
     */
    private PointOfInterest(GeoPoint geoPoint, Type type){
        this.type = type;
        setId(geoPoint.getLatitude(), geoPoint.getLongitude());
    }

    /**
     * Constructor of PointOfInterest with parameters and place the point of interest on the map
     * 
     * @param geoPoint point of interest geographical point
     * @param mapView map view to display
     * @param type point of interest type
     * @param description point of interest description
     * @param context application context
     * @param supportFragmentManager fragment manager
     */
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