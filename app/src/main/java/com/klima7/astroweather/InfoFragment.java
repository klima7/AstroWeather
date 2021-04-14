package com.klima7.astroweather;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoFragment extends Fragment {

    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";

    private float latitude;
    private float longitude;

    public InfoFragment() {
    }

    public static InfoFragment newInstance(float latitude, float longitude) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putFloat(ARG_LATITUDE, latitude);
        args.putFloat(ARG_LONGITUDE, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latitude = getArguments().getFloat(ARG_LATITUDE);
            longitude = getArguments().getFloat(ARG_LONGITUDE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView latitudeView = getView().findViewById(R.id.info_latitude);
        TextView longitudeView = getView().findViewById(R.id.info_longitude);

        latitudeView.setText(String.valueOf(latitude));
        longitudeView.setText(String.valueOf(longitude));
    }
}