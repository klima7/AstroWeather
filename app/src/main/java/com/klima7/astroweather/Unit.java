package com.klima7.astroweather;

public enum Unit {
    METRIC("c"), IMPERIAL("f");

    Unit(String yahooId) {
        this.yahooId = yahooId;
    }

    public String getYahooId() {
        return yahooId;
    }

    private String yahooId;
}
