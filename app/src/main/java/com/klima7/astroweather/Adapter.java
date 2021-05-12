package com.klima7.astroweather;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.klima7.astroweather.fragments.MoonFragment;
import com.klima7.astroweather.fragments.SunFragment;
import com.klima7.astroweather.fragments.WeatherAdditionalFragment;
import com.klima7.astroweather.fragments.WeatherGeneralFragment;

class Adapter extends FragmentStateAdapter {

    public Adapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new SunFragment();
            case 1: return new MoonFragment();
            case 2: return new WeatherGeneralFragment();
            case 3: return new WeatherAdditionalFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}