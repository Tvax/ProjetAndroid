package com.example.teiva.testdrawer.firebase;

import com.example.teiva.testdrawer.model.map.Type;

/**
 *
 * Created by tefleury on 02/03/18.
 */

public class POIFirebase {
    public String id;
    public double lat;
    public double lng;
    public Type type;
    public String description;

    public POIFirebase(){}

    public POIFirebase(double lat, double lng, Type type, String description){
        this.id = String.valueOf(lat + lng).replaceAll("[-.]", "");
        this.lat = lat;
        this.lng = lng;
        this.type = type;
        this.description = description;
    }
}
