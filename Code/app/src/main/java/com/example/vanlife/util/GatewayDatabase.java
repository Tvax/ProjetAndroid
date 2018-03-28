package com.example.vanlife.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.vanlife.model.map.Map;

/**
 * Class to make the link between the database and the application
 *
 * @author tefleury, thpradier
 */

public final class GatewayDatabase {
    /**
     * latitude name
     */
    private final static String LAT = "lat";
    /**
     * longitude name
     */
    private final static String LONG = "long";
    /**
     * map name
     */
    private final static String MAP = "Map";
    /**
     * zoom name
     */
    private final static String ZOOM = "zoom";

    /**
     * default latitude (EUROPE)
     */
    private final static float DEFAULT_LAT = (float) 53;
    /**
     * default longitude (EUROPE)
     */
    private final static float DEFAULT_LONG = (float) 9;
    /**
     * default zoom
     */
    private final static int DEFAULT_ZOOM = 6;

    /**
     * settings name
     */
    private final static String SETTINGS = "SETTINGS";
    /**
     * tips name
     */
    private final static String TIPS = "TIPS";
    /**
     * value which sets the tips display
     */
    private static final boolean DEFAULT_TIPS = true;

    /**
     * save map with all PointOfInterests on the database
     * 
     * @param map map where display all PointOfInterest
     * @param context application context
     */
    public static void saveMap(Map map, Context context){
        SharedPreferences prefs = context.getSharedPreferences(MAP, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = prefs.edit();

        editor.putFloat(LAT, (float) map.getMapView().getBoundingBox().getCenter().getLatitude());
        editor.putFloat(LONG, (float) map.getMapView().getBoundingBox().getCenter().getLongitude());
        editor.putInt(ZOOM, map.getMapView().getZoomLevel());

        editor.apply();
    }

    /**
     * load map with all PointOfInterests with the database
     * 
     * @param map map where display all PointOfInterest
     * @param context application context
     */
    public static void loadMap(Map map, Context context){
        SharedPreferences prefs = context.getSharedPreferences(MAP, Context.MODE_PRIVATE);
        map.setMapViewStartingPoint(prefs.getFloat(LAT, DEFAULT_LAT), prefs.getFloat(LONG, DEFAULT_LONG), prefs.getInt(ZOOM, DEFAULT_ZOOM));
    }

    /**
     * set tips on the database
     * 
     * @param activateTips value which defines if the tips is active
     * @param context application context
     */
    public static void setTips(boolean activateTips, Context context){
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(TIPS, activateTips);
        editor.apply();
    }

    /**
     * get tips with the database
     * 
     * @param context application context
     */
    public static boolean getTips(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return prefs.getBoolean(TIPS, DEFAULT_TIPS);
    }
}
