package com.iancowley.nfl12bars.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.iancowley.nfl12bars.R;
import com.iancowley.nfl12bars.controller.VenueController;
import com.iancowley.nfl12bars.model.Venue;
import com.iancowley.nfl12bars.ui.loader.VenueFetchLoader;

import java.util.LinkedHashMap;

/**
 * Created by ian.cowley on 6/16/15.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportLoaderManager().initLoader(0, null, mLoaderCallbacks);
    }

    private LoaderManager.LoaderCallbacks<LinkedHashMap<Long, Venue>> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<LinkedHashMap<Long, Venue>>() {
        @Override
        public Loader<LinkedHashMap<Long, Venue>> onCreateLoader(int id, Bundle args) {
            return new VenueFetchLoader(SplashActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<LinkedHashMap<Long, Venue>> loader, LinkedHashMap<Long, Venue> data) {
            if (data != null) {
                // Pass the loaded venue map to the VenueController and start the list activity.
                VenueController.getInstance().setVenues(data);
                Intent intent = VenueListActivity.newIntent(SplashActivity.this);
                startActivity(intent);
                finish();
            } else {
                // If the data returned is null, let the user know the fetch failed and exit the app.
                Toast.makeText(SplashActivity.this, "Venue list fetch failed.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        public void onLoaderReset(Loader<LinkedHashMap<Long, Venue>> loader) {
            // Do nothing.
        }
    };
}
