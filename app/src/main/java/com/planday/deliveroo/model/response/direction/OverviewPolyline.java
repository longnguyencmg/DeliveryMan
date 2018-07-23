package com.planday.deliveroo.model.response.direction;

import com.google.gson.annotations.SerializedName;

/**
 * Created by longnguyen on 10:35 PM, 7/20/18.
 */
public class OverviewPolyline {
    @SerializedName("points")
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
