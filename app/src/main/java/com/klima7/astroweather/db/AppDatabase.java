package com.klima7.astroweather.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.klima7.astroweather.weather.Entry;

@Database(entities = {Entry.class}, version = 1, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract EntryDao entryDao();
}
