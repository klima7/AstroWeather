package com.klima7.astroweather;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.klima7.astroweather.fragments.InfoFragment;
import com.klima7.astroweather.fragments.MoonFragment;
import com.klima7.astroweather.fragments.SunFragment;

import java.util.Timer;
import java.util.TimerTask;

public class AstroActivity extends FragmentActivity implements InfoFragment.InfoInterface {

    private InfoFragment infoFragment;
    private SunFragment sunFragment;
    private MoonFragment moonFragment;

    private AstroData data;

    private Timer timer;
    private TimerTask refreshTask;

    private ActivityResultLauncher startMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astro);

        // Attach ViewModel
        data = new ViewModelProvider(this).get(AstroData.class);

        // Retrieve fragments if they exists
        FragmentManager fm = getSupportFragmentManager();
        sunFragment = (SunFragment)fm.findFragmentByTag("f0");
        moonFragment = (MoonFragment)fm.findFragmentByTag("f1");

        // Create new fragments if they not exists
        infoFragment = InfoFragment.newInstance(data.getLatitude(), data.getLongitude());
        if(sunFragment == null) sunFragment = SunFragment.newInstance(data.getSunInfo());
        if(moonFragment == null) moonFragment = MoonFragment.newInstance(data.getMoonInfo());

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
        startMenu = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> applyConfig(result));
    }

    @Override
    protected void onStart() {
        super.onStart();
        scheduleRefresh();
    }

    @Override
    protected void onStop() {
        super.onStop();
        refreshTask.cancel();
    }

    @Override
    public void settingsClicked() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra(MenuActivity.LATITUDE, data.getLatitude());
        intent.putExtra(MenuActivity.LONGITUDE, data.getLongitude());
        intent.putExtra(MenuActivity.REFRESH, data.getRefreshPeriod());
        startMenu.launch(intent);
    }

    private void scheduleRefresh() {
        timer = new Timer();
        refreshTask = new RefreshTask();
        long nextRefreshTime = data.getLastRefresh() + data.getRefreshPeriod()*1000;
        long nextRefreshDelay = Math.max(nextRefreshTime - System.currentTimeMillis(), 0);
        timer.scheduleAtFixedRate(refreshTask, nextRefreshDelay, data.getRefreshPeriod() * 1000);
    }

    private void applyConfig(ActivityResult result) {
        Intent intent = result.getData();
        if(intent == null) return;
        Bundle extras = intent.getExtras();

        data.setLatitude(extras.getFloat(MenuActivity.LATITUDE));
        data.setLongitude(extras.getFloat(MenuActivity.LONGITUDE));
        data.setRefreshPeriod(extras.getInt(MenuActivity.REFRESH));
        refresh();

        infoFragment.update(data.getLatitude(), data.getLongitude());

        refreshTask.cancel();
        scheduleRefresh();
    }

    private void refresh() {
        runOnUiThread(() -> Toast.makeText(AstroActivity.this, "Odświeżenie", Toast.LENGTH_SHORT).show());
        data.refresh();
        sunFragment.update(data.getSunInfo());
        moonFragment.update(data.getMoonInfo());
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            refresh();
        }
    }

}