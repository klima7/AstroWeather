package com.klima7.astroweather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SunMoonFragment extends Fragment {

    private SunFragment sunFragment;
    private MoonFragment moonFragment;

    public SunMoonFragment() {
    }

    public static SunMoonFragment newInstance(SunFragment sunFragment, MoonFragment moonFragment) {
        SunMoonFragment fragment = new SunMoonFragment();
        fragment.setSunFragment(sunFragment);
        fragment.setMoonFragment(moonFragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.sun_container, sunFragment, "sun");
            transaction.add(R.id.moon_container, moonFragment, "moon");
            transaction.commit();
        }
    }

    public void setSunFragment(SunFragment sunFragment) {
        this.sunFragment = sunFragment;
    }

    public void setMoonFragment(MoonFragment moonFragment) {
        this.moonFragment = moonFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sun_moon, container, false);
    }
}