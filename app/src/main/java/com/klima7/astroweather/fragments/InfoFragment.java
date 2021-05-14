package com.klima7.astroweather.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.klima7.astroweather.AppData;
import com.klima7.astroweather.R;
import com.klima7.astroweather.weather.Location;

public class InfoFragment extends Fragment {

    private InfoInterface infoInterface;
    private AppData data;
    private TextView cityView, latitudeView, longitudeView;
    private ImageView disconnectedIconView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        infoInterface = (InfoInterface)context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        data = new ViewModelProvider(requireActivity()).get(AppData.class);

        ImageButton locationButton = getView().findViewById(R.id.location_change_button);
        locationButton.setOnClickListener(v -> infoInterface.locationChangeClicked());

        ImageButton settingsButton = getView().findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(v -> infoInterface.settingsClicked());

        latitudeView = getView().findViewById(R.id.info_latitude);
        longitudeView = getView().findViewById(R.id.info_longitude);
        cityView = getView().findViewById(R.id.info_city);
        disconnectedIconView = getView().findViewById(R.id.disconnected_icon);

        updateLocation(data.location.getValue());
        updateConnected(data.connected.getValue());

        data.location.observe(requireActivity(), this::updateLocation);
        data.connected.observe(requireActivity(), this::updateConnected);
    }

    private void updateLocation(Location location) {
        if(location != null) {
            latitudeView.setText(String.valueOf(location.latitude));
            longitudeView.setText(String.valueOf(location.longitude));
            cityView.setText(data.location.getValue().city + " (" + location.region.trim() + ")");
        }
        else {
            String placeholder = getResources().getString(R.string.placeholder);
            latitudeView.setText(placeholder);
            longitudeView.setText(placeholder);
            cityView.setText(placeholder);
        }
    }

    private void updateConnected(boolean connected) {
        disconnectedIconView.setVisibility(connected ? View.INVISIBLE : View.VISIBLE);
    }

    public interface InfoInterface {
        void locationChangeClicked();
        void settingsClicked();
    }
}