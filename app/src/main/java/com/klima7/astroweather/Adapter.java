package com.klima7.astroweather;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

class Adapter extends FragmentStateAdapter {

    private InfoFragment info;
    private SunFragment sun;
    private MoonFragment moon;

    public Adapter(FragmentActivity fa, InfoFragment info, SunFragment sun, MoonFragment moon) {
        super(fa);
        this.info = info;
        this.sun = sun;
        this.moon = moon;
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return info;
            case 1: return sun;
            case 2: return moon;
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}