package com.klima7.astroweather.weather;

import com.android.volley.Response;
import com.google.gson.Gson;

public class YahooLocationRequest extends YahooRequest {

    private Gson gson = new Gson();
    private String place;

    public YahooLocationRequest(String place, Response.Listener<Weather> listener, Response.ErrorListener errorListener) {
        super(listener, errorListener);
        setShouldCache(true);
        this.place = place;
    }

    @Override
    public String getUrl() {
        return baseUrl + String.format("?location=%s&format=json&u=c", place);
    }
}
