package com.klima7.astroweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.klima7.astroweather.db.AppDatabase;
import com.klima7.astroweather.db.DatabaseUtil;
import com.klima7.astroweather.weather.Location;
import com.klima7.astroweather.weather.YahooLocationRequest;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements LocationAdapter.OnLocationSelectedListener {

    public static final String RET_ID = "id";

    private AppDatabase db;
    private LocationAdapter adapter;
    private EditText locationEdit;
    private LinearLayout addLocationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        db = DatabaseUtil.getDatabase(getApplicationContext());

        locationEdit = findViewById(R.id.location_name_edit);
        addLocationView = findViewById(R.id.add_location_pane);

        Button addButton = findViewById(R.id.add_location_button);
        addButton.setOnClickListener(view -> addClicked());

        RecyclerView recycler = findViewById(R.id.place_recycler);
        adapter = new LocationAdapter(new ArrayList<>(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(layoutManager);

        registerReceiver(new LocationActivity.NetworkChangeReceiver(), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        new Thread(new FetchWeathersTask()).start();
    }

    public void addClicked() {
        RequestManager requestManager = RequestManager.getInstance(this);
        String locationName = locationEdit.getText().toString();
        YahooLocationRequest reques = new YahooLocationRequest(locationName, new Response.Listener<Location>() {
            @Override
            public void onResponse(Location location) {
                locationEdit.setText("");

                if(location.woeid == 0) {
                    Toast.makeText(getApplicationContext(), "Nieprawidłowa lokalizacja", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(Location w : adapter.getLocations()) {
                    if(w.equals(location)) {
                        Toast.makeText(getApplicationContext(), "Lokalizacja już jest na liśćie", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                adapter.addLocation(location);
                new Thread(new AddWeatherTask(location)).start();
            }
        }, null);
        requestManager.addToRequestQueue(reques);
    }

    @Override
    public void locationSelected(Location location) {
        Intent data = new Intent();
        data.putExtra(RET_ID, location.woeid);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Location location = adapter.getLocation();
        new Thread(new RemoveWeatherTask(location)).start();
        adapter.removeLocation(location);
        return true;
    }

    private class FetchWeathersTask implements Runnable {
        @Override
        public void run() {
            List<Location> locations = db.locationDao().getAll();
            runOnUiThread(() -> adapter.setLocations(locations));
        }
    }

    private class AddWeatherTask implements Runnable {
        private Location location;
        public AddWeatherTask(Location location) {
            this.location = location;
        }
        @Override
        public void run() {
            db.locationDao().insertAll(this.location);
        }
    }

    private class RemoveWeatherTask implements Runnable {
        private Location location;
        public RemoveWeatherTask(Location location) {
            this.location = location;
        }
        @Override
        public void run() {
            db.locationDao().delete(location);
        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            boolean connected = connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isConnected();
            addLocationView.setVisibility(connected ? View.VISIBLE : View.INVISIBLE);
        }
    }
}