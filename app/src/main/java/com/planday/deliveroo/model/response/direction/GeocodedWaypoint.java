package com.planday.deliveroo.model.response.direction;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by longnguyen on 10:33 PM, 7/20/18.
 */
public class GeocodedWaypoint {

    @SerializedName("geocoder_status")
    private String geocoderStatus;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("types")
    private List<String> types = null;

    public String getGeocoderStatus() {
        return geocoderStatus;
    }

    public void setGeocoderStatus(String geocoderStatus) {
        this.geocoderStatus = geocoderStatus;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

}
