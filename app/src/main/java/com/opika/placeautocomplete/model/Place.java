package com.opika.placeautocomplete.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Taufik Akbar on 17/11/2016.
 */

public class Place implements Serializable {

    @SerializedName("geometry")
    private PlaceGeometry placeGeometry;
    @SerializedName("name")
    private String name;
    @SerializedName("icon")
    private String iconPlace;
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("vicinity")
    private String vicinity;

    public PlaceGeometry getPlaceGeometry() {
        return placeGeometry;
    }

    public void setPlaceGeometry(PlaceGeometry placeGeometry) {
        this.placeGeometry = placeGeometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconPlace() {
        return iconPlace;
    }

    public void setIconPlace(String iconPlace) {
        this.iconPlace = iconPlace;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
}
