package com.klima7.astroweather.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.klima7.astroweather.weather.Location;

import java.util.List;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM Location")
    List<Location> getAll();

    @Insert
    void insertAll(Location... entries);

    @Delete
    void delete(Location entry);

    @Update
    int update(Location entry);
}
