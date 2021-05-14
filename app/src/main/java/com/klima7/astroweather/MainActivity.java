package com.klima7.astroweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
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
import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.klima7.astroweather.db.AppDatabase;
import com.klima7.astroweather.db.DatabaseUtil;
import com.klima7.astroweather.fragments.InfoFragment;
import com.klima7.astroweather.weather.Entry;
import com.klima7.astroweather.weather.YahooWeatherRequest;

import java.time.LocalDateTime;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements InfoFragment.InfoInterface, SwipeRefreshLayout.OnRefreshListener {

    private AppDatabase db;
    private RequestManager requestManager;

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

        db = DatabaseUtil.getDatabase(getApplicationContext());
        requestManager = RequestManager.getInstance(getApplication());

        // Attach ViewModel
        data = new ViewModelProvider(this).get(AppData.class);

        // Get configuration
        int size = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        boolean tablet = size == Configuration.SCREENLAYOUT_SIZE_LARGE || size == Configuration.SCREENLAYOUT_SIZE_XLARGE;

        // Set view pager
        ViewPager2 pager = findViewById(R.id.pager);
        FragmentStateAdapter adapter = tablet ? new TabletPagerAdapter(this) : new MobilePagerAdapter(this);
        pager.setAdapter(adapter);

        // get refresh layout
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

        registerReceiver(new NetworkChangeReceiver(), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
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
    public void locationChangeClicked() {
        Intent intent = new Intent(this, LocationActivity.class);
        locationLauncher.launch(intent);
    }

    private void onLocationChanged(ActivityResult result) {
        Log.i("Hello", "Receiving data");
        if(result.getData()!=null) {
            int woeid = result.getData().getIntExtra(LocationActivity.RET_ID, 0);
            new Thread(new setLocationTask(woeid)).start();
        }

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
            update();
        });
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            refresh();
        }
    }

    private void updateAstro() {
        if(data.location.getValue() == null) {
            data.sunInfo.setValue(null);
            data.moonInfo.setValue(null);
            return;
        }

        GregorianCalendar cal = new GregorianCalendar();
        int y = cal.get(GregorianCalendar.YEAR);
        int mo = cal.get(GregorianCalendar.MONTH);
        int d = cal.get(GregorianCalendar.DAY_OF_MONTH);
        int h = cal.get(GregorianCalendar.HOUR);
        int mi = cal.get(GregorianCalendar.MINUTE);
        int s = cal.get(GregorianCalendar.SECOND);
        int zoneOffset = cal.toZonedDateTime().getZone().getRules().getOffset(LocalDateTime.now()).getTotalSeconds()/3600;
        AstroDateTime time = new AstroDateTime(y, mo, d, h, mi, s, zoneOffset, true);

        AstroCalculator.Location astroLocation = new AstroCalculator.Location(data.location.getValue().getLatitude(), data.location.getValue().getLongitude());
        AstroCalculator calculator = new AstroCalculator(time, astroLocation);

        data.sunInfo.setValue(calculator.getSunInfo());
        data.moonInfo.setValue(calculator.getMoonInfo());
    }

    public void update() {
        updateAstro();
        updateWeather();
        data.lastRefresh.setValue(System.currentTimeMillis());
    }

    private void updateWeather() {
        if(data.location.getValue() == null) {
            data.currentWeather.setValue(null);
            data.forecasts.setValue(null);
            return;
        }

        YahooWeatherRequest request = new YahooWeatherRequest(data.location.getValue().getWoeid(), data.unit.getValue(), new Response.Listener<Entry>() {
            @Override
            public void onResponse(Entry weather) {
                Log.i("Hello", "Updating weather: " + weather);
                data.currentWeather.setValue(weather.getCurrent());
                data.forecasts.setValue(weather.getFuture());
                new Thread(new updateWeatherTask(weather)).start();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Hello", "Updating weather: Error response received");
            }
        });
        requestManager.addToRequestQueue(request);
    }

    private class updateWeatherTask implements Runnable {
        private Entry entry;
        public updateWeatherTask(Entry entry) {
            this.entry = entry;
        }
        @Override
        public void run() {
            db.entryDao().insertAll(this.entry);
        }
    }

    private class setLocationTask implements Runnable {
        private int woeid;
        public setLocationTask(int woeid) {
            this.woeid = woeid;
        }
        @Override
        public void run() {
            List<Entry> entries = db.entryDao().getAll();
            for(Entry entry : entries) {
                if(entry.getLocation().getWoeid() == woeid) {

                    runOnUiThread(() -> {
                        data.location.setValue(entry.getLocation());
                        data.currentWeather.setValue(entry.getCurrent());
                        data.forecasts.setValue(entry.getFuture());
//                        data.entryId = entry.getId();
                    });
                }
            }
        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if(connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isConnected()) {
                Log.i("Hello", "Connected");
            }

            else {
                Log.i("Hello", "Disconnected");
            }
        }
    }

}