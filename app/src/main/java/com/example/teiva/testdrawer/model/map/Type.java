package com.example.teiva.testdrawer.model.map;

import com.example.teiva.testdrawer.R;

/**
 *
 * Created by teiva on 28/02/18.
 */

public enum Type {
    //TODO: ajouter des drawables parce que la c'est vraiment moche
    SHOWERS(R.drawable.ic_shower),
    WC(R.drawable.ic_toilet),

    //park you car
    PARKING(R.drawable.ic_parking),

    //take beautiful picture
    INSTA_SPOT(R.drawable.ic_instaspot),

    //spot to park your van for the night or to rest freely and legally
    //could be in the forest, at a camp, in a camping, or on a parking spot
    VAN_SPOT(R.drawable.ic_vanspot);

    private int type;

    Type(int type) {
        this.type = type;
    }

    public int type(){
        return type;
    }
}
