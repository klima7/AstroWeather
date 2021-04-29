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

import com.klima7.astroweather.AstroData;
import com.klima7.astroweather.R;

public class InfoFragment extends Fragment {

    private InfoInterface infoInterface;

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

        AstroData data = new ViewModelProvider(requireActivity()).get(AstroData.class);

        ImageButton button = getView().findViewById(R.id.settingsButton);
        button.setOnClickListener(v -> infoInterface.settingsClicked());

        TextView latitudeView = getView().findViewById(R.id.info_latitude);
        TextView longitudeView = getView().findViewById(R.id.info_longitude);

        latitudeView.setText(String.valueOf(data.latitude.getValue()));
        longitudeView.setText(String.valueOf(data.longitude.getValue()));

        data.longitude.observe(requireActivity(), longitude -> longitudeView.setText(String.valueOf(longitude)));
        data.latitude.observe(requireActivity(), latitude -> latitudeView.setText(String.valueOf(latitude)));
    }

    public interface InfoInterface {
        void settingsClicked();
    }
}