package com.klima7.astroweather;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Timer;
import java.util.TimerTask;

public class AstroActivity extends FragmentActivity {

    private InfoFragment infoFragment;
    private SunFragment sunFragment;
    private MoonFragment moonFragment;
    private SunMoonFragment sunMoonFragment;

    private AstroCalculator.SunInfo sunInfo;
    private AstroCalculator.MoonInfo moonInfo;

    private Timer timer;
    private TimerTask refreshTask;

    public AstroActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astro);

        Bundle extras = getIntent().getExtras();
        float longitude = extras.getFloat(MenuActivity.EXTRA_LONGITUDE, 0);
        float latitude = extras.getFloat(MenuActivity.EXTRA_LATITUDE, 0);
        int refresh = extras.getInt(MenuActivity.EXTRA_REFRESH, 0);

        // Calculate info
        if(savedInstanceState == null) {
            AstroDateTime time = new AstroDateTime();
            AstroCalculator.Location location = new AstroCalculator.Location(longitude, latitude);
            AstroCalculator calculator = new AstroCalculator(time, location);
            sunInfo = calculator.getSunInfo();
            moonInfo = calculator.getMoonInfo();
        }

        // Retrive info from saved state
        else {
            DataWrapper wrapper = (DataWrapper) savedInstanceState.getSerializable("data");
            sunInfo = wrapper.getSunInfo();
            moonInfo = wrapper.getMoonInfo();
        }

        // Create fragments
        infoFragment = InfoFragment.newInstance(latitude, longitude);
        sunFragment = SunFragment.newInstance();
        moonFragment = MoonFragment.newInstance();
        sunMoonFragment = SunMoonFragment.newInstance(sunFragment, moonFragment);

        // Set info in fragments
        sunFragment.setInfo(sunInfo);
        moonFragment.setInfo(moonInfo);

        // Get configuration
        int orientation = getResources().getConfiguration().orientation;
        int size = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        boolean tablet = size == Configuration.SCREENLAYOUT_SIZE_LARGE || size == Configuration.SCREENLAYOUT_SIZE_XLARGE;

        // Tablet
        if (savedInstanceState == null && tablet) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.info_container, infoFragment, "infoFragment");
            transaction.add(R.id.sun_container, sunFragment, "sunFragment");
            transaction.add(R.id.moon_container, moonFragment, "moonFragment");
            transaction.commit();
        }

        // Mobile
        if(!tablet) {
            ViewPager2 pager = findViewById(R.id.pager);
            FragmentStateAdapter adapter;
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
                adapter = new PortraitPagerAdapter(this);
            else adapter = new LandscapePagerAdapter(this);
            pager.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            pager.invalidate();
        }

//        timer = new Timer();
//        refreshTask = new RefreshTask();
//        timer.scheduleAtFixedRate(refreshTask, 5000, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        refreshTask.cancel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        DataWrapper wrapper = new DataWrapper(null, sunInfo, null);
        outState.putSerializable("data", wrapper);
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            Log.i("Hello", "refresh");
            AstroDateTime time = new AstroDateTime();
            AstroCalculator.Location location = new AstroCalculator.Location(51.759445, 19.457216);
            AstroCalculator calculator = new AstroCalculator(time, location);

            AstroCalculator.SunInfo sunInfo = calculator.getSunInfo();
            sunFragment.setInfo(sunInfo);
            runOnUiThread(() -> sunFragment.applyInfo());

            AstroCalculator.MoonInfo moonInfo = calculator.getMoonInfo();
            moonFragment.setInfo(moonInfo);
            runOnUiThread(() -> moonFragment.applyInfo());
        }
    }

    private class PortraitPagerAdapter extends FragmentStateAdapter {
        public PortraitPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0: return infoFragment;
                case 1: return sunFragment;
                case 2: return moonFragment;
                default: return null;
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }


    }

    private class LandscapePagerAdapter extends FragmentStateAdapter {
        public LandscapePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0: return infoFragment;
                case 1: return sunMoonFragment;
                default: return null;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}