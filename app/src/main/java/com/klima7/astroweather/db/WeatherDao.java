package com.klima7.astroweather.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.klima7.astroweather.Unit;
import com.klima7.astroweather.weather.Location;
import com.klima7.astroweather.weather.Weather;

import java.util.List;

@Dao
public interface WeatherDao {

    @Query("SELECT * FROM Weather")
    List<Weather> getAll();

    @Query("SELECT * FROM Weather WHERE woeid=:woeid AND unit=:unit")
    Weather getSingle(int woeid, Unit unit);

    @Insert
    void insertAll(Weather... weathers);

    @Delete
    void delete(Weather entry);

    @Query("DELETE FROM Weather WHERE woeid = :woeid")
    void delete(int woeid);

    @Update
    int update(Weather entry);
}
