package com.klima7.astroweather.weather;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.List;

@Entity(primaryKeys = {"woeid", "unit"})
public class Weather {

    public int woeid;
    @NonNull public String unit;

    public int chill;
    public int direction;
    public double speed;

    public int humidity;
    public double visibility;
    public double pressure;
    public int rising;

    public String sunrise;
    public String sunset;

    public String text;
    public int code;
    public int temperature;

    public List<Forecast> forecasts;

    @Override
    public String toString() {
        return "Weather{" +
                "woeid=" + woeid +
                ", unit='" + unit + '\'' +
                ", chill=" + chill +
                ", direction=" + direction +
                ", speed=" + speed +
                ", humidity=" + humidity +
                ", visibility=" + visibility +
                ", pressure=" + pressure +
                ", rising=" + rising +
                ", sunrise='" + sunrise + '\'' +
                ", sunset='" + sunset + '\'' +
                ", text='" + text + '\'' +
                ", code=" + code +
                ", temperature=" + temperature +
                ", forecast=" + forecasts +
                '}';
    }
}
