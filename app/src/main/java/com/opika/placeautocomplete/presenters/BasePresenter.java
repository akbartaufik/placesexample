package com.opika.placeautocomplete.presenters;

/**
 * Created by Taufik on 1/18/2016.
 */
public interface BasePresenter<V> {

    void attachView(V view);

    void detachView();
}
