package com.labstract.lest.wallistract.GridActivities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.labstract.lest.wallistract.AppController;
import com.labstract.lest.wallistract.Category;
import com.labstract.lest.wallistract.R;
import com.labstract.lest.wallistract.Settings.SettingsActivity;

import java.util.List;

/**
 * Created by Adi on 26-01-2017.
 */
public class SecondActivity extends AppCompatActivity{
    private static final String TAG = SecondActivity.class.getSimpleName();
    private List<Category> albumsList;
    private int ImageName;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/

        // Getting the albums from shared preferences
        albumsList = AppController.getInstance().getPrefManger().getCategories();
        ImageName=getIntent().getExtras().getInt("position");
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(ImageName);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_full_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_settings:Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
                // selected wallpaper category
                // send album id to grid fragment to list all the wallpapers
                String albumId = albumsList.get(position).getId();
                fragment = GridFragment.newInstance(albumId);
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        } else {
            // error in creating fragment
            Log.e(TAG, "Error in creating fragment");
        }
    }
}
