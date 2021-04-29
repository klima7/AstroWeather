package com.klima7.astroweather;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

public class AstroData extends ViewModel {

    public MutableLiveData<AstroCalculator.SunInfo> sunInfo = new MutableLiveData<>();
    public MutableLiveData<AstroCalculator.MoonInfo> moonInfo = new MutableLiveData<>();

    public MutableLiveData<Float> longitude = new MutableLiveData<>();
    public MutableLiveData<Float> latitude = new MutableLiveData<>();

    public MutableLiveData<Integer> refreshPeriod = new MutableLiveData<>();
    public MutableLiveData<Long> lastRefresh = new MutableLiveData<>();

    public AstroData() {
        refreshPeriod.setValue(10);
        latitude.setValue(0f);
        longitude.setValue(0f);
        refresh();
    }

    public void refresh() {
        AstroDateTime time = new AstroDateTime();
        AstroCalculator.Location location = new AstroCalculator.Location(latitude.getValue(), longitude.getValue());
        AstroCalculator calculator = new AstroCalculator(time, location);

        sunInfo.setValue(calculator.getSunInfo());
        moonInfo.setValue(calculator.getMoonInfo());
        lastRefresh.setValue(System.currentTimeMillis());
    }
}
