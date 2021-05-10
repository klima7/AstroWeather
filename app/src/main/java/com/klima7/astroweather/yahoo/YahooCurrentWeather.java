package com.klima7.astroweather.yahoo;

public class YahooCurrentWeather {
    private int chill;
    private int direction;
    private double speed;

    private int humidity;
    private double visibility;
    private double pressure;
    private int rising;

    private String sunrise;
    private String sunset;

    private String text;
    private int code;
    private int temperature;

    public int getChill() {
        return chill;
    }

    public int getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getVisibility() {
        return visibility;
    }

    public double getPressure() {
        return pressure;
    }

    public int getRising() {
        return rising;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getText() {
        return text;
    }

    public int getCode() {
        return code;
    }

    public int getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return "CurrentWeatherInfo{" +
                "chill=" + chill +
                ", direction=" + direction +
                ", speed=" + speed +
                ", humidity=" + humidity +
                ", visibility=" + visibility +
                ", pressure=" + pressure +
                ", rising=" + rising +
                ", sunrise='" + sunrise + '\'' +
                ", sunset='" + sunset + '\'' +
                ", text='" + text + '\'' +
                ", code=" + code +
                ", temperature=" + temperature +
                '}';
    }
}
