package com.opika.placeautocomplete.api;

import com.opika.placeautocomplete.model.PlaceResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Taufik Akbar on 17/11/2016.
 */

public interface ApiService {

    @GET(ApiConfig.URL_NEARBY)
    Observable<PlaceResponse> getNearby(@Query("location") String location, @Query("radius") String radius,
                                        @Query("type") String type, @Query("key") String apikey);
}
