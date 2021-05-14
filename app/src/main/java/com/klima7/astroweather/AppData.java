package com.klima7.astroweather;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.astrocalculator.AstroCalculator;
import com.klima7.astroweather.weather.Location;
import com.klima7.astroweather.weather.Weather;

public class AppData extends AndroidViewModel {

    public MutableLiveData<Boolean> connected = new MutableLiveData<>();

    public MutableLiveData<Location> location = new MutableLiveData<>();
    public MutableLiveData<AstroCalculator.SunInfo> sunInfo = new MutableLiveData<>();
    public MutableLiveData<AstroCalculator.MoonInfo> moonInfo = new MutableLiveData<>();
    public MutableLiveData<Weather> weather = new MutableLiveData<>();

    public MutableLiveData<String> unit = new MutableLiveData<>();

    public MutableLiveData<Integer> refreshPeriod = new MutableLiveData<>();
    public MutableLiveData<Long> lastRefresh = new MutableLiveData<>();

    public AppData(Application application) {
        super(application);

        connected.setValue(true);
        location.setValue(null);
        sunInfo.setValue(null);
        moonInfo.setValue(null);
        weather.setValue(null);
        lastRefresh.setValue(0L);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        unit.setValue(sharedPreferences.getString("unit", "c"));
        refreshPeriod.setValue(Integer.parseInt(sharedPreferences.getString("refresh", "10")));
    }
}
