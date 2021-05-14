package com.klima7.astroweather.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.klima7.astroweather.AppData;
import com.klima7.astroweather.ForecastAdapter;
import com.klima7.astroweather.R;
import com.klima7.astroweather.weather.Forecast;
import com.klima7.astroweather.weather.Weather;

import java.util.ArrayList;
import java.util.List;

public class WeatherForecastFragment extends Fragment {

    private AppData data;
    private ForecastAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_forecast, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        data = new ViewModelProvider(requireActivity()).get(AppData.class);

        RecyclerView recycler = view.findViewById(R.id.forecast_recycler);
        adapter = new ForecastAdapter(view.findViewById(R.id.no_forecast_message));
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(layoutManager);

        update(data.weather.getValue());
        data.weather.observe(requireActivity(), newWeather -> update(newWeather));
    }

    private void update(Weather weather) {
        if(weather != null) {
            adapter.setForecasts(weather.forecasts);
        }
        else {
            adapter.setForecasts(new ArrayList<>());
        }
    }
}