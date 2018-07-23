package com.planday.deliveroo.service;

import com.planday.deliveroo.connector.PlaceServerConnector;
import com.planday.deliveroo.model.MapPlace;

import java.util.List;

import rx.Observable;

/**
 * Created by longnguyen on 9:30 PM, 7/19/18.
 */
public class PlaceService {

    private PlaceServerConnector placeServerConnector;

    public PlaceService() {
        placeServerConnector = new PlaceServerConnector();
    }

    public Observable<List<MapPlace>> getAllPlaces() {
        return placeServerConnector.getAllPlaces();
    }
}
