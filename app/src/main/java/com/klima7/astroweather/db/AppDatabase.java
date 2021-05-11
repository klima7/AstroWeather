package com.klima7.astroweather.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.klima7.astroweather.weather.CurrentWeather;
import com.klima7.astroweather.weather.Forecast;
import com.klima7.astroweather.weather.Location;
import com.klima7.astroweather.weather.Weather;

@Database(entities = {Weather.class}, version = 1, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract WeatherDao weatherDao();
}
