package com.iancowley.nfl12bars.util;

import android.text.TextUtils;

import com.iancowley.nfl12bars.model.Venue;

/**
 * Created by ian.cowley on 6/18/15.
 */
public class AddressUtil {

    /**
     * Builds an address for a venue with the given parameters.
     *
     * @param venue      the venue to build the address for
     * @param singleLine if the address should be a single line or two lines
     * @param includeZip if the zipcode should be included in the address
     * @return the formatted address
     */
    public static String buildAddress(Venue venue, boolean singleLine, boolean includeZip) {
        String address = "";
        // If the address is present, include it.
        if (!TextUtils.isEmpty(venue.getAddress())) {
            address += venue.getAddress();
            if (singleLine) { // If one line is requested, add a comma after the address.
                address += ", ";
            } else { // If two lines are requested, add in a line break.
                address += "\n";
            }
        }
        // If the city is present, include it.
        if (!TextUtils.isEmpty(venue.getCity())) {
            address += venue.getCity() + ", ";
        }
        // If the state is present, include it.
        if (!TextUtils.isEmpty(venue.getState())) {
            address += venue.getState();
        }
        // If the zipcode is requested and present, include it.
        if (includeZip && !TextUtils.isEmpty(venue.getZip())) {
            address += " " + venue.getZip();
        }
        return address;
    }
}
