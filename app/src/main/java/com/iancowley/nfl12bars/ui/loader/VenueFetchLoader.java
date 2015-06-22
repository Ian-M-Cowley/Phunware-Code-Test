package com.iancowley.nfl12bars.ui.loader;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.iancowley.nfl12bars.model.Venue;
import com.iancowley.nfl12bars.ui.adapter.VenueGsonAdapter;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Created by ian.cowley on 6/18/15.
 */
public class VenueFetchLoader extends android.support.v4.content.AsyncTaskLoader<LinkedHashMap<Long, Venue>> {

    public static final String TAG = VenueFetchLoader.class.getSimpleName();

    private static final String VENUE_JSON_URL = "https://s3.amazonaws.com/jon-hancock-phunware/nflapi-static.json";

    private LinkedHashMap<Long, Venue> mVenueMap;

    public VenueFetchLoader(Context context) {
        super(context);
    }

    @Override
    public LinkedHashMap<Long, Venue> loadInBackground() {
        String venueJson = fetchVenueJson();
        mVenueMap = buildVenueMapFromJson(venueJson);
        return mVenueMap;
    }

    @Override
    protected void onStartLoading() {
        // Deliver a result if we have one.
        if (mVenueMap != null) {
            deliverResult(mVenueMap);
        } else { // Load if we do not.
            forceLoad();
        }
    }

    private String fetchVenueJson() {
        OkHttpClient client = new OkHttpClient();
        try {
            // Download the venue list from the hosted json.
            Request request = new Request.Builder().url(VENUE_JSON_URL).build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e(TAG, "Error fetching venue json", e);
            return null;
        }
    }

    private LinkedHashMap<Long, Venue> buildVenueMapFromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            LinkedHashMap<Long, Venue> venueMap = new LinkedHashMap<>();

            // Build our Gson parser.
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Venue.class, new VenueGsonAdapter());
            Gson gson = builder.create();

            // Parse the json.
            JsonParser parser = new JsonParser();
            JsonElement root = parser.parse(json);
            if (root.isJsonArray()) {
                // Grab the array of Venues.
                JsonArray venueArray = root.getAsJsonArray();
                for (int i = 0; i < venueArray.size(); i++) {
                    // Parse each individual Venue.
                    JsonElement venueElement = venueArray.get(i);
                    Venue venue = gson.fromJson(venueElement, Venue.class);
                    venueMap.put(venue.getId(), venue);
                }
            }
            return venueMap;
        }
    }
}
