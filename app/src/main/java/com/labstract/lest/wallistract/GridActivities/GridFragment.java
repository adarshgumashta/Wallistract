package com.labstract.lest.wallistract.GridActivities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.labstract.lest.wallistract.AppConst;
import com.labstract.lest.wallistract.AppController;
import com.labstract.lest.wallistract.FullScreenViewSlider.FullScreenActivity;
import com.labstract.lest.wallistract.GridViewAdapter;
import com.labstract.lest.wallistract.PrefManager;
import com.labstract.lest.wallistract.R;
import com.labstract.lest.wallistract.Settings.SettingsActivity;
import com.labstract.lest.wallistract.Utils;
import com.labstract.lest.wallistract.Wallpaper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adi on 21-01-2017.
 */
public class GridFragment extends Fragment {
    Activity context;
    private static final String TAG = GridFragment.class.getSimpleName();
    private Utils utils;
    private GridViewAdapter adapter;
    private GridView gridView;
    private int columnWidth;
    private static final String bundleAlbumId = "albumId";
    private String selectedAlbumId;
    private List < Wallpaper > photosList;
    private ProgressBar pbLoader;
    private PrefManager pref;
    private ArrayList < Image > images;
    private Button Settings;
    private AdView mAdView;
    public GridFragment() {}

    public static GridFragment newInstance(String albumId) {
        GridFragment f = new GridFragment();
        Bundle args = new Bundle();
        args.putString(bundleAlbumId, albumId);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        photosList = new ArrayList <Wallpaper> ();
        pref = new PrefManager(getActivity());

        images = new ArrayList < > ();
        // Getting Album Id of the item selected in navigation drawer
        // if Album Id is null, user is selected recently added option
        if (getArguments().getString(bundleAlbumId) != null) {
            selectedAlbumId = getArguments().getString(bundleAlbumId);
            Log.d(TAG, "Selected album id: " + getArguments().getString(bundleAlbumId));
        } else {
            Log.d(TAG, "Selected recently added album");
            selectedAlbumId = null;
        }

        View rootView = inflater.inflate(R.layout.fragment_grid, container, false);

        // Hiding the gridview and showing loader image before making the http
        // request
        gridView = (GridView) rootView.findViewById(R.id.grid_view);
        gridView.setVisibility(View.GONE);
        pbLoader = (ProgressBar) rootView.findViewById(R.id.pbLoader);
        pbLoader.setVisibility(View.VISIBLE);
        utils = new Utils(getActivity());
        mAdView = (AdView)rootView.findViewById(R.id.adView);
        AdRequest adRequest =new  AdRequest.Builder()
                .addTestDevice("F88406130EA46EEC7EDF0051D6D39EDE")
                .build();
        mAdView.loadAd(adRequest);



        //Making volley's json object request to fetch list of photos from an album
        /*For Fetching Photos */
        // Picasa request to get list of photos
        String url = "https://api.flickr.com/services/rest/?method=flickr.photosets.getPhotos&api_key=481497b7c813f81b3fbf613f1c9783fb&photoset_id="+selectedAlbumId+"&user_id=153679059@N08&format=json";
        Log.d(TAG, "Albums request url: " + url);
        StringRequest getRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Log.d(TAG, "List of photos json reponse: " + response.toString());
                String s=response.replace("jsonFlickrApi(","");
                String snew=s.replace(")","");
                try
                {
                    JSONObject jsonObject=new JSONObject(snew);
                    Log.d(TAG, "Albums Response: " + jsonObject);
                    JSONArray entry =jsonObject.getJSONObject("photoset").getJSONArray("photo");
                    images.clear();
                    for (int i = 0; i < entry.length(); i++) {
                        JSONObject albumObj = (JSONObject) entry.get(i);
                        Image image = new Image();
                        String titleOfImage = albumObj.getString("title");
                        image.setName(titleOfImage);
                        String photoID=albumObj.getString("id");
                        String secretID=albumObj.getString("secret");
                        String farmID=albumObj.getString("farm");
                        String serverID=albumObj.getString("server");
                        String imageUrl="https://farm"+farmID+".staticflickr.com/"+serverID+"/"+photoID+"_"+secretID+"_b.jpg";
                        image.setLarge(imageUrl);
                        Wallpaper p=new Wallpaper(imageUrl);
                        images.add(image);
                        photosList.add(p);
                        Log.d(TAG, "Image Title: " + titleOfImage+ ", Image URl: " + imageUrl);
                    }
                    // Notify list adapter about dataset changes. So
                    // that it renders grid again
                    adapter.notifyDataSetChanged();
                    // Hide the loader, make grid visible
                    pbLoader.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Photo Fetch error: " + error.toString());
            }
        });
        // Remove the url from cache
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        // Disable the cache for this url, so that it always fetches updated json
        getRequest.setShouldCache(false);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(getRequest);
        // Initilizing Grid View
        InitializeGridLayout();
        // Gridview adapter
        adapter = new GridViewAdapter(getActivity(), photosList, columnWidth);
        // setting grid view adapter
        gridView.setAdapter(adapter);
        // Grid item select listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView < ? > parent, View v,
                                    int position, long id) {
                Bundle bundle = new Bundle();
                Intent intents=new Intent(getActivity(), FullScreenActivity.class);

                bundle.putSerializable("images", images);
                bundle.putInt("position", position);
                intents.putExtras(bundle);
                startActivity(intents);
            }
        });
        return rootView;
    }


        //Method to calculate the grid dimensions Calculates number columns and columns width in grid

        private void InitializeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConst.GRID_PADDING, r.getDisplayMetrics());

        // Column width
        columnWidth = (int)((utils.getScreenWidth() - ((pref
                .getNoOfGridColumns() + 1) * padding)) / pref
                .getNoOfGridColumns());

        // Setting number of grid columns
        gridView.setNumColumns(pref.getNoOfGridColumns());
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);

        // Setting horizontal and vertical padding
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

}