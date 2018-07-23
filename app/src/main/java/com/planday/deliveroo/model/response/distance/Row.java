package com.planday.deliveroo.model.response.distance;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by longnguyen on 9:29 PM, 7/20/18.
 */
public class Row {
    @SerializedName("elements")
    private List<Element> elements = null;

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }
}
