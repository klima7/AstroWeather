package com.klima7.astroweather.weather;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

public class YahooWeatherRequest extends YahooRequest<Weather> {

    private int woeid;
    private String unit;

    public YahooWeatherRequest(int woeid, String unit, Response.Listener<Weather> listener, Response.ErrorListener errorListener) {
        super(unit, String.format("?woeid=%d&format=json&u=%s", woeid, unit), listener, errorListener);
        setShouldCache(false);
        this.woeid = woeid;
        this.unit = unit;
    }

    @Override
    public Weather parseResponse(String jsonObject) {
        Gson gson = new Gson();
        JsonObject fullJson = gson.fromJson(jsonObject, JsonObject.class);

        JsonObject observationPartJson = fullJson.getAsJsonObject("current_observation");
        JsonObject windPartJson = observationPartJson.getAsJsonObject("wind");
        JsonObject atmospherePartJson = observationPartJson.getAsJsonObject("atmosphere");
        JsonObject astronomyPartJson = observationPartJson.getAsJsonObject("astronomy");
        JsonObject conditionPartJson = observationPartJson.getAsJsonObject("condition");
        JsonObject observationMergedJson = merge(windPartJson, atmospherePartJson, astronomyPartJson, conditionPartJson);
        Weather weather = gson.fromJson(observationMergedJson, Weather.class);

        JsonArray forecastPartJson = fullJson.getAsJsonArray("forecasts");
        Forecast[] forecastsArray = gson.fromJson(forecastPartJson, Forecast[].class);
        List<Forecast> forecasts = Arrays.asList(forecastsArray);

        weather.forecasts = forecasts;
        weather.woeid = woeid;
        weather.unit = unit;

        return weather;
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
