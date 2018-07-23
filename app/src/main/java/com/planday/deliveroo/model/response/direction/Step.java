package com.planday.deliveroo.model.response.direction;

import com.google.gson.annotations.SerializedName;

/**
 * Created by longnguyen on 10:37 PM, 7/20/18.
 */
public class Step {
    @SerializedName("distance")
    private Distance_ distance;

    @SerializedName("duration")
    private Duration_ duration;

    @SerializedName("end_location")
    private EndLocation_ endLocation;

    @SerializedName("html_instructions")
    private String htmlInstructions;

    @SerializedName("polyline")
    private Polyline polyline;

    @SerializedName("start_location")
    private StartLocation_ startLocation;

    @SerializedName("travel_mode")
    private String travelMode;

    @SerializedName("maneuver")
    private String maneuver;

    public Distance_ getDistance() {
        return distance;
    }

    public void setDistance(Distance_ distance) {
        this.distance = distance;
    }

    public Duration_ getDuration() {
        return duration;
    }

    public void setDuration(Duration_ duration) {
        this.duration = duration;
    }

    public EndLocation_ getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(EndLocation_ endLocation) {
        this.endLocation = endLocation;
    }

    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public StartLocation_ getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(StartLocation_ startLocation) {
        this.startLocation = startLocation;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getManeuver() {
        return maneuver;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }
}
