package com.planday.deliveroo.model;

import java.io.Serializable;

/**
 * Created by longnguyen on 9:07 PM, 7/19/18.
 */
public class MapPlace implements Serializable{

    private String name;
    private Double latitude;
    private Double longitude;
    private String description;

    public MapPlace(){

    }

    public MapPlace(String name, Double latitude, Double longitude, String description) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
