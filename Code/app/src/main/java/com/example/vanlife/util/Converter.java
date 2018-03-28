package com.example.vanlife.util;

import android.content.Context;

import com.example.vanlife.firebase.POIFirebase;
import com.example.vanlife.model.map.Map;
import com.example.vanlife.model.map.PointOfInterest;

import org.osmdroid.util.GeoPoint;

/**
 * Class to convert POIFirebase to PointOfInterest
 *
 * @author tefleury, thpradier
 */

public final class Converter {
	/**
     * Returns to PointOfInterest with POIFirebase
     * 
     * @param map map where display all PointOfInterest
     * @param context application context
     * @param supportFragmentManager fragment manager
     * @return PointOfInterest
     */
    public static PointOfInterest POIFirebaseTopointOfInterest(POIFirebase poiFirebase, Map map, Context context, android.support.v4.app.FragmentManager fragmentManager) {
        return new PointOfInterest(new GeoPoint(poiFirebase.lat, poiFirebase.lng), map.getMapView(), poiFirebase.type, poiFirebase.description, context, fragmentManager);
    }
}
