package com.planday.deliveroo.model.response.direction;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by longnguyen on 10:28 PM, 7/20/18.
 */
public class GeocodedWaypoints {
    @SerializedName("geocoded_waypoints")
    private List<GeocodedWaypoint> geocodedWaypoints = null;

    @SerializedName("routes")
    private List<Route> routes = null;

    @SerializedName("status")
    private String status;

    public List<GeocodedWaypoint> getGeocodedWaypoints() {
        return geocodedWaypoints;
    }

    public void setGeocodedWaypoints(List<GeocodedWaypoint> geocodedWaypoints) {
        this.geocodedWaypoints = geocodedWaypoints;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
