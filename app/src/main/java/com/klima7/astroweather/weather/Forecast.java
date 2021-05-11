package com.klima7.astroweather.weather;

public class Forecast {

    private String day;
    private long date;
    private int low;
    private int high;
    private String text;
    private int code;

    public String getDay() {
        return day;
    }

    public long getDate() {
        return date;
    }

    public int getLow() {
        return low;
    }

    public int getHigh() {
        return high;
    }

    public String getText() {
        return text;
    }

    public int getCode() {
        return code;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCode(int code) {
        this.code = code;
    }

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
