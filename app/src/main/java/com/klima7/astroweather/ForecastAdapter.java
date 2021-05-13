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

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
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

        private TextView dateView, temperatureView, descriptionView, conditionView;

        public ForecastHolder(@NonNull View itemView) {
            super(itemView);
            dateView = itemView.findViewById(R.id.forecast_date);
            temperatureView = itemView.findViewById(R.id.forecast_temperature);
            descriptionView = itemView.findViewById(R.id.forecast_description);
            conditionView = itemView.findViewById(R.id.forecast_conditions);
        }

        public void update(Forecast forecast) {
            dateView.setText(String.valueOf(forecast.getDay()));
            temperatureView.setText(forecast.getLow() + "-" + forecast.getHigh());
            descriptionView.setText(String.valueOf(forecast.getText()));
            conditionView.setText(String.valueOf(forecast.getCode()));
        }
    }
}
