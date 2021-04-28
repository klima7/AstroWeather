package com.klima7.astroweather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.astrocalculator.AstroCalculator;
import com.klima7.astroweather.R;
import com.klima7.astroweather.util.Formatter;
import com.klima7.astroweather.util.SunInfoWrapper;

public class SunFragment extends Fragment {

    private static AstroCalculator.SunInfo info;
    private TextView sunriseTimeView, sunriseAzimuthView, sunsetTimeView, sunsetAzimuthView, dawnTimeView, duskTimeView;

    public static SunFragment newInstance(AstroCalculator.SunInfo sunInfo) {
        SunFragment fragment = new SunFragment();
        fragment.update(sunInfo);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sun, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sunriseTimeView = getView().findViewById(R.id.sunrise_time);
        sunriseAzimuthView = getView().findViewById(R.id.sunrise_azimuth);

        sunsetTimeView = getView().findViewById(R.id.sunset_time);
        sunsetAzimuthView = getView().findViewById(R.id.sunset_azimuth);

        dawnTimeView = getView().findViewById(R.id.dawn_time);
        duskTimeView = getView().findViewById(R.id.dusk_time);

        applyInfo();
    }

    public void update(AstroCalculator.SunInfo info) {
        this.info = info;
        applyInfo();
    }

    private void applyInfo() {
        if(info == null || sunriseTimeView == null)
            return;

        sunriseTimeView.setText(Formatter.formatTime(info.getSunrise()));
        sunriseAzimuthView.setText(Formatter.formatAzimuth(info.getAzimuthRise()));

        sunsetTimeView.setText(Formatter.formatTime(info.getSunset()));
        sunsetAzimuthView.setText(Formatter.formatAzimuth(info.getAzimuthSet()));

        dawnTimeView.setText(Formatter.formatTime(info.getTwilightMorning()));
        duskTimeView.setText(Formatter.formatTime(info.getTwilightEvening()));
    }
}