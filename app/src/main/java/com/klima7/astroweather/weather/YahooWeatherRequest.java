package com.klima7.astroweather.weather;

import com.android.volley.Response;
import com.klima7.astroweather.Unit;

public class YahooWeatherRequest extends YahooRequest {

    private int woeid;

    public YahooWeatherRequest(int woeid, Unit unit, Response.Listener<Entry> listener, Response.ErrorListener errorListener) {
        super(unit, listener, errorListener);
        setShouldCache(false);

        this.woeid = woeid;
    }

    @Override
    public String getUrl() {
        return baseUrl + String.format("?woeid=%d&format=json&u=%s", woeid, getUnit().getYahooId());
    }
}
