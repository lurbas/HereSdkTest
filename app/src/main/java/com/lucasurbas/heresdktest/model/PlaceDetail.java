package com.lucasurbas.heresdktest.model;


public class PlaceDetail {

    private String name;
    private String address;
    private String browserUrl;

    public PlaceDetail(String name, String address, String browserUrl) {
        this.name = name;
        this.address = address;
        this.browserUrl = browserUrl;
    }

    public String getName() {
        return name;
    }

    public String getBrowserUrl() {
        return browserUrl;
    }

    public String getAddress() {
        return address;
    }
}
