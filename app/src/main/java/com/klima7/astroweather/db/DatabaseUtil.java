package com.klima7.astroweather.db;

import android.content.Context;

import androidx.room.Room;

public class DatabaseUtil {

    private static AppDatabase db;

    public static AppDatabase getDatabase(Context context) {
        if(db == null) {
            db = Room.databaseBuilder(context, AppDatabase.class, "database").build();
        }
        return db;
    }
}
