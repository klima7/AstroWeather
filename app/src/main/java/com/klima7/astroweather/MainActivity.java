package com.klima7.astroweather;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.klima7.astroweather.fragments.InfoFragment;
import com.klima7.astroweather.fragments.MoonFragment;
import com.klima7.astroweather.fragments.SunFragment;
import com.klima7.astroweather.weather.Entry;
import com.klima7.astroweather.weather.YahooWeatherRequest;
import com.klima7.astroweather.weather.YahooLocationRequest;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements InfoFragment.InfoInterface, SwipeRefreshLayout.OnRefreshListener {

    private AppData data;
    private Timer timer;
    private TimerTask refreshTask;
    private ActivityResultLauncher startMenu;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astro);

        // Attach ViewModel
        data = new ViewModelProvider(this).get(AppData.class);

        // Get configuration
        int orientation = getResources().getConfiguration().orientation;
        int size = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        boolean tablet = size == Configuration.SCREENLAYOUT_SIZE_LARGE || size == Configuration.SCREENLAYOUT_SIZE_XLARGE;

        // Tablet
        if (tablet) {
            if(savedInstanceState == null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.sun_container, new SunFragment(), "sunFragment");
                transaction.add(R.id.moon_container, new MoonFragment(), "moonFragment");
                transaction.commit();
            }
        }

        // Mobile
        else {
            ViewPager2 pager = findViewById(R.id.pager);

            FragmentStateAdapter adapter = new Adapter(this);
            pager.setAdapter(adapter);

            if(orientation == Configuration.ORIENTATION_PORTRAIT) pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            else pager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        }

        // Set refresh layout
        refreshLayout = findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(this);


        // Reconfigure timer on refresh change
//        data.refreshPeriod.observe(this, newRefreshPeriod -> {
//            refreshTask.cancel();
//            timer.cancel();
//            scheduleRefresh();
//        });

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
        timer.cancel();
    }

    @Override
    public void settingsClicked() {
        Intent intent = new Intent(this, LocationActivity.class);
        startMenu.launch(intent);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
    }

    private void scheduleRefresh() {
        timer = new Timer();
        refreshTask = new RefreshTask();
        long nextRefreshTime = data.lastRefresh.getValue() + data.refreshPeriod.getValue()*1000;
        long nextRefreshDelay = Math.max(nextRefreshTime - System.currentTimeMillis(), 0);
        timer.scheduleAtFixedRate(refreshTask, nextRefreshDelay, data.refreshPeriod.getValue() * 1000);
    }

    private void applyConfig(ActivityResult result) {
        Log.i("Hello", "Receiving data");

//        if(new_latitude != data.latitude.getValue() || new_longitude != data.longitude.getValue() || new_refresh != data.refreshPeriod.getValue()) {
//            data.latitude.setValue(new_latitude);
//            data.longitude.setValue(new_longitude);
//            data.refreshPeriod.setValue(new_refresh);
//            refresh();
//        }
    }

    private void refresh() {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, "Odświeżenie", Toast.LENGTH_SHORT).show();
            data.update();
        });
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            refresh();
        }
    }

}