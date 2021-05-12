package com.klima7.astroweather;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.klima7.astroweather.weather.CurrentWeather;
import com.klima7.astroweather.weather.Forecast;
import com.klima7.astroweather.weather.Location;
import com.klima7.astroweather.weather.Entry;

import java.time.LocalDateTime;
import java.util.GregorianCalendar;
import java.util.List;

public class AppData extends ViewModel {

    public MutableLiveData<AstroCalculator.SunInfo> sunInfo = new MutableLiveData<>();
    public MutableLiveData<AstroCalculator.MoonInfo> moonInfo = new MutableLiveData<>();
    public MutableLiveData<Location> location = new MutableLiveData<>();
    public MutableLiveData<CurrentWeather> currentWeather = new MutableLiveData<>();
    public MutableLiveData<List<Forecast>> forecasts = new MutableLiveData<>();

    public MutableLiveData<Integer> refreshPeriod = new MutableLiveData<>();
    public MutableLiveData<Long> lastRefresh = new MutableLiveData<>();

    public AppData() {
        location.setValue(null);
        sunInfo.setValue(null);
        moonInfo.setValue(null);
        currentWeather.setValue(null);
        forecasts.setValue(null);
        refreshPeriod.setValue(10);
        lastRefresh.setValue(0L);
    }

    public void update() {
        updateAstro();
        updateWeather();
        lastRefresh.setValue(System.currentTimeMillis());
    }

    private void updateAstro() {
        if(location.getValue() == null) {
            sunInfo.setValue(null);
            moonInfo.setValue(null);
            return;
        }

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
    }

    private void updateWeather() {

    }
}
