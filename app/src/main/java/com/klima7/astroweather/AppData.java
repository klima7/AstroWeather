package com.klima7.astroweather;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.klima7.astroweather.weather.Location;
import com.klima7.astroweather.weather.Weather;

import java.time.LocalDateTime;
import java.util.GregorianCalendar;

public class AppData extends ViewModel {

    public MutableLiveData<AstroCalculator.SunInfo> sunInfo = new MutableLiveData<>();
    public MutableLiveData<AstroCalculator.MoonInfo> moonInfo = new MutableLiveData<>();
    public MutableLiveData<Weather> weather = new MutableLiveData<>();
    public MutableLiveData<Location> location = new MutableLiveData<>();

    public MutableLiveData<Integer> refreshPeriod = new MutableLiveData<>();
    public MutableLiveData<Long> lastRefresh = new MutableLiveData<>();

    public AppData() {
        location.setValue(null);
        weather.setValue(null);
        sunInfo.setValue(null);
        moonInfo.setValue(null);
        refreshPeriod.setValue(10);
        lastRefresh.setValue(0L);
    }

    public void update() {
        GregorianCalendar cal = new GregorianCalendar();
        int y = cal.get(GregorianCalendar.YEAR);
        int mo = cal.get(GregorianCalendar.MONTH);
        int d = cal.get(GregorianCalendar.DAY_OF_MONTH);
        int h = cal.get(GregorianCalendar.HOUR);
        int mi = cal.get(GregorianCalendar.MINUTE);
        int s = cal.get(GregorianCalendar.SECOND);
        int zoneOffset = cal.toZonedDateTime().getZone().getRules().getOffset(LocalDateTime.now()).getTotalSeconds()/3600;
        AstroDateTime time = new AstroDateTime(y, mo, d, h, mi, s, zoneOffset, true);

        AstroCalculator.Location astroLocation = new AstroCalculator.Location(location.getValue().getLatitude(), location.getValue().getLongitude());
        AstroCalculator calculator = new AstroCalculator(time, astroLocation);

        sunInfo.setValue(calculator.getSunInfo());
        moonInfo.setValue(calculator.getMoonInfo());
        lastRefresh.setValue(System.currentTimeMillis());
    }
}
