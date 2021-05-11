package com.klima7.astroweather.weather;

public class CurrentWeather {

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

    public void setChill(int chill) {
        this.chill = chill;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setRising(int rising) {
        this.rising = rising;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
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
