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
import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.google.android.material.snackbar.Snackbar;
import com.klima7.astroweather.db.AppDatabase;
import com.klima7.astroweather.db.DatabaseUtil;
import com.klima7.astroweather.fragments.InfoFragment;
import com.klima7.astroweather.weather.Location;
import com.klima7.astroweather.weather.Weather;
import com.klima7.astroweather.weather.YahooWeatherRequest;

import java.time.LocalDateTime;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements InfoFragment.InfoInterface, SwipeRefreshLayout.OnRefreshListener {

    private AppDatabase db;
    private RequestManager requestManager;
    SharedPreferences sharedPreferences;

    private AppData data;
    private Timer timer;
    private TimerTask refreshTask;
    private ActivityResultLauncher locationLauncher;
    private ActivityResultLauncher settingsLauncher;
    private NetworkChangeReceiver networkReceiver;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astro);

        db = DatabaseUtil.getDatabase(getApplicationContext());
        requestManager = RequestManager.getInstance(getApplication());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

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

        // Create launchers
        locationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> onLocationChanged(result));

        settingsLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> onSettingsChanged(result));

        if(savedInstanceState == null) {
            update(sharedPreferences.getInt("woeid", 0), data.unit.getValue());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        networkReceiver = new NetworkChangeReceiver();
        registerReceiver(networkReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        scheduleRefresh();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkReceiver);
        refreshTask.cancel();
        timer.cancel();
    }

    @Override
    public void locationChangeClicked() {
        Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra(LocationActivity.EXTRA_WOEID, data.location.getValue() != null ? data.location.getValue().woeid : 0);
        locationLauncher.launch(intent);
    }

    private void onLocationChanged(ActivityResult result) {
        if(result == null || result.getData() == null)
            return;
        int woeid = result.getData().getIntExtra(LocationActivity.RET_WOEID, 0);
        sharedPreferences.edit().putInt("woeid", woeid).apply();
        update(woeid, data.unit.getValue());
    }

    @Override
    public void settingsClicked() {
        Intent intent = new Intent(this, SettingsActivity.class);
        settingsLauncher.launch(intent);
    }

    private void onSettingsChanged(ActivityResult result) {
        Unit newUnit = Unit.fromCode(sharedPreferences.getString("unit", "c"));
        if(data.unit.getValue() != newUnit) {
            data.unit.setValue(newUnit);
            update();
        }

        int newRefreshPeriod = Integer.parseInt(sharedPreferences.getString("refresh", "10"));
        if(data.refreshPeriod.getValue() != newRefreshPeriod) {
            data.refreshPeriod.setValue(newRefreshPeriod);
            refreshTask.cancel();
            timer.cancel();
            scheduleRefresh();
        }
    }

    private void scheduleRefresh() {
        timer = new Timer();
        refreshTask = new RefreshTask();
        long nextRefreshTime = data.lastRefresh.getValue() + data.refreshPeriod.getValue()*1000;
        long nextRefreshDelay = Math.max(nextRefreshTime - System.currentTimeMillis(), 0);
        timer.scheduleAtFixedRate(refreshTask, nextRefreshDelay, data.refreshPeriod.getValue() * 1000);
    }

    @Override
    public void onRefresh() {
        update();
        refreshLayout.setRefreshing(false);
    }

    private void update() {
        int woeid = data.location.getValue() != null ? data.location.getValue().woeid : 0;
        Unit unit = data.unit.getValue();
        update(woeid, unit);
    }

    private void update(int woeid, Unit unit) {
        updateLocationAndAstro(woeid);
        updateWeather(woeid, unit);
        data.lastRefresh.setValue(System.currentTimeMillis());
    }

    private void updateLocationAndAstro(int woeid) {
        if(woeid == 0) {
            data.location.setValue(null);
            data.sunInfo.setValue(null);
            data.moonInfo.setValue(null);
            return;
        }

        new Thread(new UpdateLocationAndAstroTask(woeid)).start();
    }

    private void updateWeather(int woeid, Unit unit) {
        if(woeid == 0) {
            data.weather.setValue(null);
            return;
        }

        if(data.connected.getValue()) {
            YahooWeatherRequest request = new YahooWeatherRequest(woeid, unit, new Response.Listener<Weather>() {
                @Override
                public void onResponse(Weather weather) {
                    data.weather.setValue(weather);
                    new Thread(new InsertWeatherTask(weather)).start();
                }
            }, null);

            requestManager.addToRequestQueue(request);
        }

        else {
            new Thread(new SetSavedWeatherTask(woeid, unit)).start();
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

        AstroCalculator.Location astroLocation = new AstroCalculator.Location(data.location.getValue().latitude, data.location.getValue().longitude);
        AstroCalculator calculator = new AstroCalculator(time, astroLocation);

        data.sunInfo.setValue(calculator.getSunInfo());
        data.moonInfo.setValue(calculator.getMoonInfo());
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(()-> {
                update();
                Toast.makeText(MainActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private class UpdateLocationAndAstroTask implements Runnable {
        private int woeid;

        public UpdateLocationAndAstroTask(int woeid) {
            this.woeid = woeid;
        }

        @Override
        public void run() {
            Location location = db.locationDao().getSingle(woeid);
            runOnUiThread(() -> {
                data.location.setValue(location);
                updateAstro();
            });
        }
    }

    private class InsertWeatherTask implements Runnable {
        private Weather weather;

        public InsertWeatherTask(Weather weather) {
            this.weather = weather;
        }

        @Override
        public void run() {
            Weather current = db.weatherDao().getSingle(weather.woeid, weather.unit);
            if(current == null) db.weatherDao().insertAll(weather);
            else db.weatherDao().update(weather);
        }
    }

    private class SetSavedWeatherTask implements Runnable {
        private int woeid;
        private Unit unit;

        public SetSavedWeatherTask(int woeid, Unit unit) {
            this.woeid = woeid;
            this.unit = unit;
        }

        @Override
        public void run() {
            Weather weather = db.weatherDao().getSingle(woeid, unit);
            runOnUiThread(() -> data.weather.setValue(weather));
        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            boolean old_connected = data.connected.getValue();
            boolean new_connected = connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isConnected();

            if(new_connected && !old_connected) {
                data.connected.setValue(true);
                refreshLayout.setEnabled(true);
            }

            else if(!new_connected && old_connected) {
                data.connected.setValue(false);
                refreshLayout.setEnabled(false);
                Snackbar snackbar = Snackbar.make(refreshLayout, "No Internet connection", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }
}