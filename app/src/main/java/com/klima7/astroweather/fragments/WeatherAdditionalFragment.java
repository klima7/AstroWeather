package com.klima7.astroweather.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.klima7.astroweather.AppData;
import com.klima7.astroweather.R;

public class WeatherAdditionalFragment extends Fragment {

    private AppData data;
    private TextView windChillView, windDirectionView, windSpeedView, humidityView, visibilityView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_additional, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        data = new ViewModelProvider(requireActivity()).get(AppData.class);

        windChillView = getView().findViewById(R.id.wind_chill_view);
        windDirectionView = getView().findViewById(R.id.wind_direction_view);
        windSpeedView = getView().findViewById(R.id.wind_speed_view);
        humidityView = getView().findViewById(R.id.humidity_view);
        visibilityView = getView().findViewById(R.id.visibility_view);

        updateLocation();
        data.location.observe(requireActivity(), newLocation -> updateLocation());
    }

    private void updateLocation() {
        if(data.location.getValue() != null) {

        }
        else {
            String placeholder = getResources().getString(R.string.placeholder);
            windChillView.setText(placeholder);
            windDirectionView.setText(placeholder);
            windSpeedView.setText(placeholder);
            humidityView.setText(placeholder);
            visibilityView.setText(placeholder);
        }
    }
}