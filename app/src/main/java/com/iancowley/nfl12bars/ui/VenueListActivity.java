package com.iancowley.nfl12bars.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.iancowley.nfl12bars.R;
import com.iancowley.nfl12bars.controller.VenueController;
import com.iancowley.nfl12bars.model.Venue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ian.cowley on 6/16/15.
 */
public class VenueListActivity extends BaseActivity implements VenueListFragment.VenueListFragmentListener {

    private static final String FRAG_VENUE_LIST = "venueList";
    private static final String FRAG_VENUE_DETAIL = "venueDetail";

    private boolean mIsDualPane;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, VenueListActivity.class);
        return intent;
    }

    @Override
    int getCustomLayoutId() {
        return R.layout.activity_venue_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If there is the second frame present in the layout, we are in dual pane mode on a tablet.
        if (findViewById(R.id.dual_frame) != null) {
            mIsDualPane = true;
        }

        if (savedInstanceState == null) {
            // Inflate the list fragment into the first frame.
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, VenueListFragment.newInstance(), FRAG_VENUE_LIST)
                    .commit();
            // If we are in dual pane mode, inflate the detail fragment into the second frame.
            if (mIsDualPane) {
                List<Venue> venues = new ArrayList<>(VenueController.getInstance().getVenues().values());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.dual_frame, VenueDetailFragment.newInstance(venues.get(0).getId()), FRAG_VENUE_DETAIL)
                        .commit();
            }
        }
    }

    @Override
    public void onVenueClicked(Venue venue) {
        // If we are in dual pane mode, replace the current detail fragment with a new instance that
        // is for the selected venue.
        if (mIsDualPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dual_frame, VenueDetailFragment.newInstance(venue.getId()), FRAG_VENUE_DETAIL)
                    .commit();
        } else { // If we are in single pane mode, launch the detail activity.
            Intent intent = VenueDetailActivity.newIntent(this, venue.getId());
            startActivity(intent);
        }
    }
}
