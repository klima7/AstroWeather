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

public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_LATITUDE = "latitude";
    public static final String EXTRA_LONGITUDE = "latitude";
    public static final String EXTRA_REFRESH = "refresh";

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
        Log.i("Hello", "Selected");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.i("Hello", "Nothing selected");
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

        Intent intent = new Intent(this, AstroActivity.class);
        Bundle bundle = new Bundle();
        bundle.putFloat(EXTRA_LATITUDE, latitude);
        bundle.putFloat(EXTRA_LONGITUDE, longitude);
        bundle.putInt(EXTRA_REFRESH, 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}