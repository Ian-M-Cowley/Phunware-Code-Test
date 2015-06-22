package com.iancowley.nfl12bars.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.iancowley.nfl12bars.R;

/**
 * Created by ian.cowley on 6/16/15.
 */
public class VenueDetailActivity extends BaseActivity {

    private static final String FRAG_VENUE_DETAIL = "venueDetail";
    private static final String ARG_VENUE_ID = "venueId";

    private long mVenueId;

    public static Intent newIntent(Context context, long venueId) {
        Intent intent = new Intent(context, VenueDetailActivity.class);
        intent.putExtra(ARG_VENUE_ID, venueId);
        return intent;
    }

    @Override
    int getCustomLayoutId() {
        return R.layout.activity_venue_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Allow toolbar home button to be displayed.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // Inflate the VenueDetailFragment into its container.
        if (savedInstanceState == null) {
            mVenueId = getIntent().getExtras().getLong(ARG_VENUE_ID);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, VenueDetailFragment.newInstance(mVenueId), FRAG_VENUE_DETAIL)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // If the user hits the toolbar back button, finish this activity.
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
