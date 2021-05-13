package com.klima7.astroweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.klima7.astroweather.db.AppDatabase;
import com.klima7.astroweather.db.DatabaseUtil;
import com.klima7.astroweather.weather.Location;
import com.klima7.astroweather.weather.Entry;
import com.klima7.astroweather.weather.YahooLocationRequest;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements LocationAdapter.OnLocationSelectedListener {

    public static final String WEATHER_ID = "id";

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
        adapter = new LocationAdapter(new ArrayList<>(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(layoutManager);

        new Thread(new FetchWeathersTask()).start();
    }

    public void addLocationClicked() {
        RequestManager requestManager = RequestManager.getInstance(this);
        String locationName = locationEdit.getText().toString();
        YahooLocationRequest reques = new YahooLocationRequest(locationName, Unit.METRIC, new Response.Listener<Entry>() {
            @Override
            public void onResponse(Entry entry) {
                Location location = entry.getLocation();
                locationEdit.setText("");

                if(!location.isValid()) {
                    Toast.makeText(getApplicationContext(), "Nieprawidłowa lokalizacja", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(Entry w : adapter.getEntries()) {
                    if(w.equals(location)) {
                        Toast.makeText(getApplicationContext(), "Lokalizacja już jest na liśćie", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                adapter.addEntry(entry);
                new Thread(new AddWeatherTask(entry)).start();
            }
        }, null);
        requestManager.addToRequestQueue(reques);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Entry entry = adapter.getEntry();
        new Thread(new RemoveWeatherTask(entry)).start();
        adapter.removeEntry(entry);
        return true;
    }

    @Override
    public void locationSelected(Entry entry) {
        Intent data = new Intent();
        data.putExtra(WEATHER_ID, entry.getId());
        setResult(RESULT_OK, data);
        finish();
    }

    private class FetchWeathersTask implements Runnable {
        @Override
        public void run() {
            List<Entry> entries = db.entryDao().getAll();
            runOnUiThread(() -> {
                adapter.setEntries(entries);
                adapter.notifyDataSetChanged();
            });
        }
    }

    private class AddWeatherTask implements Runnable {
        private Entry entry;
        public AddWeatherTask(Entry entry) {
            this.entry = entry;
        }
        @Override
        public void run() {
            db.entryDao().insertAll(this.entry);
        }
    }

    private class RemoveWeatherTask implements Runnable {
        private Entry entry;
        public RemoveWeatherTask(Entry entry) {
            this.entry = entry;
        }
        @Override
        public void run() {
            db.entryDao().delete(entry);
        }
    }
}