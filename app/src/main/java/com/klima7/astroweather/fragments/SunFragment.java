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
import com.klima7.astroweather.Formatter;
import com.klima7.astroweather.R;

public class SunFragment extends Fragment {

    private TextView sunriseTimeView, sunriseAzimuthView, sunsetTimeView, sunsetAzimuthView, dawnTimeView, duskTimeView;
    private String sunriseTimeText, sunriseAzimuthText, sunsetTimeText, sunsetAzimuthText, dawnTimeText, duskTimeText;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle saved) {
        super.onViewCreated(view, saved);

        sunriseTimeView = getView().findViewById(R.id.sunrise_time);
        sunriseAzimuthView = getView().findViewById(R.id.sunrise_azimuth);
        sunsetTimeView = getView().findViewById(R.id.sunset_time);
        sunsetAzimuthView = getView().findViewById(R.id.sunset_azimuth);
        dawnTimeView = getView().findViewById(R.id.dawn_time);
        duskTimeView = getView().findViewById(R.id.dusk_time);

        if(saved != null) {
            sunriseTimeText = saved.getString("sunriseTimeText");
            sunriseAzimuthText = saved.getString("sunriseAzimuthText");
            sunsetTimeText = saved.getString("sunsetTimeText");
            sunsetAzimuthText = saved.getString("sunsetAzimuthText");
            dawnTimeText = saved.getString("dawnTimeText");
            duskTimeText = saved.getString("duskTimeText");
        }

        apply();
    }

    public void update(AstroCalculator.SunInfo info) {
        sunriseTimeText = Formatter.formatTime(info.getSunrise());
        sunriseAzimuthText = Formatter.formatAzimuth(info.getAzimuthRise());
        sunsetTimeText = Formatter.formatTime(info.getSunset());
        sunsetAzimuthText = Formatter.formatAzimuth(info.getAzimuthSet());
        dawnTimeText = Formatter.formatTime(info.getTwilightMorning());
        duskTimeText = Formatter.formatTime(info.getTwilightEvening());

        if(sunriseAzimuthView != null)
            apply();
    }

    private void apply() {
        sunriseTimeView.setText(sunriseTimeText);
        sunriseAzimuthView.setText(sunriseAzimuthText);
        sunsetTimeView.setText(sunsetTimeText);
        sunsetAzimuthView.setText(sunsetAzimuthText);
        dawnTimeView.setText(dawnTimeText);
        duskTimeView.setText(duskTimeText);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("sunriseTimeText", sunriseTimeText);
        outState.putString("sunriseAzimuthText", sunriseAzimuthText);
        outState.putString("sunsetTimeText", sunsetTimeText);
        outState.putString("sunsetAzimuthText", sunsetAzimuthText);
        outState.putString("dawnTimeText", dawnTimeText);
        outState.putString("duskTimeText", duskTimeText);
    }
}