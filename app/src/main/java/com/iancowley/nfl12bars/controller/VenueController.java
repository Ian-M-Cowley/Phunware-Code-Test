package com.iancowley.nfl12bars.controller;

import com.iancowley.nfl12bars.model.Venue;

import java.util.LinkedHashMap;

/**
 * Created by ian.cowley on 6/16/15.
 */
public class VenueController {

    public static final String TAG = VenueController.class.getSimpleName();

    // We use a LinkedHashMap here to keep insertion order but ensure quick access by id.
    private LinkedHashMap<Long, Venue> mVenues = new LinkedHashMap<>();

    private static class SingletonHolder {
        public static final VenueController sInstance = new VenueController();
    }

    public VenueController() {
    }

    /**
     * Returns the singleton instance of this VenueController.
     *
     * @return the singleton instance of this VenueController
     */
    public static VenueController getInstance() {
        return SingletonHolder.sInstance;
    }

    /**
     * Returns the entire map of Venues.
     *
     * @return the map of Venues
     */
    public LinkedHashMap<Long, Venue> getVenues() {
        return mVenues;
    }

    /**
     * Sets the map of Venues after they are parsed from the feed.
     *
     * @param venues the map of Venues
     */
    public void setVenues(LinkedHashMap<Long, Venue> venues) {
        mVenues = venues;
    }

    /**
     * Returns a Venue from the map of Venues with the given ID.
     *
     * @param id the ID of the venue
     * @return the Venue with the corresponding ID
     */
    public Venue getVenueById(long id) {
        return mVenues.get(id);
    }
}
