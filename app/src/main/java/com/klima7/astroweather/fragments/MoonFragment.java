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
import com.klima7.astroweather.util.MoonInfoWrapper;

public class MoonFragment extends Fragment {

    private AstroCalculator.MoonInfo info;
    private TextView moonriseTimeView, moonsetTimeView, interlunarTimeView, fullMoonTimeView;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        moonriseTimeView = getView().findViewById(R.id.moonrise_time);
        moonsetTimeView = getView().findViewById(R.id.moonset_time);

        interlunarTimeView = getView().findViewById(R.id.internular_time);
        fullMoonTimeView = getView().findViewById(R.id.fullmoon_time);

        applyInfo();
    }

    public void update(AstroCalculator.MoonInfo info) {
        this.info = info;
        applyInfo();
    }

    private void applyInfo() {
        if(info == null || moonriseTimeView == null)
            return;

        moonriseTimeView.setText(Formatter.formatTime(info.getMoonrise()));
        moonsetTimeView.setText(Formatter.formatTime(info.getMoonset()));

        interlunarTimeView.setText(Formatter.formatDate(info.getNextNewMoon()));
        fullMoonTimeView.setText(Formatter.formatDate(info.getNextFullMoon()));
    }
}