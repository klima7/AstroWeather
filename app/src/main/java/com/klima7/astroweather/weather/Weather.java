package com.klima7.astroweather.weather;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.klima7.astroweather.db.DataConverter;

import java.util.Arrays;
import java.util.List;

@Entity
public class Weather {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "weather_id")
    private int id;

    @Embedded
    private Location location;
    @Embedded
    private CurrentWeather current;
    @Embedded
    private List<Forecast> future;

    public Weather(Location location, CurrentWeather current, List<Forecast> future) {
        this.location = location;
        this.current = current;
        this.future = future;
    }

    public Weather() {

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

    @TypeConverters(DataConverter.class)
    public List<Forecast> getFuture() {
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

    public void setFuture(List<Forecast> future) {
        this.future = future;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", location=" + location +
                ", current=" + current +
                ", future=" + future +
                '}';
    }
}
