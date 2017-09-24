package com.labstract.lest.wallistract.Settings;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.labstract.lest.wallistract.AppController;
import com.labstract.lest.wallistract.GridActivities.Image;
import com.labstract.lest.wallistract.PrefManager;
import com.labstract.lest.wallistract.R;
import com.labstract.lest.wallistract.Wallpaper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by Adi on 28-01-2017.
 */
public class AlarmReceiver extends BroadcastReceiver {
    int randomphoto;
    ArrayList<String> retrieveSelectedAlbum=new ArrayList<>();
    ArrayList<String> albumsselected=new ArrayList<>();
    ArrayList<String> retrieveimages=new ArrayList<>();
    private PrefManager pref;
    String url = "";
    int[] covers = new int[]{

    };
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        pref=new PrefManager(arg0);
        Toast.makeText(arg0, "Alarm received!", Toast.LENGTH_LONG).show();
        if ("android.intent.action.DATE_CHANGED".equals(arg1.getAction()) || "Intent.ACTION_TIME_CHANGED".equals(arg1.getAction())) {
            retrieveSelectedAlbum=pref.getSelectedAlbum();
            albumsselected.clear();
            retrieveimages.clear();
            if(retrieveSelectedAlbum!=null)
            {
                if(retrieveSelectedAlbum.contains("stockSnap"))
                {
                    albumsselected.add("72157679197565753");
                }
                if(retrieveSelectedAlbum.contains("isoRepublic"))
                {
                    albumsselected.add("72157682945749226");
                }
                if(retrieveSelectedAlbum.contains("Kaboom"))
                {
                    albumsselected.add("72157680421362833");
                }
                if(retrieveSelectedAlbum.contains("pexels"))
                {
                    albumsselected.add("72157680333764444");
                }
                if(retrieveSelectedAlbum.contains("Pixabay"))
                {
                    albumsselected.add("72157680791784062");
                }
                if(retrieveSelectedAlbum.contains("SplitShare"))
                {
                    albumsselected.add("72157680421440423");
                }
                if(retrieveSelectedAlbum.contains("tommy"))
                {
                    albumsselected.add("72157681402959311");
                }

                for(int i=0;i<albumsselected.size();i++)
                {
                  url="https://api.flickr.com/services/rest/?method=flickr.photosets.getPhotos&api_key=481497b7c813f81b3fbf613f1c9783fb&photoset_id="+albumsselected.get(i)+"&user_id=153679059@N08&format=json";
                    StringRequest getRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {
                            String s=response.replace("jsonFlickrApi(","");
                            String snew=s.replace(")","");
                            try
                            {
                                JSONObject jsonObject=new JSONObject(snew);
                                JSONArray entry =jsonObject.getJSONObject("photoset").getJSONArray("photo");
                                for (int i = 0; i < entry.length(); i++) {
                                    JSONObject albumObj = (JSONObject) entry.get(i);
                                    String photoID=albumObj.getString("id");
                                    String secretID=albumObj.getString("secret");
                                    String farmID=albumObj.getString("farm");
                                    String serverID=albumObj.getString("server");
                                    String imageUrl="https://farm"+farmID+".staticflickr.com/"+serverID+"/"+photoID+"_"+secretID+"_b.jpg";
                                    retrieveimages.add(imageUrl);
                                }
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Log.d(TAG, "Photo Fetch error: " + error.toString());
                        }
                    });
                    AppController.getInstance().addToRequestQueue(getRequest);
                }
                randomphoto = GenerateRandom();
                DisplayMetrics metrics = new DisplayMetrics();
                WindowManager wm = (WindowManager)arg0.getSystemService(Context.WINDOW_SERVICE);
                wm.getDefaultDisplay().getMetrics(metrics);
                int height = metrics.heightPixels;
                int width = metrics.widthPixels;
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(arg0);
                try {
                    Bitmap bitmaps = Glide.with(arg0).load(retrieveimages.get(randomphoto)).asBitmap().into(height, width).get();
                    wallpaperManager.setBitmap(bitmaps);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }
    public int GenerateRandom()
    {
        Random random = new Random();
        int randomNumber = random.nextInt((retrieveimages.size()-0)+1)+0;
        return randomNumber;
    }

}

