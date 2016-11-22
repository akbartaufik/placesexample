package com.opika.placeautocomplete.views;


import com.opika.placeautocomplete.model.Place;

import java.util.ArrayList;

/**
 * Created by Taufik on 17/11/2016.
 */
public interface NearbyView {
    void showProgressDialogIndicator();
    void onFailed(String message);
    void onSuccess(ArrayList<Place> arrayList);
}
