package com.example.vanlife.model.map;

import com.example.vanlife.R;

/**
 *
 * Created by teiva on 28/02/18.
 */

public enum Type {

    SHOWERS(R.drawable.ic_shower, R.string.SHOWER),
    WC(R.drawable.ic_toilet, R.string.WC),

    //park you car
    PARKING(R.drawable.ic_parking, R.string.PARKING_SPOT),

    //take beautiful picture
    INSTA_SPOT(R.drawable.ic_instaspot, R.string.INSTA_SPOT),

    //spot to park your van for the night or to rest freely and legally
    //could be in the forest, at a camp, in a camping, or on a parking spot
    VAN_SPOT(R.drawable.ic_vanspot, R.string.VAN_SPOT),

    SURF_SPOT(R.drawable.ic_surfspot, R.string.SURF_SPOT),

    OTHER(R.drawable.ic_unknown, R.string.OTHER);

    //TODO: add both + string afeter icon to get a clean string

    private int type;
    private int description;

    Type(int icon_id, int description) {
        this.type = icon_id;
        this.description = description;
    }

    public int getType(){
        return type;
    }

    public int getDescription(){
        return description;
    }
}
