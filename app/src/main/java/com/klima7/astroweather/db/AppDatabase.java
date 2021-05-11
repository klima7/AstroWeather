package com.klima7.astroweather.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.klima7.astroweather.weather.Location;

@Database(entities = {Location.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
}
