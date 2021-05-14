package com.klima7.astroweather;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.klima7.astroweather.weather.Location;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {

    private Location location;
    private List<Location> entries;
    private OnLocationSelectedListener listener;

    public LocationAdapter(List<Location> entries, OnLocationSelectedListener listener) {
        this.entries = entries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocationHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_location, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        Location location = entries.get(position);
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
        return entries.size();
    }

    public void addLocation(Location location) {
        entries.add(location);
        notifyDataSetChanged();
    }

    public void removeLocation(Location location) {
        entries.remove(location);
        notifyDataSetChanged();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Location> getLocations() {
        return entries;
    }

    public void setLocations(List<Location> entries) {
        this.entries = entries;
        notifyDataSetChanged();
    }

    public class LocationHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private TextView cityView, regionView, latitudeView, longitudeView;
        private CardView locationCard;

        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            cityView = itemView.findViewById(R.id.entry_city);
            regionView = itemView.findViewById(R.id.entry_region);
            latitudeView = itemView.findViewById(R.id.entry_latitude);
            longitudeView = itemView.findViewById(R.id.entry_longitude);
            locationCard = itemView.findViewById(R.id.location_card);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void update(Location location) {
            cityView.setText(location.city);
            regionView.setText(location.region.trim());
            latitudeView.setText(String.valueOf(location.latitude));
            longitudeView.setText(String.valueOf(location.longitude));
            locationCard.setOnClickListener(v -> listener.locationSelected(location));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Akcja");
            menu.add(0, v.getId(), 0, "Usuń");
        }
    }

    public interface OnLocationSelectedListener {
        void locationSelected(Location location);
    }
}
