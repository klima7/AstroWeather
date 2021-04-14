package com.klima7.astroweather;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

public class Formatter {

    public static String formatTime(AstroDateTime time) {
        return String.format("%d:%d:%d", time.getHour(), time.getMinute(), time.getSecond());
    }

    public static String formatAzimuth(double azimuth) {
        return String.format("%.1f", azimuth);
    }

    public static String formatDate(AstroDateTime time) {
        return String.format("%d.%d.%d", time.getDay(), time.getMonth(), time.getYear());
    }
}
