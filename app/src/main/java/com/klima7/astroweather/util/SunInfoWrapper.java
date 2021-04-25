package com.klima7.astroweather.util;

import com.astrocalculator.AstroCalculator;

import java.io.Serializable;

public class SunInfoWrapper implements Serializable {

    private AstroCalculator.SunInfo sunInfo;

    public SunInfoWrapper(AstroCalculator.SunInfo sunInfo) {
        this.sunInfo = sunInfo;
    }

    public AstroCalculator.SunInfo get() {
        return sunInfo;
    }
}
