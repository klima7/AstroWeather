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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.klima7.astroweather.fragments.InfoFragment;
import com.klima7.astroweather.fragments.MoonFragment;
import com.klima7.astroweather.fragments.SunFragment;
import com.klima7.astroweather.yahoo.YahooWeather;
import com.klima7.astroweather.yahoo.YahooWeatherRequest;

import java.util.Timer;
import java.util.TimerTask;

public class AstroActivity extends FragmentActivity implements InfoFragment.InfoInterface {

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

        // Reconfigure timer on refresh change
        data.refreshPeriod.observe(this, newRefreshPeriod -> {
            refreshTask.cancel();
            timer.cancel();
            scheduleRefresh();
        });

        // Create menu launcher
        startMenu = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> applyConfig(result));
    }

    @Override
    protected void onStart() {
        super.onStart();
        scheduleRefresh();

        super.onStart();

        RequestManager requestManager = RequestManager.getInstance(this);
        YahooWeatherRequest request = new YahooWeatherRequest(2502265, YahooWeatherRequest.METRIC_UNIT, new Response.Listener<YahooWeather>() {
            @Override
            public void onResponse(YahooWeather response) {
                Log.i("Hello", "Weather = " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Hello", "Error response received");
            }
        });
        requestManager.addToRequestQueue(request);
    }

    @Override
    protected void onStop() {
        super.onStop();
        refreshTask.cancel();
        timer.cancel();
    }

    @Override
    public void settingsClicked() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra(MenuActivity.LATITUDE, data.latitude.getValue());
        intent.putExtra(MenuActivity.LONGITUDE, data.longitude.getValue());
        intent.putExtra(MenuActivity.REFRESH, data.refreshPeriod.getValue());
        startMenu.launch(intent);
    }

    private void scheduleRefresh() {
        timer = new Timer();
        refreshTask = new RefreshTask();
        long nextRefreshTime = data.lastRefresh.getValue() + data.refreshPeriod.getValue()*1000;
        long nextRefreshDelay = Math.max(nextRefreshTime - System.currentTimeMillis(), 0);
        timer.scheduleAtFixedRate(refreshTask, nextRefreshDelay, data.refreshPeriod.getValue() * 1000);
    }

    private void applyConfig(ActivityResult result) {
        Intent intent = result.getData();
        if(intent == null) return;
        Bundle extras = intent.getExtras();

        float new_latitude = extras.getFloat(MenuActivity.LATITUDE);
        float new_longitude = extras.getFloat(MenuActivity.LONGITUDE);
        int new_refresh = extras.getInt(MenuActivity.REFRESH);

        if(new_latitude != data.latitude.getValue() || new_longitude != data.longitude.getValue() || new_refresh != data.refreshPeriod.getValue()) {
            data.latitude.setValue(new_latitude);
            data.longitude.setValue(new_longitude);
            data.refreshPeriod.setValue(new_refresh);
            refresh();
        }
    }

    private void refresh() {
        runOnUiThread(() -> {
            Toast.makeText(AstroActivity.this, "Odświeżenie", Toast.LENGTH_SHORT).show();
            data.refresh();
        });
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            refresh();
        }
    }

}