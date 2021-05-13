package com.klima7.astroweather;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.klima7.astroweather.db.AppDatabase;
import com.klima7.astroweather.db.DatabaseUtil;
import com.klima7.astroweather.weather.CurrentWeather;
import com.klima7.astroweather.weather.Forecast;
import com.klima7.astroweather.weather.Location;
import com.klima7.astroweather.weather.Entry;
import com.klima7.astroweather.weather.YahooWeatherRequest;

import java.time.LocalDateTime;
import java.util.GregorianCalendar;
import java.util.List;

public class AppData extends AndroidViewModel {

    public MutableLiveData<AstroCalculator.SunInfo> sunInfo = new MutableLiveData<>();
    public MutableLiveData<AstroCalculator.MoonInfo> moonInfo = new MutableLiveData<>();
    public MutableLiveData<Location> location = new MutableLiveData<>();
    public MutableLiveData<CurrentWeather> currentWeather = new MutableLiveData<>();
    public MutableLiveData<List<Forecast>> forecasts = new MutableLiveData<>();

    public MutableLiveData<String> unit = new MutableLiveData<>();

    public MutableLiveData<Integer> refreshPeriod = new MutableLiveData<>();
    public MutableLiveData<Long> lastRefresh = new MutableLiveData<>();

    public AppData(Application application) {
        super(application);
        location.setValue(null);
        sunInfo.setValue(null);
        moonInfo.setValue(null);
        currentWeather.setValue(null);
        forecasts.setValue(null);
        refreshPeriod.setValue(10);
        lastRefresh.setValue(0L);
    }
}
