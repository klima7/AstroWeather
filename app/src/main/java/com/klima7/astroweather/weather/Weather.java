package com.klima7.astroweather.weather;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.klima7.astroweather.Unit;

import java.util.List;

@Entity(primaryKeys = {"woeid", "unit"})
public class Weather {

    public int woeid;
    @NonNull public Unit unit;

    public int chill;
    public int direction;
    public double speed;

    public int humidity;
    public double visibility;
    public double pressure;
    public int rising;

    public String text;
    public int code;
    public int temperature;

    public String date;

    public List<Forecast> forecasts;
}
