package com.klima7.astroweather;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.klima7.astroweather.fragments.AstroAndWeatherFragment;
import com.klima7.astroweather.fragments.CurrentAndAdditionalFragment;
import com.klima7.astroweather.fragments.SunAndMoonFragment;
import com.klima7.astroweather.fragments.WeatherForecastFragment;

class TabletPagerAdapter extends FragmentStateAdapter {

    public TabletPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new AstroAndWeatherFragment();
            case 1: return new WeatherForecastFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}