package com.klima7.astroweather;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.klima7.astroweather.fragments.Config;
import com.klima7.astroweather.fragments.InfoFragment;
import com.klima7.astroweather.fragments.MoonFragment;
import com.klima7.astroweather.fragments.SunFragment;

import java.util.Timer;
import java.util.TimerTask;

public class AstroActivity extends FragmentActivity implements InfoFragment.InfoInterface {

    private InfoFragment infoFragment;
    private SunFragment sunFragment;
    private MoonFragment moonFragment;

    private AstroData astroData;

    Config config;
    ActivityResultLauncher startMenu;

    private Timer timer;
    private TimerTask refreshTask;
    private long lastRefreshTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astro);

        astroData = new ViewModelProvider(this).get(AstroData.class);

        // Get arguments
        config = new Config(0, 0, 10);

        // Retrieve saved state
        if(savedInstanceState != null) {
            lastRefreshTime = savedInstanceState.getLong("lastRefreshTime");
        }

        // Retrieve fragments if they exists
        FragmentManager fm = getSupportFragmentManager();
        sunFragment = (SunFragment)fm.findFragmentByTag("f0");
        moonFragment = (MoonFragment)fm.findFragmentByTag("f1");

        // Create new fragments if they not exists
        if(infoFragment == null) infoFragment = InfoFragment.newInstance(config.getLatitude(), config.getLongitude());
        if(sunFragment == null) sunFragment = new SunFragment();
        if(moonFragment == null) moonFragment = new MoonFragment();

        // Refresh data if first launch
        if(savedInstanceState == null)
            refreshAstroInfo();

        // Update
        sunFragment.update(sunInfo);
        moonFragment.update(moonInfo);

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

        // Create menu launcher
        startMenu = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                        Log.i("Hello", "Ejj");
                        Intent intent = result.getData();
                        Bundle extras = intent.getExtras();
                        config = (Config)extras.getSerializable("config");
                        Log.i("Hello", "Data received " + config.getLatitude());
                    });
    }

    @Override
    protected void onStop() {
        super.onStop();
        refreshTask.cancel();
    }

    @Override
    protected void onStart() {
        super.onStart();

        timer = new Timer();
        refreshTask = new RefreshTask();
        long nextRefreshTime = lastRefreshTime + config.getRefresh()*1000;
        long nextRefreshDelay = Math.max(nextRefreshTime - System.currentTimeMillis(), 0);
        timer.scheduleAtFixedRate(refreshTask, nextRefreshDelay, config.getRefresh()*1000);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("lastRefreshTime", lastRefreshTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Hello", "Destroyed :(");
    }

    @Override
    public void settingsClicked() {
        Intent intent = new Intent(this, MenuActivity.class);
        startMenu.launch(intent);
    }

    private void refreshAstroInfo() {
        AstroDateTime time = new AstroDateTime();
        AstroCalculator.Location location = new AstroCalculator.Location(config.getLatitude(), config.getLongitude());
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