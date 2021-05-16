package com.klima7.astroweather;

public enum Unit {
    METRIC("c", "C", "km", "degree", "km/h", "%", "millibar"),
    IMPERIAL("f", "F", "mi.", "degree", "mi./h", "%", "Inch Hg");

    Unit(String code, String temperature, String distance, String windDirection, String windSpeed, String humidity, String pressure) {
        this.code = code;
        this.temperature = temperature;
        this.distance = distance;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    private final String code;
    private final String temperature;
    private final String distance;
    private final String windDirection;
    private final String windSpeed;
    private final String humidity;
    private final String pressure;

    public static Unit fromCode(String code) {
        for(Unit unit : Unit.values()) {
            if(unit.code.equalsIgnoreCase(code))
                return unit;
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getTemperatureUnit() {
        return temperature;
    }

    public String getDistanceUnit() {
        return distance;
    }

    public String getWindDirectionUnit() {
        return windDirection;
    }

    public String getWindSpeedUnit() {
        return windSpeed;
    }

    public String getHumidityUnit() {
        return humidity;
    }

    public String getPressureUnit() {
        return pressure;
    }
}
