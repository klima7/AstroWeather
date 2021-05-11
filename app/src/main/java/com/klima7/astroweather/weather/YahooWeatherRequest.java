package com.klima7.astroweather.weather;

import com.android.volley.Response;

public class YahooWeatherRequest extends YahooRequest {

    public static final String IMPERIAL_UNIT = "f";
    public static final String METRIC_UNIT = "c";

    private int woeid;
    private String unit;

    public YahooWeatherRequest(int woeid, String unit, Response.Listener<Weather> listener, Response.ErrorListener errorListener) {
        super(listener, errorListener);
        setShouldCache(false);

        this.woeid = woeid;
        this.unit = unit;
    }

    @Override
    public String getUrl() {
        return baseUrl + String.format("?woeid=%d&format=json&u=%s", woeid, unit);
    }
}
