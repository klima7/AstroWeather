package com.klima7.astroweather.weather;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class YahooLocationRequest extends YahooRequest<Location> {

    public YahooLocationRequest(String place, Response.Listener<Location> listener, Response.ErrorListener errorListener) {
        super("c", String.format("?location=%s&format=json", place), listener, errorListener);
        setShouldCache(true);
    }

    @Override
    public Location parseResponse(String jsonObject) {
        Gson gson = new Gson();
        JsonObject fullJson = gson.fromJson(jsonObject, JsonObject.class);
        JsonObject locationJson = fullJson.getAsJsonObject("location");
        Location location = gson.fromJson(locationJson, Location.class);
        return location;
    }
}
