package com.iancowley.nfl12bars.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iancowley.nfl12bars.R;
import com.iancowley.nfl12bars.controller.VenueController;
import com.iancowley.nfl12bars.model.ScheduleItem;
import com.iancowley.nfl12bars.model.Venue;
import com.iancowley.nfl12bars.util.AddressUtil;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by ian.cowley on 6/16/15.
 */
public class VenueDetailFragment extends Fragment {

    private static final String ARG_VENUE_ID = "venueId";

    private Venue mVenue;

    // Views
    private View mRoot;
    private ImageView mVenueImage;
    private TextView mVenueNameText;
    private TextView mAddressText;
    private LinearLayout mScheduleLayout;

    // Layout Inflater for use creating the schedule.
    private LayoutInflater mLayoutInflater;

    public static VenueDetailFragment newInstance(long venueId) {
        VenueDetailFragment frag = new VenueDetailFragment();
        Bundle args = new Bundle();
        // Pass the venue id to the fragment in the arguments.
        args.putLong(ARG_VENUE_ID, venueId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Grab the venue id from the arguments.
        long venueId = getArguments().getLong(ARG_VENUE_ID);

        // Retrieve the venue from the VenueController by the passed in id.
        mVenue = VenueController.getInstance().getVenueById(venueId);
        mLayoutInflater = LayoutInflater.from(getActivity());

        // This fragment will populate the toolbar options with a call and share button.
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_venue_detail, container, false);

        // Venue Image
        mVenueImage = (ImageView) mRoot.findViewById(R.id.venue_image);
        if (!TextUtils.isEmpty(mVenue.getImageUrl())) {
            Picasso.with(getActivity()).load(mVenue.getImageUrl()).placeholder(R.drawable.nfl).into(mVenueImage);
        } else {
            // Set placeholder image if the venue does not have one.
            mVenueImage.setImageResource(R.drawable.nfl);
        }

        // Venue Name
        mVenueNameText = (TextView) mRoot.findViewById(R.id.venue_name);
        mVenueNameText.setText(mVenue.getName());

        // Venue Address
        mAddressText = (TextView) mRoot.findViewById(R.id.venue_address);
        mAddressText.setText(AddressUtil.buildAddress(mVenue, false, true));

        // Venue Schedule
        mScheduleLayout = (LinearLayout) mRoot.findViewById(R.id.schedule_layout);
        if (mVenue.getSchedule() != null && mVenue.getSchedule().size() != 0) {
            mScheduleLayout.setVisibility(View.VISIBLE);
            inflateAndPopulateSchedule(mVenue.getSchedule());
        }

        return mRoot;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_venue_detail, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Hide the call button if the venue does not have a phone number.
        if (TextUtils.isEmpty(mVenue.getPhone())) {
            menu.findItem(R.id.menu_item_call).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_call:
                launchCallIntent();
                return true;
            case R.id.menu_item_share:
                launchShareIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Launches an intent with the venues phone number.
     * If the device has a dialer, it will be sent to the dialer.
     * If the device does not, it should show up to add the number to their contacts.
     */
    private void launchCallIntent() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mVenue.getPhone()));
        startActivity(intent);
    }

    /**
     * Launches an intent with simple share data of the venue's address.
     */
    private void launchShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, AddressUtil.buildAddress(mVenue, true, true));
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getResources().getText(R.string.share_title)));
    }

    /**
     * Inflates and populates the schedule for the venue if there are any schedule items.
     * <p/>
     * I choose to do this with dynamic view inflation because the items are simple TextViews
     * and there will likely not be many of them. If the items were more intricate or there were
     * significantly more of them, I would use a recycler with an adapter.
     *
     * @param scheduleItemList the list of schedule items
     */
    private void inflateAndPopulateSchedule(List<ScheduleItem> scheduleItemList) {
        for (ScheduleItem item : scheduleItemList) {
            TextView scheduleText = (TextView) mLayoutInflater.inflate(R.layout.item_schedule, mScheduleLayout, false);
            String scheduleString = buildScheduleTimeFromDates(item.getStartDate(), item.getEndDate());
            scheduleText.setText(scheduleString);
            mScheduleLayout.addView(scheduleText);
        }
    }

    /**
     * Builds a string representation of a schedule item in this form:
     * Wednesday 1/30 8:00am to 10:00pm
     *
     * @param startDate the start time
     * @param endDate   the end time
     * @return a string representation of the schedule item
     */
    private String buildScheduleTimeFromDates(Date startDate, Date endDate) {
        String scheduleString;

        // Format the start date to the form of:
        // Wednesday 1/30 8:00AM
        DateFormat format = new SimpleDateFormat("EEEE M/d K:mma", Locale.getDefault());
        format.setTimeZone(TimeZone.getDefault());
        scheduleString = format.format(startDate);

        // Append on the end date hour in form of:
        // 'to 10:00PM'
        scheduleString += " to ";
        format = new SimpleDateFormat("K:mma", Locale.getDefault());
        format.setTimeZone(TimeZone.getDefault());
        scheduleString += format.format(endDate);

        // Replace all upper case PM and AM with lower case.
        scheduleString = scheduleString.replace("AM", "am");
        scheduleString = scheduleString.replace("PM", "pm");
        return scheduleString;
    }
}
