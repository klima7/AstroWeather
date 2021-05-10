package com.klima7.astroweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.klima7.astroweather.yahoo.YahooLocation;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {

    private List<YahooLocation> locations;

    public LocationAdapter(List<YahooLocation> locations) {
        this.locations = locations;
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocationHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.location_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        YahooLocation location = locations.get(position);
        holder.update(location);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void addLocation(YahooLocation location) {
        locations.add(location);
        notifyDataSetChanged();
    }

    public static class LocationHolder extends RecyclerView.ViewHolder {

        private TextView cityView, regionView, latitudeView, longitudeView;

        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            cityView = itemView.findViewById(R.id.entry_city);
            regionView = itemView.findViewById(R.id.entry_region);
            latitudeView = itemView.findViewById(R.id.entry_latitude);
            longitudeView = itemView.findViewById(R.id.entry_longitude);
        }

        public void update(YahooLocation location) {
            cityView.setText(location.getCity());
            regionView.setText(location.getRegion().trim());
            latitudeView.setText(String.valueOf(location.getLatitude()));
            longitudeView.setText(String.valueOf(location.getLongitude()));
        }
    }
}
