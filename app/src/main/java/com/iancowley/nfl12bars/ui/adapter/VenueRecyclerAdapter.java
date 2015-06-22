package com.iancowley.nfl12bars.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iancowley.nfl12bars.R;
import com.iancowley.nfl12bars.model.Venue;
import com.iancowley.nfl12bars.util.AddressUtil;

import java.util.List;

/**
 * Created by ian.cowley on 6/16/15.
 */
public class VenueRecyclerAdapter extends RecyclerView.Adapter<VenueRecyclerAdapter.ViewHolder> {

    private List<Venue> mVenues;
    private VenueClickedListener mVenueClickedCallbacks;

    public VenueRecyclerAdapter(List<Venue> venues, VenueClickedListener listener) {
        mVenues = venues;
        mVenueClickedCallbacks = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_venue, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);
        // Set the click listener for callbacks when a venue is selected.
        holder.itemView.setOnClickListener(mClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Venue venue = mVenues.get(i);
        // Set tag in order to access later when the item is clicked.
        viewHolder.itemView.setTag(venue);

        // Set the name and address information for this venue.
        viewHolder.name.setText(venue.getName());
        viewHolder.address.setText(AddressUtil.buildAddress(venue, true, false));
    }

    @Override
    public int getItemCount() {
        return mVenues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Venue name
        public TextView name;

        // Venue address
        public TextView address;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.venue_name);
            address = (TextView) itemView.findViewById(R.id.venue_address);
        }
    }

    public interface VenueClickedListener {
        void onVenueClicked(Venue venue);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Grab a hold of the venue from the tag set earlier.
            Venue venue = (Venue) view.getTag();
            mVenueClickedCallbacks.onVenueClicked(venue);
        }
    };
}
