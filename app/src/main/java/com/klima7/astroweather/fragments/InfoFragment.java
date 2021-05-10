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

public class InfoFragment extends Fragment {

    private InfoInterface infoInterface;
    private AppData data;
    private TextView cityView, latitudeView, longitudeView;

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

        ImageButton button = getView().findViewById(R.id.settingsButton);
        button.setOnClickListener(v -> infoInterface.settingsClicked());

        latitudeView = getView().findViewById(R.id.info_latitude);
        longitudeView = getView().findViewById(R.id.info_longitude);
        cityView = getView().findViewById(R.id.info_city);

        updateLocation();
        data.location.observe(requireActivity(), newLocation -> updateLocation());
    }

    private void updateLocation() {
        if(data.location.getValue() != null) {
            latitudeView.setText(String.valueOf(data.location.getValue().getLatitude()));
            longitudeView.setText(String.valueOf(data.location.getValue().getLongitude()));
            cityView.setText(String.valueOf(data.location.getValue().getCity()));
        }
        else {
            String placeholder = getResources().getString(R.string.placeholder);
            latitudeView.setText(placeholder);
            longitudeView.setText(placeholder);
            cityView.setText(placeholder);
        }
    }

    public interface InfoInterface {
        void settingsClicked();
    }
}