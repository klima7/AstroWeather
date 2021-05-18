package com.klima7.astroweather.weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.klima7.astroweather.Unit;

import java.nio.ByteBuffer;
import java.util.List;

@Entity(primaryKeys = {"woeid", "unit"})
public class Weather {

    public int woeid;
    @NonNull public Unit unit;

    public int chill;
    public int direction;
    public double speed;

    public int humidity;
    public double visibility;
    public double pressure;
    public int rising;

    public String text;
    public int code;
    public int temperature;

    public String date;

    public String image;

    public List<Forecast> forecasts;

    public Bitmap decodeBase64() {
        byte[] decodedByte = Base64.decode(image, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
