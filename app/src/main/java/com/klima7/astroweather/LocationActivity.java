package com.klima7.astroweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.klima7.astroweather.weather.Location;
import com.klima7.astroweather.weather.YahooLocationRequest;

import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity {

    private LocationAdapter adapter;
    private EditText locationEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        locationEdit = findViewById(R.id.location_name_edit);

        Button addButton = findViewById(R.id.add_location_button);
        addButton.setOnClickListener(view -> addLocationClicked());

        RecyclerView recycler = findViewById(R.id.place_recycler);
        adapter = new LocationAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(layoutManager);
    }

    private void addLocationClicked() {
        RequestManager requestManager = RequestManager.getInstance(this);
        String locationName = locationEdit.getText().toString();
        YahooLocationRequest reques = new YahooLocationRequest(locationName, new Response.Listener<Location>() {
            @Override
            public void onResponse(Location location) {
                if(location.isValid()) {
                    adapter.addLocation(location);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Nieprawidłowa lokalizacja", Toast.LENGTH_SHORT).show();
                }

                locationEdit.setText("");
            }
        }, null);
        requestManager.addToRequestQueue(reques);
    }
}