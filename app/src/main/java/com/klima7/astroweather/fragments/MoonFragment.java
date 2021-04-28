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

public class MoonFragment extends Fragment {

    private TextView moonriseTimeView, moonsetTimeView, interlunarTimeView, fullMoonTimeView;
    private String moonriseTimeText, moonsetTimeText, interlunarTimeText, fullMoonTimeText;

    public static MoonFragment newInstance(AstroCalculator.MoonInfo moonInfo) {
        MoonFragment fragment = new MoonFragment();
        fragment.update(moonInfo);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_moon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saved) {
        super.onViewCreated(view, saved);

        moonriseTimeView = getView().findViewById(R.id.moonrise_time);
        moonsetTimeView = getView().findViewById(R.id.moonset_time);
        interlunarTimeView = getView().findViewById(R.id.internular_time);
        fullMoonTimeView = getView().findViewById(R.id.fullmoon_time);

        if(saved != null) {
            moonriseTimeText = saved.getString("moonriseTimeText");
            moonsetTimeText = saved.getString("moonsetTimeText");
            interlunarTimeText = saved.getString("interlunarTimeText");
            fullMoonTimeText = saved.getString("fullMoonTimeText");
        }

        applyInfo();
    }

    public void update(AstroCalculator.MoonInfo info) {
        moonriseTimeText = Formatter.formatTime(info.getMoonrise());
        moonsetTimeText = Formatter.formatTime(info.getMoonset());
        interlunarTimeText = Formatter.formatDate(info.getNextNewMoon());
        fullMoonTimeText = Formatter.formatDate(info.getNextFullMoon());

        if(moonriseTimeView != null)
            applyInfo();
    }

    private void applyInfo() {
        moonriseTimeView.setText(moonriseTimeText);
        moonsetTimeView.setText(moonsetTimeText);
        interlunarTimeView.setText(interlunarTimeText);
        fullMoonTimeView.setText(fullMoonTimeText);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("fullMoonTimeText", fullMoonTimeText);
        outState.putString("moonsetTimeText", moonsetTimeText);
        outState.putString("interlunarTimeText", interlunarTimeText);
        outState.putString("fullMoonTimeText", fullMoonTimeText);
    }
}