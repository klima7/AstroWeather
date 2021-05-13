package com.klima7.astroweather.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.klima7.astroweather.weather.Entry;

import java.util.List;

@Dao
public interface EntryDao {
    @Query("SELECT * FROM Entry")
    List<Entry> getAll();

    @Insert
    void insertAll(Entry... entries);

    @Delete
    void delete(Entry entry);

    @Update
    int update(Entry entry);
}
