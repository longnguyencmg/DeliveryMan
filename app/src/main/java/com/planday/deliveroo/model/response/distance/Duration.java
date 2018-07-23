package com.planday.deliveroo.model.response.distance;

import com.google.gson.annotations.SerializedName;

/**
 * Created by longnguyen on 9:30 PM, 7/20/18.
 */
public class Duration {
    @SerializedName("text")
    private String text;

    @SerializedName("value")
    private Integer value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
