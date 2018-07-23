package com.planday.deliveroo.api;

import com.planday.deliveroo.model.response.direction.GeocodedWaypoints;
import com.planday.deliveroo.model.response.distance.DestinationAddresses;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by longnguyen on 8:35 PM, 7/20/18.
 */
public interface GoogleAPI {

    @GET("distancematrix/json")
    Observable<DestinationAddresses> getDistanceMatrix(@Query("unit") String unit,
                                         @Query("origins") String origins,
                                         @Query("destinations") String destinations,
                                         @Query("mode") String mode,
                                         @Query("key") String apiKey);

    @GET("directions/json")
    Observable<GeocodedWaypoints> getDirection(@Query("origin") String origin,
                                               @Query("destination") String destination,
                                               @Query("waypoints") String wayPoints,
                                               @Query("mode") String mode,
                                               @Query("key") String apiKey);
}
