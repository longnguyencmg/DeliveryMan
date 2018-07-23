package com.planday.deliveroo.model.response.direction;

import com.google.gson.annotations.SerializedName;

/**
 * Created by longnguyen on 10:32 PM, 7/20/18.
 */
public class EndLocation {

    @SerializedName("lat")
    private Double lat;

    @SerializedName("lng")
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

}
