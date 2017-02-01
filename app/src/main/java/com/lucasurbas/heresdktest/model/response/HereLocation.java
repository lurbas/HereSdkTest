package com.lucasurbas.heresdktest.model.response;


import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class HereLocation {

    @Expose
    private ArrayList<Double> position;

    @Expose
    private HereAddress address;

    public ArrayList<Double> getPosition() {
        return position;
    }

    public HereAddress getAddress() {
        return address;
    }
}
