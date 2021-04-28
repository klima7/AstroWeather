package com.klima7.astroweather;

import androidx.lifecycle.ViewModel;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

public class AstroData extends ViewModel {

    private AstroCalculator.SunInfo sunInfo;
    private AstroCalculator.MoonInfo moonInfo;

    private float longitude;
    private float latitude;

    private float refreshPeriod;
    private long lastRefresh;

    public void refresh() {
        AstroDateTime time = new AstroDateTime();
        AstroCalculator.Location location = new AstroCalculator.Location(latitude, longitude);
        AstroCalculator calculator = new AstroCalculator(time, location);

        sunInfo = calculator.getSunInfo();
        moonInfo = calculator.getMoonInfo();

        lastRefresh = System.currentTimeMillis();
    }

    public AstroCalculator.SunInfo getSunInfo() {
        return sunInfo;
    }

    public AstroCalculator.MoonInfo getMoonInfo() {
        return moonInfo;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getRefreshPeriod() {
        return refreshPeriod;
    }

    public long getLastRefresh() {
        return lastRefresh;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setRefreshPeriod(float refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
    }
}
