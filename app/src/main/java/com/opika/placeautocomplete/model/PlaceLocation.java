package com.opika.placeautocomplete.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Taufik Akbar on 17/11/2016.
 */

public class PlaceLocation {

    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
