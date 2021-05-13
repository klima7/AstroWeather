package com.klima7.astroweather.weather;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.klima7.astroweather.Unit;

public class YahooLocationRequest extends YahooRequest {

    private Gson gson = new Gson();
    private String place;

    public YahooLocationRequest(String place, Unit unit, Response.Listener<Entry> listener, Response.ErrorListener errorListener) {
        super(unit, listener, errorListener);
        setShouldCache(true);
        this.place = place;
    }

    @Override
    public String getUrl() {
        return baseUrl + String.format("?location=%s&format=json&u=%s", place, getUnit().getYahooId());
    }
}
