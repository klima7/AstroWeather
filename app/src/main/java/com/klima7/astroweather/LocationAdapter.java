package com.klima7.astroweather;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.klima7.astroweather.weather.Entry;
import com.klima7.astroweather.weather.Location;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.WeatherHolder> {

    private Entry entry;
    private List<Entry> entries;
    private OnLocationSelectedListener listener;

    public LocationAdapter(List<Entry> entries, OnLocationSelectedListener listener) {
        this.entries = entries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.location_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHolder holder, int position) {
        Entry entry = entries.get(position);
        holder.update(entry);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setEntry(entry);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
        notifyDataSetChanged();
    }

    public void removeEntry(Entry entry) {
        entries.remove(entry);
        notifyDataSetChanged();
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public class WeatherHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private TextView cityView, regionView, latitudeView, longitudeView;
        private CardView locationCard;

        public WeatherHolder(@NonNull View itemView) {
            super(itemView);
            cityView = itemView.findViewById(R.id.entry_city);
            regionView = itemView.findViewById(R.id.entry_region);
            latitudeView = itemView.findViewById(R.id.entry_latitude);
            longitudeView = itemView.findViewById(R.id.entry_longitude);
            locationCard = itemView.findViewById(R.id.location_card);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void update(Entry entry) {
            Location location = entry.getLocation();
            cityView.setText(location.getCity());
            regionView.setText(location.getRegion().trim());
            latitudeView.setText(String.valueOf(location.getLatitude()));
            longitudeView.setText(String.valueOf(location.getLongitude()));
            locationCard.setOnClickListener(v -> listener.locationSelected(entry));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Akcja");
            menu.add(0, v.getId(), 0, "Usuń");
        }
    }

    public interface OnLocationSelectedListener {
        void locationSelected(Entry entry);
    }
}
