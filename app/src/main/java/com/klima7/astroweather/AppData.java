package com.klima7.astroweather;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.time.LocalDateTime;
import java.util.GregorianCalendar;

public class AppData extends ViewModel {

    public MutableLiveData<AstroCalculator.SunInfo> sunInfo = new MutableLiveData<>();
    public MutableLiveData<AstroCalculator.MoonInfo> moonInfo = new MutableLiveData<>();

    public MutableLiveData<Float> longitude = new MutableLiveData<>();
    public MutableLiveData<Float> latitude = new MutableLiveData<>();

    public MutableLiveData<Integer> refreshPeriod = new MutableLiveData<>();
    public MutableLiveData<Long> lastRefresh = new MutableLiveData<>();

    public AppData() {
        refreshPeriod.setValue(10);
        latitude.setValue(0f);
        longitude.setValue(0f);
        refresh();
    }

    public void refresh() {
        GregorianCalendar cal = new GregorianCalendar();
        int y = cal.get(GregorianCalendar.YEAR);
        int mo = cal.get(GregorianCalendar.MONTH);
        int d = cal.get(GregorianCalendar.DAY_OF_MONTH);
        int h = cal.get(GregorianCalendar.HOUR);
        int mi = cal.get(GregorianCalendar.MINUTE);
        int s = cal.get(GregorianCalendar.SECOND);
        int zoneOffset = cal.toZonedDateTime().getZone().getRules().getOffset(LocalDateTime.now()).getTotalSeconds()/3600;
        AstroDateTime time = new AstroDateTime(y, mo, d, h, mi, s, zoneOffset, true);

        AstroCalculator.Location location = new AstroCalculator.Location(latitude.getValue(), longitude.getValue());
        AstroCalculator calculator = new AstroCalculator(time, location);

        sunInfo.setValue(calculator.getSunInfo());
        moonInfo.setValue(calculator.getMoonInfo());
        lastRefresh.setValue(System.currentTimeMillis());
    }
}
