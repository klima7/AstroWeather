package com.klima7.astroweather;

import com.astrocalculator.AstroCalculator;

import java.io.Serializable;

public class DataWrapper implements Serializable {

    private AstroCalculator.Location location;
    private AstroCalculator.SunInfo sunInfo;
    private AstroCalculator.MoonInfo moonInfo;

    public DataWrapper(AstroCalculator.Location location, AstroCalculator.SunInfo sunInfo, AstroCalculator.MoonInfo moonInfo) {
        this.location = location;
        this.sunInfo = sunInfo;
        this.moonInfo = moonInfo;
    }

    public AstroCalculator.Location getLocation() {
        return location;
    }

    public AstroCalculator.SunInfo getSunInfo() {
        return sunInfo;
    }

    public AstroCalculator.MoonInfo getMoonInfo() {
        return moonInfo;
    }
}
