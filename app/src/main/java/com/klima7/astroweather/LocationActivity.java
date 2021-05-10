package com.klima7.astroweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.klima7.astroweather.yahoo.YahooLocation;
import com.klima7.astroweather.yahoo.YahooLocationRequest;

import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        RecyclerView recycler = findViewById(R.id.place_recycler);
        LocationAdapter adapter = new LocationAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(layoutManager);

        RequestManager requestManager = RequestManager.getInstance(this);
        YahooLocationRequest reques = new YahooLocationRequest("Kompina", new Response.Listener<YahooLocation>() {
            @Override
            public void onResponse(YahooLocation location) {
                Log.i("Hello", "location = " + location);
                adapter.addLocation(location);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Hello", "Error response received");
            }
        });
        requestManager.addToRequestQueue(reques);
    }
}