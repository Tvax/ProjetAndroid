package com.example.vanlife.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.vanlife.model.map.Map;

/**
 *
 * Created by teiva on 28/02/18.
 */

public final class GatewayDatabase {

    private final static String LAT = "lat";
    private final static String LONG = "long";
    private final static String MAP = "Map";
    private final static String ZOOM = "zoom";

    //LOS ANGELES
    private final static float DEFAULT_LAT = (float) 34.052235;
    private final static float DEFAULT_LONG = (float) -118.243683;
    private final static int DEFAULT_ZOOM = 10;

    private final static String SETTINGS = "SETTINGS";
    private final static String TIPS = "TIPS";
    private static final boolean DEFAULT_TIPS = true;


    public static void saveMap(Map map, Context context){
        SharedPreferences prefs = context.getSharedPreferences(MAP, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = prefs.edit();

        editor.putFloat(LAT, (float) map.getMapView().getBoundingBox().getCenter().getLatitude());
        editor.putFloat(LONG, (float) map.getMapView().getBoundingBox().getCenter().getLongitude());
        editor.putInt(ZOOM, map.getMapView().getZoomLevel());

        editor.apply();
    }

    public static void loadMap(Map map, Context context){
        SharedPreferences prefs = context.getSharedPreferences(MAP, Context.MODE_PRIVATE);
        map.setMapViewStartingPoint(prefs.getFloat(LAT, DEFAULT_LAT), prefs.getFloat(LONG, DEFAULT_LONG), prefs.getInt(ZOOM, DEFAULT_ZOOM));
    }

    public static void setTips(boolean activateTips, Context context){
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(TIPS, activateTips);
        editor.apply();
    }

    public static boolean getTips(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return prefs.getBoolean(TIPS, DEFAULT_TIPS);
    }
}
