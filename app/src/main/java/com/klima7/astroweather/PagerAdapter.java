package com.klima7.astroweather;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.klima7.astroweather.fragments.CurrentAndAdditionalFragment;
import com.klima7.astroweather.fragments.SunAndMoonFragment;
import com.klima7.astroweather.fragments.WeatherAdditionalFragment;
import com.klima7.astroweather.fragments.WeatherForecastFragment;

class PagerAdapter extends FragmentStateAdapter {

    public PagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new SunAndMoonFragment();
            case 1: return new CurrentAndAdditionalFragment();
            case 2: return new WeatherForecastFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}