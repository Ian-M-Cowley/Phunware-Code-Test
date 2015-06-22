package com.iancowley.nfl12bars.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iancowley.nfl12bars.R;
import com.iancowley.nfl12bars.controller.VenueController;
import com.iancowley.nfl12bars.model.Venue;
import com.iancowley.nfl12bars.ui.adapter.VenueRecyclerAdapter;
import com.iancowley.nfl12bars.ui.adapter.VenueRecyclerAdapter.VenueClickedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ian.cowley on 6/16/15.
 */
public class VenueListFragment extends Fragment {

    // Views
    private View mRoot;
    private RecyclerView mVenueRecycler;

    // Callbacks
    private VenueListFragmentListener mCallbacks;

    // Venue List Adapter
    private VenueRecyclerAdapter mVenueRecyclerAdapter;

    private List<Venue> mVenueList;

    public interface VenueListFragmentListener {
        void onVenueClicked(Venue venue);
    }

    public static VenueListFragment newInstance() {
        VenueListFragment frag = new VenueListFragment();
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof VenueListFragmentListener) {
            mCallbacks = (VenueListFragmentListener) activity;
        } else {
            throw new ClassCastException("Activity " + activity.getClass().getCanonicalName()
                    + " must implement " + VenueListFragmentListener.class.getCanonicalName());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pull the map of venues from the VenueController and convert it into a list
        // for our VenueRecyclerAdapter.
        HashMap<Long, Venue> venueMap = VenueController.getInstance().getVenues();
        mVenueList = new ArrayList<>(venueMap.values());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_venue_list, container, false);

        // Populate Venue List.
        mVenueRecycler = (RecyclerView) mRoot.findViewById(R.id.venue_list);
        mVenueRecyclerAdapter = new VenueRecyclerAdapter(mVenueList, mVenueClickedListener);
        mVenueRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mVenueRecycler.setAdapter(mVenueRecyclerAdapter);

        return mRoot;
    }

    private VenueClickedListener mVenueClickedListener = new VenueClickedListener() {
        @Override
        public void onVenueClicked(Venue venue) {
            // Pass through callbacks to activity.
            mCallbacks.onVenueClicked(venue);
        }
    };
}
