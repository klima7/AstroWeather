package com.klima7.astroweather;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

    private float longitude, latitude;
    private AstroCalculator.SunInfo sunInfo;
    private AstroCalculator.MoonInfo moonInfo;

    private Timer timer;
    private TimerTask refreshTask;
    private long lastRefreshTime;

    public AstroActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astro);

        Bundle extras = getIntent().getExtras();
        longitude = extras.getFloat(MenuActivity.EXTRA_LONGITUDE, 0);
        latitude = extras.getFloat(MenuActivity.EXTRA_LATITUDE, 0);
        int refreshPeriod = extras.getInt(MenuActivity.EXTRA_REFRESH, 0);

        // Create fragments
        infoFragment = InfoFragment.newInstance(latitude, longitude);
        sunFragment = SunFragment.newInstance();
        moonFragment = MoonFragment.newInstance();

        // Get info
        if(savedInstanceState == null) {
            refreshAstroInfo();
        }
        else {
            DataWrapper wrapper = (DataWrapper) savedInstanceState.getSerializable("data");
            sunInfo = wrapper.getSunInfo();
            moonInfo = wrapper.getMoonInfo();
            lastRefreshTime = savedInstanceState.getLong("lastRefreshTime");
        }

        // Set info in fragments
        sunFragment.setInfo(sunInfo);
        moonFragment.setInfo(moonInfo);

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
            FragmentStateAdapter adapter = new Adapter(this, infoFragment, sunFragment, moonFragment);
            pager.setAdapter(adapter);
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
        DataWrapper wrapper = new DataWrapper(null, sunInfo, moonInfo);
        outState.putSerializable("data", wrapper);
        outState.putLong("lastRefreshTime", lastRefreshTime);
    }

    private void refreshAstroInfo() {
        AstroDateTime time = new AstroDateTime();
        AstroCalculator.Location location = new AstroCalculator.Location(latitude, longitude);
        AstroCalculator calculator = new AstroCalculator(time, location);

        sunInfo = calculator.getSunInfo();
        sunFragment.setInfo(sunInfo);

        moonInfo = calculator.getMoonInfo();
        moonFragment.setInfo(moonInfo);

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