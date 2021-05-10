package com.klima7.astroweather.yahoo;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class YahooWeatherRequest extends YahooRequest<YahooWeather> {

    public static final String IMPERIAL_UNIT = "f";
    public static final String METRIC_UNIT = "c";

    private Gson gson = new Gson();
    private int woeid;
    private String unit;

    public YahooWeatherRequest(int woeid, String unit, Response.Listener<YahooWeather> listener, Response.ErrorListener errorListener) {
        super(listener, errorListener);
        setShouldCache(false);

        this.woeid = woeid;
        this.unit = unit;
    }

    @Override
    public String getUrl() {
        return baseUrl + String.format("?woeid=%d&format=json&u=%s", woeid, unit);
    }

    @Override
    public YahooWeather parseResponse(String jsonObject) {
        JsonObject fullJson = gson.fromJson(jsonObject, JsonObject.class);

        JsonObject locationJson = fullJson.getAsJsonObject("location");
        YahooLocation location = gson.fromJson(locationJson, YahooLocation.class);

        JsonObject observationPartJson = fullJson.getAsJsonObject("current_observation");
        JsonObject windPartJson = observationPartJson.getAsJsonObject("wind");
        JsonObject atmospherePartJson = observationPartJson.getAsJsonObject("atmosphere");
        JsonObject astronomyPartJson = observationPartJson.getAsJsonObject("astronomy");
        JsonObject conditionPartJson = observationPartJson.getAsJsonObject("condition");
        JsonObject observationMergedJson = merge(windPartJson, atmospherePartJson, astronomyPartJson, conditionPartJson);
        YahooCurrentWeather currentWeather = gson.fromJson(observationMergedJson, YahooCurrentWeather.class);

        JsonArray forecastPartJson = fullJson.getAsJsonArray("forecasts");
        YahooForecast[] forecasts = gson.fromJson(forecastPartJson, YahooForecast[].class);

        YahooWeather info = new YahooWeather(location, currentWeather, forecasts);
        return info;
    }

    private JsonObject merge(JsonObject... objects) {
        JsonObject merged = new JsonObject();
        for(JsonObject object : objects) {
            for (String key : object.keySet()) {
                merged.add(key, object.get(key));
            }
        }
        return merged;
    }
}
