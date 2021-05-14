package com.klima7.astroweather.weather;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Location {

    @PrimaryKey
    public int woeid;
    public String city;
    public String region;
    public String country;
    public String timezone_id;
    @SerializedName("lat") public double latitude;
    @SerializedName("long") public double longitude;

    @Override
    public String toString() {
        return "Location{" +
                "woeid=" + woeid +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", country='" + country + '\'' +
                ", timezone_id='" + timezone_id + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
