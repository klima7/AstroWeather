package com.klima7.astroweather;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.klima7.astroweather.weather.Location;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {

    private Location location;
    private List<Location> locations;

    public LocationAdapter(List<Location> locations) {
        this.locations = locations;
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocationHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.location_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        Location location = locations.get(position);
        holder.update(location);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setLocation(location);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void addLocation(Location location) {
        locations.add(location);
        notifyDataSetChanged();
    }

    public void removeLocation(Location location) {
        locations.remove(location);
        notifyDataSetChanged();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public static class LocationHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private TextView cityView, regionView, latitudeView, longitudeView;

        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            cityView = itemView.findViewById(R.id.entry_city);
            regionView = itemView.findViewById(R.id.entry_region);
            latitudeView = itemView.findViewById(R.id.entry_latitude);
            longitudeView = itemView.findViewById(R.id.entry_longitude);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void update(Location location) {
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
}
