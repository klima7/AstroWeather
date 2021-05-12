package com.klima7.astroweather.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.klima7.astroweather.AppData;
import com.klima7.astroweather.R;

public class WeatherGeneralFragment extends Fragment {

    private AppData data;
    private TextView temperatureView, pressureView, descriptionView, conditionView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_general, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        data = new ViewModelProvider(requireActivity()).get(AppData.class);

        temperatureView = getView().findViewById(R.id.temperature_view);
        pressureView = getView().findViewById(R.id.pressure_view);
        descriptionView = getView().findViewById(R.id.description_view);
        conditionView = getView().findViewById(R.id.condition_view);

        updateLocation();
        data.location.observe(requireActivity(), newLocation -> updateLocation());
    }

    private void updateLocation() {
        if(data.location.getValue() != null) {

        }
        else {
            String placeholder = getResources().getString(R.string.placeholder);
            temperatureView.setText(placeholder);
            pressureView.setText(placeholder);
            descriptionView.setText(placeholder);
            conditionView.setText(placeholder);
        }
    }
}