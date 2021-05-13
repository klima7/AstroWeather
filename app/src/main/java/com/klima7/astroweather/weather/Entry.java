package com.klima7.astroweather.weather;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.klima7.astroweather.db.DataConverter;

import java.util.List;

@Entity
public class Entry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Embedded
    private Location location;

    @Embedded
    private CurrentWeather current;

    private List<Forecast> future;

    private String unit;

    public Entry(Location location, String unit, CurrentWeather current, List<Forecast> future) {
        this.location = location;
        this.current = current;
        this.future = future;
        this.unit = unit;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id=" + id +
                ", location=" + location +
                ", current=" + current +
                ", future=" + future +
                '}';
    }
}
