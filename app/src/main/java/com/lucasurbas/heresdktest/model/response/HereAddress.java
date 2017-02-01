package com.lucasurbas.heresdktest.model.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HereAddress {

    @Expose @SerializedName("text")
    private String htmlText;

    public String getHtmlText() {
        return htmlText;
    }
}
