package com.klima7.astroweather.weather;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class YahooWeatherRequest extends YahooRequest<Weather> {

    public static final String IMPERIAL_UNIT = "f";
    public static final String METRIC_UNIT = "c";

    private Gson gson = new Gson();
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

    @Override
    public Weather parseResponse(String jsonObject) {
        JsonObject fullJson = gson.fromJson(jsonObject, JsonObject.class);

        JsonObject locationJson = fullJson.getAsJsonObject("location");
        Location location = gson.fromJson(locationJson, Location.class);

        JsonObject observationPartJson = fullJson.getAsJsonObject("current_observation");
        JsonObject windPartJson = observationPartJson.getAsJsonObject("wind");
        JsonObject atmospherePartJson = observationPartJson.getAsJsonObject("atmosphere");
        JsonObject astronomyPartJson = observationPartJson.getAsJsonObject("astronomy");
        JsonObject conditionPartJson = observationPartJson.getAsJsonObject("condition");
        JsonObject observationMergedJson = merge(windPartJson, atmospherePartJson, astronomyPartJson, conditionPartJson);
        CurrentWeather currentWeather = gson.fromJson(observationMergedJson, CurrentWeather.class);

        JsonArray forecastPartJson = fullJson.getAsJsonArray("forecasts");
        Forecast[] forecasts = gson.fromJson(forecastPartJson, Forecast[].class);

        Weather info = new Weather(location, currentWeather, forecasts);
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
