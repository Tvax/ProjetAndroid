package com.example.vanlife.firebase;

import com.example.vanlife.model.map.Type;


/**
 * Class representing a point of interest (PointOfInterest) on database Firebase
 *
 * @author tefleury, thpradier
 */


public class POIFirebase {
    /**
     * PointOfInterest id
     */
    public String id;
    /**
     * PointOfInterest latitude
     */
    public double lat;
    /**
     * PointOfInterest longitude
     */
    public double lng;
    /**
     * PointOfInterest type
     */
    public Type type;
    /**
     * PointOfInterest description
     */
    public String description;

    /**
     * Constructor of POIFirebase without parameters
     */
    public POIFirebase(){}


    /**
     * Constructor of POIFirebase with parameters
     * 
     * @param id PointOfInterest id
     * @param lat PointOfInterest latitude
     * @param lng PointOfInterest longitude
     * @param type PointOfInterest type
     * @param description PointOfInterest description
     */
    public POIFirebase(String id, double lat, double lng, Type type, String description){
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.type = type;
        this.description = description;
    }
}
