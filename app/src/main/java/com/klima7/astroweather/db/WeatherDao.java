package com.klima7.astroweather.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.klima7.astroweather.weather.Location;
import com.klima7.astroweather.weather.Weather;

import java.util.List;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM Weather")
    List<Weather> getAll();

    @Insert
    void insertAll(Weather... weathers);

    @Delete
    void delete(Weather weather);

    @Update
    int updateWeather(Weather weather);
}
