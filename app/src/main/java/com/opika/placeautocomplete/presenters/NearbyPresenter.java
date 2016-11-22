package com.opika.placeautocomplete.presenters;

import android.content.Context;
import android.content.res.Resources;

import com.opika.placeautocomplete.R;
import com.opika.placeautocomplete.api.ApiService;
import com.opika.placeautocomplete.dagger.App;
import com.opika.placeautocomplete.model.PlaceResponse;
import com.opika.placeautocomplete.views.NearbyView;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Taufik Akbar on 17/11/2016.
 */

public class NearbyPresenter implements BasePresenter<NearbyView> {

    @Inject
    ApiService apiService;

    private PlaceResponse placeResponse;
    private NearbyView nearbyView;
    private Subscription subscription;
    private Context mContext;

    public NearbyPresenter(Context mContext) {
        this.mContext = mContext;
        App.appComponent().inject(this);
    }

    @Override
    public void attachView(NearbyView view) {
        this.nearbyView = view;
    }

    @Override
    public void detachView() {
        this.nearbyView = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void getNearbyPlaces(String location, String radius, String type){
        nearbyView.showProgressDialogIndicator();
        if (subscription != null) subscription.unsubscribe();

        subscription = apiService.getNearby(location, radius, type, mContext.getResources().getString(R.string.google_maps_key))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<PlaceResponse>() {
                    @Override
                    public void onCompleted() {
                        if (placeResponse.getPlaceArrayList() != null) {
                            nearbyView.onSuccess(placeResponse.getPlaceArrayList());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        nearbyView.onFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(PlaceResponse placeResponse) {
                        NearbyPresenter.this.placeResponse = placeResponse;

                    }
                });
    }
}
