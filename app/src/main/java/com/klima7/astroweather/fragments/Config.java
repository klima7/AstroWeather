package com.klima7.astroweather.fragments;

import java.io.Serializable;

public class Config implements Serializable {

    private float latitude;
    private float longitude;
    private int refresh;

    public Config(float latitude, float longitude, int refresh) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.refresh = refresh;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public int getRefresh() {
        return refresh;
    }
}
