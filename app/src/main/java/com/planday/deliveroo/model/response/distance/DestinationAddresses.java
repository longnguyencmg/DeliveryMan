package com.planday.deliveroo.model.response.distance;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by longnguyen on 9:29 PM, 7/20/18.
 */
public class DestinationAddresses {
    @SerializedName("destination_addresses")
    private List<String> destinationAddresses = null;
    @SerializedName("origin_addresses")
    private List<String> originAddresses      = null;
    @SerializedName("rows")
    private List<Row>    rows                 = null;
    @SerializedName("status")
    private String status;

    public List<String> getDestinationAddresses() {
        return destinationAddresses;
    }

    public void setDestinationAddresses(List<String> destinationAddresses) {
        this.destinationAddresses = destinationAddresses;
    }

    public List<String> getOriginAddresses() {
        return originAddresses;
    }

    public void setOriginAddresses(List<String> originAddresses) {
        this.originAddresses = originAddresses;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
