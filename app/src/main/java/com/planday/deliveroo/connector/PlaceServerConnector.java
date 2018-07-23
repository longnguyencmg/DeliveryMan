package com.planday.deliveroo.connector;

import com.planday.deliveroo.api.PlaceAPI;
import com.planday.deliveroo.model.MapPlace;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by longnguyen on 9:22 PM, 7/19/18.
 */
public class PlaceServerConnector extends BaseServerConnector<PlaceAPI> {

    public PlaceServerConnector() {
        super(PlaceAPI.class);
    }

    @Override
    protected Retrofit getRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = getRetrofit(EXAMPLE_URL);
        }
        return mRetrofit;
    }

    public Observable<List<MapPlace>> getAllPlaces() {
        return mApi.getAllPlaces();
    }
}
