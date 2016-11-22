package com.opika.placeautocomplete.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Taufik Akbar on 17/11/2016.
 */

public class PlaceGeometry {

    @SerializedName("location")
    private PlaceLocation location;

    public PlaceLocation getLocation() {
        return location;
    }

    public void setLocation(PlaceLocation location) {
        this.location = location;
    }
}
