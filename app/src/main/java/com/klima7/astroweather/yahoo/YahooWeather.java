package com.klima7.astroweather.yahoo;

import java.util.Arrays;

public class YahooWeather {

    private YahooLocation location;
    private YahooCurrentWeather current;
    private YahooForecast[] future;

    public YahooWeather(YahooLocation location, YahooCurrentWeather current, YahooForecast[] future) {
        this.location = location;
        this.current = current;
        this.future = future;
    }

    public YahooLocation getLocation() {
        return location;
    }

    public YahooCurrentWeather getCurrent() {
        return current;
    }

    public YahooForecast[] getFuture() {
        return future;
    }

    @Override
    public String toString() {
        return "WeatherInfo{" +
                "location=" + location +
                ", current=" + current +
                ", future=" + Arrays.toString(future) +
                '}';
    }
}
