package com.example.vanlife.model.map;

import com.example.vanlife.R;

/**
 * Enumeration representing a point of interest type
 *
 * @author tefleury, thpradier
 */

public enum Type {
    /**
     * To take a shower
     */
    SHOWERS(R.drawable.ic_shower, R.string.SHOWER),
    /**
     * to meet primary need
     */
    WC(R.drawable.ic_toilet, R.string.WC),
    /**
     * To park you car
     */
    PARKING(R.drawable.ic_parking, R.string.PARKING_SPOT),

    /**
     * To take beautiful picture
     */
    INSTA_SPOT(R.drawable.ic_instaspot, R.string.INSTA_SPOT),

    /**
     * spot to park your van for the night or to rest freely and legally.
     * could be in the forest, at a camp, in a camping, or on a parking spot
     */
    VAN_SPOT(R.drawable.ic_vanspot, R.string.VAN_SPOT),

    /**
     * spot to surf
     */
    SURF_SPOT(R.drawable.ic_surfspot, R.string.SURF_SPOT),

    /**
     * other type
     */
    OTHER(R.drawable.ic_unknown, R.string.OTHER);

    /**
     * PointOfInterest icon
     */
    private int type;
    /**
     * PointOfInterest description
     */
    private int description;

    /**
     * Constructor of Type with parameters
     * 
     * @param icon_id PointOfInterest icon
     * @param description PointOfInterest description
     */
    Type(int icon_id, int description) {
        this.type = icon_id;
        this.description = description;
    }

    /**
     * Returns to PointOfInterest icon
     *
     * @return PointOfInterest icon
     */
    public int getType(){
        return type;
    }
    /**
     * Returns to PointOfInterest description
     *
     * @return PointOfInterest description
     */
    public int getDescription(){
        return description;
    }
}
