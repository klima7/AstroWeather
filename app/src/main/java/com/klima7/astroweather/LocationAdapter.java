package com.klima7.astroweather;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.klima7.astroweather.weather.Location;
import com.klima7.astroweather.weather.Weather;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.WeatherHolder> {

    private Weather weather;
    private List<Weather> weathers;

    public LocationAdapter(List<Weather> weathers) {
        this.weathers = weathers;
    }

    @NonNull
    @Override
    public WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.location_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHolder holder, int position) {
        Weather weather = weathers.get(position);
        holder.update(weather);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setWeather(weather);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    public void addWeather(Weather weather) {
        weathers.add(weather);
        notifyDataSetChanged();
    }

    public void removeWeather(Weather weather) {
        weathers.remove(weather);
        notifyDataSetChanged();
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public List<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }

    public static class WeatherHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private TextView cityView, regionView, latitudeView, longitudeView;

        public WeatherHolder(@NonNull View itemView) {
            super(itemView);
            cityView = itemView.findViewById(R.id.entry_city);
            regionView = itemView.findViewById(R.id.entry_region);
            latitudeView = itemView.findViewById(R.id.entry_latitude);
            longitudeView = itemView.findViewById(R.id.entry_longitude);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void update(Weather weather) {
            Location location = weather.getLocation();
            cityView.setText(location.getCity());
            regionView.setText(location.getRegion().trim());
            latitudeView.setText(String.valueOf(location.getLatitude()));
            longitudeView.setText(String.valueOf(location.getLongitude()));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Akcja");
            menu.add(0, v.getId(), 0, "Usuń");
        }
    }

    public interface OnLocationSelectedListener {
        void locationSelected(Weather weather);
    }
}
