package com.klima7.astroweather.weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Forecast {

    public String day;
    public String date;
    public int low;
    public int high;
    public String text;
    public int code;
    public String image;

    public Bitmap decodeBase64() {
        byte[] decodedByte = Base64.decode(image, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
