package com.klima7.astroweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;

public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String REFRESH = "refresh";

    public static final int REFRESH_TIMES[] = {10, 30, 60, 300, 600, 900, 3600};
    private int selectedRefresh = 10;

    private EditText latitudeEdit, longitudeEdit;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Get views
        latitudeEdit = findViewById(R.id.latitude_input);
        longitudeEdit = findViewById(R.id.longitude_input);
        spinner = findViewById(R.id.refresh_frequency_spinner);

        // Configure spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.refresh_times, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Set position default values
        Intent intent = getIntent();
        latitudeEdit.setText(String.valueOf(intent.getFloatExtra(LATITUDE, 0)));
        longitudeEdit.setText(String.valueOf(intent.getFloatExtra(LONGITUDE, 0)));

        // Set refresh default value
        int defaultRefresh = intent.getIntExtra(REFRESH, 10);
        for(int index=0; index<REFRESH_TIMES.length; index++) {
            if(REFRESH_TIMES[index] == defaultRefresh) {
                spinner.setSelection(index);
                break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedRefresh = REFRESH_TIMES[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selectedRefresh = 0;
    }

    public void confirmClicked(View view) {
        EditText latitudeEdit = findViewById(R.id.latitude_input);
        EditText longitudeEdit = findViewById(R.id.longitude_input);

        String latitudeStr = latitudeEdit.getText().toString();
        String longitudeStr = longitudeEdit.getText().toString();

        float latitude, longitude;

        try {
            latitude = Float.parseFloat(latitudeStr);
            longitude = Float.parseFloat(longitudeStr);
        } catch(NumberFormatException e) {
            Toast.makeText(this, R.string.invalid_position, Toast.LENGTH_SHORT).show();
            return;
        }

        if(selectedRefresh == 0) {
            Toast.makeText(this, R.string.invalid_refresh, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(LATITUDE, latitude);
        data.putExtra(LONGITUDE, longitude);
        data.putExtra(REFRESH, selectedRefresh);
        setResult(RESULT_OK, data);
        finish();
    }
}