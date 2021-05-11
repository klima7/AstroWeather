package com.klima7.astroweather.weather;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;

@Entity
public class Weather {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Location location;
    private CurrentWeather current;
    private Forecast[] future;

    public Weather(Location location, CurrentWeather current, Forecast[] future) {
        this.location = location;
        this.current = current;
        this.future = future;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setCurrent(CurrentWeather current) {
        this.current = current;
    }

    public void setFuture(Forecast[] future) {
        this.future = future;
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
