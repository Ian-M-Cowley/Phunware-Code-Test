package com.iancowley.nfl12bars.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.iancowley.nfl12bars.R;

/**
 * Created by ian.cowley on 6/18/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    // Views
    private Toolbar mToolbar;

    /**
     * Child classes must provide their layout id in this method.
     *
     * @return
     */
    abstract int getCustomLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getCustomLayoutId());
        initToolbar();
    }

    /**
     * Inflates and sets the action bar if there is a toolbar in the given layout.
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }
}
