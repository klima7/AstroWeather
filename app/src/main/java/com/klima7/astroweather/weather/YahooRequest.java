package com.klima7.astroweather.weather;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YahooRequest extends JsonRequest {

    private Gson gson = new Gson();
    private String unit;

    public static final String appId = "e4fr98gu";
    public static final String CONSUMER_KEY = "dj0yJmk9UnhwNXhycDNSdGhBJmQ9WVdrOVpUUm1jams0WjNVbWNHbzlNQT09JnM9Y29uc3VtZXJzZWNyZXQmc3Y9MCZ4PTRm";
    public static final String CONSUMER_SECRET = "6f307dc157458f96a02def7f040b143ae2e3b3f3";
    public static final String baseUrl = "https://weather-ydn-yql.media.yahoo.com/forecastrss";

    public YahooRequest(String unit, Response.Listener<Entry> listener, Response.ErrorListener errorListener) {
        super(Request.Method.GET, null, null, listener, errorListener);
        this.unit = unit;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        OAuthConsumer consumer = new OAuthConsumer(null, CONSUMER_KEY, CONSUMER_SECRET, null);
        consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.HMAC_SHA1);
        OAuthAccessor accessor = new OAuthAccessor(consumer);
        try {
            OAuthMessage request = accessor.newRequestMessage(OAuthMessage.GET, getUrl(), null);
            String authorization = request.getAuthorizationHeader(null);
            headers.put("Authorization", authorization);
        } catch (Exception e) {
            throw new AuthFailureError(e.getMessage());
        }

        headers.put("X-Yahoo-App-Id", appId);
        headers.put("Content-Type", "application/json");
        return headers;
    }

    @Override
    protected Response<Entry> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Entry parsedResponse = parseResponse(json);
            return Response.success(
                    parsedResponse,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    public Entry parseResponse(String jsonObject) {
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
        Forecast[] forecastsArray = gson.fromJson(forecastPartJson, Forecast[].class);
        List<Forecast> forecasts = Arrays.asList(forecastsArray);

        Entry info = new Entry(location, unit, currentWeather, forecasts);
        return info;
    }

    public String getUnit() {
        return unit;
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