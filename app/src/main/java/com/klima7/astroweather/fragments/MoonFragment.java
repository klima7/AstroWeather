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

public class MoonFragment extends Fragment {

    private AppData data;
    private TextView moonriseTimeView, moonsetTimeView, interlunarTimeView, fullMoonTimeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_moon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saved) {
        super.onViewCreated(view, saved);

        data = new ViewModelProvider(requireActivity()).get(AppData.class);

        moonriseTimeView = getView().findViewById(R.id.moonrise_time);
        moonsetTimeView = getView().findViewById(R.id.moonset_time);
        interlunarTimeView = getView().findViewById(R.id.internular_time);
        fullMoonTimeView = getView().findViewById(R.id.fullmoon_time);

        updateMoonInfo();
        data.moonInfo.observe(requireActivity(), newInfo -> updateMoonInfo());
    }

    private void updateMoonInfo() {
        AstroCalculator.MoonInfo info = data.moonInfo.getValue();

        if(info != null) {
            moonriseTimeView.setText(Formatter.formatTime(info.getMoonrise()));
            moonsetTimeView.setText(Formatter.formatTime(info.getMoonset()));
            interlunarTimeView.setText(Formatter.formatDate(info.getNextNewMoon()));
            fullMoonTimeView.setText(Formatter.formatDate(info.getNextFullMoon()));
        }
        else {
            String placeholder = getResources().getString(R.string.placeholder);
            moonriseTimeView.setText(placeholder);
            moonsetTimeView.setText(placeholder);
            interlunarTimeView.setText(placeholder);
            fullMoonTimeView.setText(placeholder);
        }
    }
}