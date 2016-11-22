package com.opika.placeautocomplete.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Taufik Akbar on 17/11/2016.
 */

public class PlaceResponse implements Serializable {

    @SerializedName("error_message")
    private String errorMessage;
    @SerializedName("results")
    private ArrayList<Place> placeArrayList;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ArrayList<Place> getPlaceArrayList() {
        return placeArrayList;
    }

    public void setPlaceArrayList(ArrayList<Place> placeArrayList) {
        this.placeArrayList = placeArrayList;
    }
}
