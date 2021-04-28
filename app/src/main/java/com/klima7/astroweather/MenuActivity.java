package com.klima7.astroweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.klima7.astroweather.fragments.Config;

public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_LATITUDE = "latitude";
    public static final String EXTRA_LONGITUDE = "longitude";
    public static final String EXTRA_REFRESH = "refresh";

    public static final int REFRESH_TIMES[] = {10, 30, 60, 300, 600, 900, 3600};
    private int selectedRefreshTime = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Spinner spinner = findViewById(R.id.refresh_frequency_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.refresh_times, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedRefreshTime = REFRESH_TIMES[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selectedRefreshTime = 0;
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

        if(selectedRefreshTime == 0) {
            Toast.makeText(this, R.string.invalid_refresh, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, AstroActivity.class);
        Bundle bundle = new Bundle();
        bundle.putFloat(EXTRA_LATITUDE, latitude);
        bundle.putFloat(EXTRA_LONGITUDE, longitude);
        bundle.putInt(EXTRA_REFRESH, selectedRefreshTime);
        intent.putExtras(bundle);
        startActivity(intent);

        Config config = new Config(1, 1, 0);
        Intent data = new Intent();
        data.putExtra("config", config);
        setResult(RESULT_OK, data);
    }
}