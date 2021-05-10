package com.klima7.astroweather.yahoo;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class YahooLocationRequest extends YahooRequest<YahooLocation> {

    private Gson gson = new Gson();
    private String place;

    public YahooLocationRequest(String place, Response.Listener<YahooLocation> listener, Response.ErrorListener errorListener) {
        super(listener, errorListener);
        setShouldCache(true);
        this.place = place;
    }

    @Override
    public String getUrl() {
        return baseUrl + String.format("?location=%s&format=json&u=c", place);
    }

    @Override
    public YahooLocation parseResponse(String jsonObject) {
        JsonObject fullJson = gson.fromJson(jsonObject, JsonObject.class);
        JsonObject locationJson = fullJson.getAsJsonObject("location");
        YahooLocation location = gson.fromJson(locationJson, YahooLocation.class);
        return location;
    }
}
