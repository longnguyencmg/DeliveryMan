package com.planday.deliveroo.model.response.direction;

import com.google.gson.annotations.SerializedName;

/**
 * Created by longnguyen on 10:31 PM, 7/20/18.
 */
public class Distance_ {

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
