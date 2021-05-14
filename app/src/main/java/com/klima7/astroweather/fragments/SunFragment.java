package com.klima7.astroweather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.astrocalculator.AstroCalculator;
import com.klima7.astroweather.AppData;
import com.klima7.astroweather.Formatter;
import com.klima7.astroweather.R;

public class SunFragment extends Fragment {

    private AppData data;
    private TextView sunriseTimeView, sunriseAzimuthView, sunsetTimeView, sunsetAzimuthView, dawnTimeView, duskTimeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sun, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saved) {
        super.onViewCreated(view, saved);

        data = new ViewModelProvider(requireActivity()).get(AppData.class);

        sunriseTimeView = getView().findViewById(R.id.sunrise_time);
        sunriseAzimuthView = getView().findViewById(R.id.sunrise_azimuth);
        sunsetTimeView = getView().findViewById(R.id.sunset_time);
        sunsetAzimuthView = getView().findViewById(R.id.sunset_azimuth);
        dawnTimeView = getView().findViewById(R.id.dawn_time);
        duskTimeView = getView().findViewById(R.id.dusk_time);

        updateSunInfo();
        data.sunInfo.observe(requireActivity(), newInfo -> updateSunInfo());
    }

    private void updateSunInfo() {
        AstroCalculator.SunInfo info = data.sunInfo.getValue();

        if(info != null) {
            sunriseTimeView.setText(Formatter.formatTime(info.getSunrise()));
            sunriseAzimuthView.setText(Formatter.formatAzimuth(info.getAzimuthRise()));
            sunsetTimeView.setText(Formatter.formatTime(info.getSunset()));
            sunsetAzimuthView.setText(Formatter.formatAzimuth(info.getAzimuthSet()));
            dawnTimeView.setText(Formatter.formatTime(info.getTwilightMorning()));
            duskTimeView.setText(Formatter.formatTime(info.getTwilightEvening()));
        }
        else {
            String placeholder = getResources().getString(R.string.placeholder);
            sunriseTimeView.setText(placeholder);
            sunriseAzimuthView.setText(placeholder);
            sunsetTimeView.setText(placeholder);
            sunsetAzimuthView.setText(placeholder);
            dawnTimeView.setText(placeholder);
            duskTimeView.setText(placeholder);
        }
    }
}