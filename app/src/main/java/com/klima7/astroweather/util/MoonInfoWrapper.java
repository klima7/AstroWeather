package com.klima7.astroweather.util;

import com.astrocalculator.AstroCalculator;

import java.io.Serializable;

public class MoonInfoWrapper implements Serializable {

    private AstroCalculator.MoonInfo moonInfo;

    public MoonInfoWrapper(AstroCalculator.MoonInfo moonInfo) {
        this.moonInfo = moonInfo;
    }

    public AstroCalculator.MoonInfo get() {
        return moonInfo;
    }
}
