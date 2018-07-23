package com.planday.deliveroo.connector;

import com.planday.deliveroo.api.GoogleAPI;
import com.planday.deliveroo.model.response.direction.GeocodedWaypoints;
import com.planday.deliveroo.model.response.distance.DestinationAddresses;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by longnguyen on 9:05 PM, 7/20/18.
 */
public class GoogleServerConnector extends BaseServerConnector<GoogleAPI> {

    public GoogleServerConnector() {
        super(GoogleAPI.class);
    }

    @Override
    protected Retrofit getRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = getRetrofit(GOOGLE_API);
        }
        return mRetrofit;
    }

    public Observable<DestinationAddresses> getDistanceMatrix(String unit, String origins, String destinations, String mode, String apiKey) {
        return mApi.getDistanceMatrix(unit, origins, destinations, mode, apiKey);
    }

    public Observable<GeocodedWaypoints> getDirection(String origin, String destination, String wayPoints, String mode, String apiKey) {
        return mApi.getDirection(origin, destination, wayPoints, mode, apiKey);
    }
}
