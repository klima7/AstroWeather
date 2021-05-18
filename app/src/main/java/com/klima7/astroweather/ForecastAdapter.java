package com.klima7.astroweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.klima7.astroweather.weather.Forecast;

import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastHolder> {

    private List<Forecast> forecasts = new ArrayList<>();
    private Unit unit;
    private View noForecastMessage;

    public ForecastAdapter(View noForecastMessage) {
        this.noForecastMessage = noForecastMessage;
    }

    @NonNull
    @Override
    public ForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForecastHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_forecast, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastHolder holder, int position) {
        Forecast forecast = forecasts.get(position);
        holder.update(forecast);
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    public void setForecasts(List<Forecast> forecasts, Unit unit) {
        this.forecasts = forecasts;
        this.unit = unit;
        this.notifyDataSetChanged();
        updateNoForecastMessage();
    }

    private void updateNoForecastMessage() {
        if(forecasts.isEmpty())
            noForecastMessage.setVisibility(View.VISIBLE);
        else
            noForecastMessage.setVisibility(View.INVISIBLE);
    }

    public class ForecastHolder extends RecyclerView.ViewHolder {

        private TextView dateView, dayView, temperatureView, conditionView;

        public ForecastHolder(@NonNull View itemView) {
            super(itemView);
            dateView = itemView.findViewById(R.id.forecast_date);
            dayView = itemView.findViewById(R.id.forecast_day);
            temperatureView = itemView.findViewById(R.id.forecast_temperature);
            conditionView = itemView.findViewById(R.id.forecast_conditions);
        }

        public void update(Forecast forecast) {
            dateView.setText(String.valueOf(forecast.date));
            dayView.setText(String.valueOf(forecast.day));
            temperatureView.setText(forecast.low + "-" + forecast.high + unit.getTemperatureUnit());
            conditionView.setText(String.valueOf(forecast.text));
        }
    }
}
