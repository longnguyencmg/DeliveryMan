package com.planday.deliveroo.model.response.direction;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by longnguyen on 10:36 PM, 7/20/18.
 */
public class Route {
    @SerializedName("bounds")
    private Bounds bounds;

    @SerializedName("copyrights")
    private String copyrights;

    @SerializedName("legs")
    private List<Leg> legs = null;

    @SerializedName("overview_polyline")
    private OverviewPolyline overviewPolyline;

    @SerializedName("summary")
    private String summary;

    @SerializedName("warnings")
    private List<Object> warnings = null;

    @SerializedName("waypoint_order")
    private List<Integer> waypointOrder = null;

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Object> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Object> warnings) {
        this.warnings = warnings;
    }

    public List<Integer> getWaypointOrder() {
        return waypointOrder;
    }

    public void setWaypointOrder(List<Integer> waypointOrder) {
        this.waypointOrder = waypointOrder;
    }
}
