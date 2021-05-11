package com.klima7.astroweather.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.klima7.astroweather.weather.CurrentWeather;
import com.klima7.astroweather.weather.Forecast;
import com.klima7.astroweather.weather.Location;
import com.klima7.astroweather.weather.Weather;

@Database(entities = {Location.class, Weather.class, CurrentWeather.class, Forecast.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
    public abstract WeatherDao weatherDao();
}
