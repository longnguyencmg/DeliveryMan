package com.planday.deliveroo.api;

import com.planday.deliveroo.model.MapPlace;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by longnguyen on 9:04 PM, 7/19/18.
 */
public interface PlaceAPI {

    @GET("api/places")
    Observable<List<MapPlace>> getAllPlaces();
}
