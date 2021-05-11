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
import com.klima7.astroweather.weather.Weather;
import com.klima7.astroweather.weather.YahooLocationRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        YahooLocationRequest reques = new YahooLocationRequest(locationName, new Response.Listener<Weather>() {
            @Override
            public void onResponse(Weather weather) {
                Location location = weather.getLocation();
                locationEdit.setText("");

                if(!location.isValid()) {
                    Toast.makeText(getApplicationContext(), "Nieprawidłowa lokalizacja", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(Weather w : adapter.getWeathers()) {
                    if(w.equals(location)) {
                        Toast.makeText(getApplicationContext(), "Lokalizacja już jest na liśćie", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                adapter.addWeather(weather);
                new Thread(new AddWeatherTask(weather)).start();
            }
        }, null);
        requestManager.addToRequestQueue(reques);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Weather weather = adapter.getWeather();
        new Thread(new RemoveWeatherTask(weather)).start();
        adapter.removeWeather(weather);
        return true;
    }

    @Override
    public void locationSelected(Weather weather) {
        Intent data = new Intent();
        data.putExtra(WEATHER_ID, weather.getId());
        setResult(RESULT_OK, data);
        finish();
    }

    private class FetchWeathersTask implements Runnable {
        @Override
        public void run() {
            List<Weather> weathers = db.weatherDao().getAll();
            runOnUiThread(() -> {
                adapter.setWeathers(weathers);
                adapter.notifyDataSetChanged();
            });
        }
    }

    private class AddWeatherTask implements Runnable {
        private Weather weather;
        public AddWeatherTask(Weather weather) {
            this.weather = weather;
        }
        @Override
        public void run() {
            db.weatherDao().insertAll(this.weather);
        }
    }

    private class RemoveWeatherTask implements Runnable {
        private Weather weather;
        public RemoveWeatherTask(Weather weather) {
            this.weather = weather;
        }
        @Override
        public void run() {
            db.weatherDao().delete(weather);
        }
    }
}