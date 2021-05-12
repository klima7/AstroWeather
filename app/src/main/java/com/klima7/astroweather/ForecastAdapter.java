package com.klima7.astroweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.klima7.astroweather.weather.Forecast;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastHolder> {

    private List<Forecast> forecasts;

    public ForecastAdapter(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    @NonNull
    @Override
    public ForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForecastHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_entry, parent, false));
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
    }

    public class ForecastHolder extends RecyclerView.ViewHolder {

        public ForecastHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void update(Forecast forecast) {
        }
    }
}
