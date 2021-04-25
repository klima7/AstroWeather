package com.klima7.astroweather;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.klima7.astroweather.fragments.InfoFragment;
import com.klima7.astroweather.fragments.MoonFragment;
import com.klima7.astroweather.fragments.SunFragment;
import com.klima7.astroweather.util.MoonInfoWrapper;
import com.klima7.astroweather.util.SunInfoWrapper;

import java.util.Timer;
import java.util.TimerTask;

public class AstroActivity extends FragmentActivity implements InfoFragment.InfoInterface {

    private InfoFragment infoFragment;
    private SunFragment sunFragment;
    private MoonFragment moonFragment;

    private float longitude, latitude;
    private AstroCalculator.SunInfo sunInfo;
    private AstroCalculator.MoonInfo moonInfo;

    private Timer timer;
    private TimerTask refreshTask;
    private long lastRefreshTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astro);

        // Get arguments
        Bundle extras = getIntent().getExtras();
        longitude = extras.getFloat(MenuActivity.EXTRA_LONGITUDE, 0);
        latitude = extras.getFloat(MenuActivity.EXTRA_LATITUDE, 0);
        int refreshPeriod = extras.getInt(MenuActivity.EXTRA_REFRESH, 0);

        // Retrieve saved state
        if(savedInstanceState != null) {
            SunInfoWrapper sunWrapper = (SunInfoWrapper) savedInstanceState.getSerializable("sunInfo");
            MoonInfoWrapper moonWrapper = (MoonInfoWrapper) savedInstanceState.getSerializable("moonInfo");
            sunInfo = sunWrapper.get();
            moonInfo = moonWrapper.get();
            lastRefreshTime = savedInstanceState.getLong("lastRefreshTime");
        }

        // Retrieve fragments if they exists
        FragmentManager fm = getSupportFragmentManager();
        sunFragment = (SunFragment)fm.findFragmentByTag("f0");
        moonFragment = (MoonFragment)fm.findFragmentByTag("f1");

        // Create new fragments if they not exists
        if(infoFragment == null) infoFragment = InfoFragment.newInstance(latitude, longitude);
        if(sunFragment == null) sunFragment = SunFragment.newInstance(sunInfo);
        if(moonFragment == null) moonFragment = MoonFragment.newInstance(moonInfo);

        // Refresh data if first launch
        if(savedInstanceState == null)
            refreshAstroInfo();

        // Get configuration
        int orientation = getResources().getConfiguration().orientation;
        int size = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        boolean tablet = size == Configuration.SCREENLAYOUT_SIZE_LARGE || size == Configuration.SCREENLAYOUT_SIZE_XLARGE;

        // Tablet
        if (tablet) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.info_container, infoFragment, "infoFragment");
            transaction.replace(R.id.sun_container, sunFragment, "sunFragment");
            transaction.replace(R.id.moon_container, moonFragment, "moonFragment");
            transaction.commit();
        }

        // Mobile
        else {
            ViewPager2 pager = findViewById(R.id.pager);
            Log.i("Hello", "" + (pager.getAdapter()==null));
            FragmentStateAdapter adapter = new Adapter(this, sunFragment, moonFragment);
            pager.setAdapter(adapter);

            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.info_holder_mobile, infoFragment);
            ft.commit();

            if(orientation == Configuration.ORIENTATION_PORTRAIT)
                pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            else
                pager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        }

        // Schedule refresh
        timer = new Timer();
        refreshTask = new RefreshTask();
        long nextRefreshTime = lastRefreshTime + refreshPeriod*1000;
        long nextRefreshDelay = Math.max(nextRefreshTime - System.currentTimeMillis(), 0);
        timer.scheduleAtFixedRate(refreshTask, nextRefreshDelay, refreshPeriod*1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refreshTask.cancel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("sunInfo", new SunInfoWrapper(sunInfo));
        outState.putSerializable("moonInfo", new MoonInfoWrapper(moonInfo));
        outState.putLong("lastRefreshTime", lastRefreshTime);
    }

    @Override
    public void settingsClicked() {
        Log.i("Hello", "Clicked");
    }

    private void refreshAstroInfo() {
        AstroDateTime time = new AstroDateTime();
        AstroCalculator.Location location = new AstroCalculator.Location(latitude, longitude);
        AstroCalculator calculator = new AstroCalculator(time, location);

        sunInfo = calculator.getSunInfo();
        sunFragment.update(sunInfo);

        moonInfo = calculator.getMoonInfo();
        moonFragment.update(moonInfo);

        lastRefreshTime = System.currentTimeMillis();
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(() -> Toast.makeText(AstroActivity.this, "Odświeżenie", Toast.LENGTH_LONG).show());
            refreshAstroInfo();
        }
    }
}