package com.klima7.astroweather.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.klima7.astroweather.Unit;
import com.klima7.astroweather.weather.Forecast;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {

    @TypeConverter
    public String fromForecastList(List<Forecast> Forecast) {
        if (Forecast == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Forecast>>() {}.getType();
        String json = gson.toJson(Forecast, type);
        return json;
    }

    @TypeConverter
    public List<Forecast> toForecastList(String ForecastString) {
        if (ForecastString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Forecast>>() {}.getType();
        List<Forecast> ForecastList = gson.fromJson(ForecastString, type);
        return ForecastList;
    }

    @TypeConverter
    public String fromUnit(Unit unit) {
        return unit.getCode();
    }

    @TypeConverter
    public Unit toUnit(String code) {
        return Unit.fromCode(code);
    }
}