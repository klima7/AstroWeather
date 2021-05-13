package com.klima7.astroweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.klima7.astroweather.fragments.InfoFragment;
import com.klima7.astroweather.weather.Entry;
import com.klima7.astroweather.weather.YahooLocationRequest;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements InfoFragment.InfoInterface, SwipeRefreshLayout.OnRefreshListener {

    private AppData data;
    private Timer timer;
    private TimerTask refreshTask;
    private ActivityResultLauncher locationLauncher;
    private ActivityResultLauncher settingsLauncher;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astro);

        // Attach ViewModel
        data = new ViewModelProvider(this).get(AppData.class);

        // Get configuration
        int size = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        boolean tablet = size == Configuration.SCREENLAYOUT_SIZE_LARGE || size == Configuration.SCREENLAYOUT_SIZE_XLARGE;

        // Mobile
        if(!tablet) {
            ViewPager2 pager = findViewById(R.id.pager);

            FragmentStateAdapter adapter = new PagerAdapter(this);
            pager.setAdapter(adapter);
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

        // Create launchers
        locationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> onLocationChanged(result));

        settingsLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> onSettingsChanged(result));
    }

    @Override
    protected void onStart() {
        super.onStart();
        scheduleRefresh();

        RequestManager requestManager = RequestManager.getInstance(this);
        YahooLocationRequest request2 = new YahooLocationRequest("Kompina", Unit.METRIC, new Response.Listener<Entry>() {
            @Override
            public void onResponse(Entry weather) {
                Log.i("Hello", "location = " + weather);
                data.location.setValue(weather.getLocation());
                data.currentWeather.setValue(weather.getCurrent());
                data.forecasts.setValue(weather.getFuture());
                data.update();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Hello", "Error response received");
            }
        });
        requestManager.addToRequestQueue(request2);
    }

    @Override
    protected void onStop() {
        super.onStop();
        refreshTask.cancel();
        timer.cancel();
    }

    @Override
    public void locationChangeClicked() {
        Intent intent = new Intent(this, LocationActivity.class);
        locationLauncher.launch(intent);
    }

    private void onLocationChanged(ActivityResult result) {
        Log.i("Hello", "Receiving data");

//        if(new_latitude != data.latitude.getValue() || new_longitude != data.longitude.getValue() || new_refresh != data.refreshPeriod.getValue()) {
//            data.latitude.setValue(new_latitude);
//            data.longitude.setValue(new_longitude);
//            data.refreshPeriod.setValue(new_refresh);
//            refresh();
//        }
    }

    @Override
    public void settingsClicked() {
        Intent intent = new Intent(this, SettingsActivity.class);
        settingsLauncher.launch(intent);
    }

    private void onSettingsChanged(ActivityResult result) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
        String name = sharedPreferences.getString("refresh", "none");
        Log.i("Hello", "Settings changed: " + name);
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