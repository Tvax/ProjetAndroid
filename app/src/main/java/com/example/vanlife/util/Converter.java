package com.example.vanlife.util;

import android.content.Context;

import com.example.vanlife.firebase.POIFirebase;
import com.example.vanlife.model.map.Map;
import com.example.vanlife.model.map.PointOfInterest;

import org.osmdroid.util.GeoPoint;

/**
 *
 * Created by teiva on 17/03/18.
 */

public final class Converter {
    public static PointOfInterest POIFirebaseTopointOfInterest(POIFirebase poiFirebase, Map map, Context context, android.support.v4.app.FragmentManager fragmentManager) {
        return new PointOfInterest(new GeoPoint(poiFirebase.lat, poiFirebase.lng), map.getMapView(), poiFirebase.type, poiFirebase.description, context, fragmentManager);
    }
}
