package com.planday.deliveroo.model.response.direction;

import com.google.gson.annotations.SerializedName;

/**
 * Created by longnguyen on 10:29 PM, 7/20/18.
 */
public class Bounds {

    @SerializedName("northeast")
    private Northeast northeast;

    @SerializedName("southwest")
    private Southwest southwest;

    public Northeast getNortheast() {
        return northeast;
    }

    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    public Southwest getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }

}
