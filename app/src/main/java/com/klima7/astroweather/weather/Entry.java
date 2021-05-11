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

    public Entry(Location location, CurrentWeather current, List<Forecast> future) {
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
        return "Entry{" +
                "id=" + id +
                ", location=" + location +
                ", current=" + current +
                ", future=" + future +
                '}';
    }
}
