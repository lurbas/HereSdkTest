package com.lucasurbas.heresdktest.model.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceResponse {

    @Expose
    private String placeId;
    @Expose
    private String name;
    @Expose @SerializedName("view")
    private String browserUrl;
    @Expose
    private HereLocation location;

    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    public String getBrowserUrl() {
        return browserUrl;
    }

    public HereLocation getLocation() {
        return location;
    }
}
