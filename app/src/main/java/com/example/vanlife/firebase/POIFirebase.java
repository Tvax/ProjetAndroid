package com.example.vanlife.firebase;

import com.example.vanlife.model.map.Type;

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

    public POIFirebase(String id, double lat, double lng, Type type, String description){
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.type = type;
        this.description = description;
    }
}
