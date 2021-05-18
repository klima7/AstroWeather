package com.klima7.astroweather.weather;

public class Forecast {

    public String day;
    public String date;
    public int low;
    public int high;
    public String text;
    public int code;

    @Override
    public String toString() {
        return "ForecastInfo{" +
                "day='" + day + '\'' +
                ", date=" + date +
                ", low=" + low +
                ", high=" + high +
                ", text='" + text + '\'' +
                ", code=" + code +
                '}';
    }
}
