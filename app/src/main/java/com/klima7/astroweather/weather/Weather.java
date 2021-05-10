package com.klima7.astroweather.weather;

import java.util.Arrays;

public class Weather {

    private Location location;
    private CurrentWeather current;
    private Forecast[] future;

    public Weather(Location location, CurrentWeather current, Forecast[] future) {
        this.location = location;
        this.current = current;
        this.future = future;
    }

    public Location getLocation() {
        return location;
    }

    public CurrentWeather getCurrent() {
        return current;
    }

    public Forecast[] getFuture() {
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
