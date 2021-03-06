package com.klima7.astroweather.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.klima7.astroweather.AppData;
import com.klima7.astroweather.R;
import com.klima7.astroweather.Unit;
import com.klima7.astroweather.weather.Weather;

public class WeatherGeneralFragment extends Fragment {

    private AppData data;
    private TextView temperatureView, pressureView, conditionView, measurementView;
    private ImageView imageView;

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
        conditionView = getView().findViewById(R.id.condition_view);
        measurementView = getView().findViewById(R.id.measurement_view);
        imageView = getView().findViewById(R.id.weather_image);

        update();
        data.weather.observe(requireActivity(), newWeather -> update());
    }

    private void update() {
        Weather weather = data.weather.getValue();

        if(weather != null) {
            measurementView.setText(weather.date.substring(0, weather.date.length()-7));
            temperatureView.setText(String.valueOf(weather.temperature) + weather.unit.getTemperatureUnit());
            pressureView.setText(String.valueOf(weather.pressure) + weather.unit.getPressureUnit());
            conditionView.setText(String.valueOf(weather.text));
            imageView.setImageBitmap(weather.decodeBase64());
        }
        else {
            String placeholder = getResources().getString(R.string.placeholder);
            measurementView.setText(placeholder);
            temperatureView.setText(placeholder);
            pressureView.setText(placeholder);
            conditionView.setText(placeholder);
            imageView.setImageResource(R.drawable.question_mark);
        }
    }
}