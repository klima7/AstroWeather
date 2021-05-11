package com.klima7.astroweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.klima7.astroweather.db.AppDatabase;
import com.klima7.astroweather.db.DatabaseUtil;
import com.klima7.astroweather.db.LocationDao;
import com.klima7.astroweather.weather.Location;
import com.klima7.astroweather.weather.YahooLocationRequest;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private AppDatabase db;
    private LocationAdapter adapter;
    private EditText locationEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        db = DatabaseUtil.getDatabase(getApplicationContext());

        locationEdit = findViewById(R.id.location_name_edit);

        Button addButton = findViewById(R.id.add_location_button);
        addButton.setOnClickListener(view -> addLocationClicked());

        RecyclerView recycler = findViewById(R.id.place_recycler);
        adapter = new LocationAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(layoutManager);

        new Thread(new FetchLocationsTask()).start();
    }

    public void addLocationClicked() {
        RequestManager requestManager = RequestManager.getInstance(this);
        String locationName = locationEdit.getText().toString();
        YahooLocationRequest reques = new YahooLocationRequest(locationName, new Response.Listener<Location>() {
            @Override
            public void onResponse(Location location) {
                locationEdit.setText("");

                if(!location.isValid()) {
                    Toast.makeText(getApplicationContext(), "Nieprawidłowa lokalizacja", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(Location l : adapter.getLocations()) {
                    if(l.equals(location)) {
                        Toast.makeText(getApplicationContext(), "Lokalizacja już jest na liśćie", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                adapter.addLocation(location);
                new Thread(new AddLocationTask(location)).start();
            }
        }, null);
        requestManager.addToRequestQueue(reques);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Location location = adapter.getLocation();
        new Thread(new RemoveLocationTask(location)).start();
        adapter.removeLocation(location);
        return true;
    }

    private class FetchLocationsTask implements Runnable {
        @Override
        public void run() {
            List<Location> locations = db.locationDao().getAll();
            runOnUiThread(() -> {
                adapter.setLocations(locations);
                adapter.notifyDataSetChanged();
            });
        }
    }

    private class AddLocationTask implements Runnable {
        private Location location;
        public AddLocationTask(Location location) {
            this.location = location;
        }
        @Override
        public void run() {
            db.locationDao().insertAll(this.location);
        }
    }

    private class RemoveLocationTask implements Runnable {
        private Location location;
        public RemoveLocationTask(Location location) {
            this.location = location;
        }
        @Override
        public void run() {
            db.locationDao().delete(this.location);
        }
    }
}