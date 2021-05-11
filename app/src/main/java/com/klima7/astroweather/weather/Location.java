package com.klima7.astroweather.weather;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Location {

    private String city;
    private String region;
    private int woeid;
    private String country;
    private String timezone_id;
    @SerializedName("lat") private double latitude;
    @SerializedName("long") private double longitude;

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public int getWoeid() {
        return woeid;
    }

    public String getCountry() {
        return country;
    }

    public String getTimezone_id() {
        return timezone_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isValid() {
        return woeid != 0;
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
                "city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", woeid=" + woeid +
                ", country='" + country + '\'' +
                ", timezone_id='" + timezone_id + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return woeid == location.woeid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(woeid);
    }
}