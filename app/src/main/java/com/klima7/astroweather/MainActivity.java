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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.google.android.material.snackbar.Snackbar;
import com.klima7.astroweather.db.AppDatabase;
import com.klima7.astroweather.db.DatabaseUtil;
import com.klima7.astroweather.fragments.InfoFragment;

import java.time.LocalDateTime;
import java.util.GregorianCalendar;
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void locationChangeClicked() {
        Intent intent = new Intent(this, LocationActivity.class);
        locationLauncher.launch(intent);
    }

    private void onLocationChanged(ActivityResult result) {
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

    public void update() {
        updateAstro();
        updateWeather();
        data.lastRefresh.setValue(System.currentTimeMillis());
    }

    private void updateAstro() {
        if(data.location.getValue() == null) {
            data.sunInfo.setValue(null);
            data.moonInfo.setValue(null);
            data.weather.setValue(null);
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

    private void updateWeather() {
        if(data.location.getValue() == null) {
            data.weather.setValue(null);
            return;
        }

        // TODO: update and assign data.weather
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
            }

            else if(!new_connected && old_connected) {
                data.connected.setValue(false);
                Snackbar snackbar = Snackbar.make(refreshLayout, "Brak połączenia z internetem", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

}