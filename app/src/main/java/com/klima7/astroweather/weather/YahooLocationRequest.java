package com.klima7.astroweather.weather;

import com.android.volley.Response;

public class YahooLocationRequest extends YahooRequest {

    private String place;

    public YahooLocationRequest(String place, String unit, Response.Listener<Entry> listener, Response.ErrorListener errorListener) {
        super(unit, listener, errorListener);
        setShouldCache(true);
        this.place = place;
    }

    @Override
    public String getUrl() {
        return baseUrl + String.format("?location=%s&format=json&u=%s", place, getUnit());
    }
}
