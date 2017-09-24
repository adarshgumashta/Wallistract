package com.labstract.lest.wallistract.FullScreenViewSlider;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.labstract.lest.wallistract.CropActivities.crop;
import com.labstract.lest.wallistract.GridActivities.GridFragment;
import com.labstract.lest.wallistract.GridActivities.Image;
import com.labstract.lest.wallistract.PrefManager;
import com.labstract.lest.wallistract.R;
import com.labstract.lest.wallistract.Settings.SettingsActivity;
import com.labstract.lest.wallistract.Utils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class FullScreenActivity extends Activity {
    private String TAG = FullScreenActivity.class.getSimpleName();
    private ArrayList<Image> images;
    Utils utils;
    Bitmap bitmap;
    private ViewPager viewPager;
    private ImageView fullImageView;
    FloatingActionButton slideshow,SaveWallpaper,SetWallpaper,ShareImage;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate;
    GridFragment gridFragment;
    PrefManager pref;
    InterstitialAd mInterstitialAd;
    private int selectedPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setIcon(R.color.transparent);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        lblCount = (TextView) findViewById(R.id.lbl_count);
        lblTitle = (TextView) findViewById(R.id.title);
        lblDate = (TextView) findViewById(R.id.date);
        Bundle b=getIntent().getExtras();
        images = (ArrayList<Image>) b.getSerializable("images");
        selectedPosition = b.getInt("position");
        pref = new PrefManager(getApplicationContext());
        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + images.size());
       final   AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        utils = new Utils(getApplicationContext());
        View decorView =getWindow().getDecorView();
    //    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
     //   decorView.setSystemUiVisibility(uiOptions);

        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

        SetWallpaper=(FloatingActionButton) findViewById(R.id.fab1);
        slideshow = (FloatingActionButton) findViewById(R.id.fab2);
        ShareImage = (FloatingActionButton) findViewById(R.id.fab3);
        SaveWallpaper=(FloatingActionButton) findViewById(R.id.fab4);
        slideshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "SlideShow Starts", Snackbar.LENGTH_LONG).show();
                final Handler mHandler = new Handler();
                // Create runnable for posting
                final Runnable mUpdateResults = new Runnable() {
                    public void run() {
                    setCurrentItem(selectedPosition);
                    selectedPosition++;
                    }
                };
                int delay = 1000; // delay for 1 sec.
                int period = 1500; // repeat every 4 sec.
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        mHandler.post(mUpdateResults);
                    }
                }, delay, period);

            }
        });
        SaveWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Image image2=images.get(viewPager.getCurrentItem());

                AsyncTask<Void,Void,Void> my_tasks=new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Looper.prepare();
                        try
                        {
                            DisplayMetrics metrics = new DisplayMetrics();
                            WindowManager wms = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                            wms.getDefaultDisplay().getMetrics(metrics);
                            final int heights = metrics.heightPixels;
                            final int widths = metrics.widthPixels;
                            Bitmap bitmaps = Glide.with(getApplicationContext()).load(image2.getLarge()).asBitmap().into(widths,heights).get();
                            utils.saveImageToSDCard(bitmaps,image2.getName());
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        catch (ExecutionException e)
                        {
                            e.printStackTrace();
                        }
                        return  null;
                    }
                };
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    my_tasks.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
                else
                    my_tasks.execute((Void[])null);

            }
        });
        SetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridFragment=new GridFragment();
                final Image image1=images.get(viewPager.getCurrentItem());
                AsyncTask<Void,Void,Void> my_task=new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Looper.prepare();
                        try {
                            DisplayMetrics metrics = new DisplayMetrics();
                            WindowManager wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                            wm.getDefaultDisplay().getMetrics(metrics);
                            int height = metrics.heightPixels;
                            int width = metrics.widthPixels;
                            WallpaperManager wms = WallpaperManager.getInstance(getApplicationContext());
                            wms.suggestDesiredDimensions(width, height);

                            Bitmap bitmaps = Glide.with(getApplicationContext()).load(image1.getLarge()).asBitmap().into(height, width).get();
                            Bitmap bitmap = Bitmap.createScaledBitmap(bitmaps, width, height, true);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                            byte[] byteArray = stream.toByteArray();
                            Intent intent = new Intent(getApplicationContext(), crop.class);
                            intent.putExtra("imageArray",byteArray);
                            startActivity(intent);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        catch (ExecutionException e)
                        {
                            e.printStackTrace();
                        }
                        return  null;
                    }
                };
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    my_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
                else
                    my_task.execute((Void[])null);
/*                LayoutInflater factory = LayoutInflater.from(FullScreenActivity.this);
                final View deleteDialogView = factory.inflate(R.layout.choosesetwallpaper, null);
                deleteDialog.setView(deleteDialogView);
                deleteDialogView.findViewById(R.id.radioButton1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //your business logic
                        Toast.makeText(getApplicationContext(),"Hello", Toast.LENGTH_SHORT).show();
                        deleteDialog.dismiss();
                    }
                });
                deleteDialogView.findViewById(R.id.radioButton2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Hello1", Toast.LENGTH_SHORT).show();
                        deleteDialog.dismiss();
                    }
                });

                deleteDialog.show();*/

            }
        });
        ShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Image image3 = images.get(viewPager.getCurrentItem());

                AsyncTask<Void, Void, Void> my_task = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Looper.prepare();
                        try {
                            Bitmap sharebitmap = null;
                            sharebitmap = Glide.with(getApplicationContext()).load(image3.getLarge()).asBitmap().into(-1, -1).get();
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("image/jpeg");
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            sharebitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            String path = MediaStore.Images.Media.insertImage(getContentResolver(), sharebitmap, "Title", null);
                            Uri imageUri = Uri.parse(path);
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                            startActivity(Intent.createChooser(sharingIntent, "Select"));

                            /*DisplayMetrics metrics = new DisplayMetrics();
                            WindowManager wms = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                            wms.getDefaultDisplay().getMetrics(metrics);
                            final int heights = metrics.heightPixels;
                            final int widths = metrics.widthPixels;
                            Bitmap bitmaps = Glide.with(getApplicationContext()).load(image3.getLarge()).asBitmap().into(widths,heights).get();
                            utils.saveImageToSDCard(bitmaps,image3.getName());
                            String path = Environment.getExternalStorageDirectory()+"/"+ pref.getGalleryName()+"/"+image3.getName()+".jpg";
                            File files=new File(path);
                            Uri imageUri = Uri.fromFile(files);

                            if (Build.VERSION.SDK_INT >= 21) {
                                try {
                                //    startActivity(WallpaperManager.getInstance(getApplicationContext()).getCropAndSetWallpaperIntent(getContentUri(getApplicationContext(),files)));
                                    final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
                                    final String orderBy = MediaStore.Images.Media._ID;
                                    //Stores all the images from the gallery in Cursor
                                    Cursor cursor = getContentResolver().query(
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                                            null, orderBy);
                                    //Total number of images
                                    int count = cursor.getCount();

                                    //Create an array to store path to all the images
                                    String[] arrPath = new String[count];

                                    for (int i = 0; i < count; i++) {
                                        cursor.moveToPosition(i);
                                        int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                                        //Store the path of the image
                                        arrPath[i]= cursor.getString(dataColumnIndex);
                                        Log.i("PATH", arrPath[i]);
                                    }
                                } catch (IllegalArgumentException e2) {
                                    e2.printStackTrace();
                                }
                            }
                            Intent intent = new Intent("android.intent.action.ATTACH_DATA");
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setDataAndType(imageUri, "image*//*");
                            intent.putExtra("mimeType", "image*//*");
                            startActivity(Intent.createChooser(intent, "Set As:"));*/
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    my_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
                else
                    my_task.execute((Void[]) null);
            }
        });
        setCurrentItem(selectedPosition);
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public static Uri getContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Uri photoUri = Images.Media.getContentUri("internal");
        String[] projection = new String[]{"_id"};
        //Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ", new String[]{filePath}, null);
        Cursor cursor =context.getContentResolver().query(photoUri, projection, "_data LIKE ?", new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("contnt://media/external/audio/media");
            return Uri.withAppendedPath(baseUri, "" + id);

        }
        return null;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }
    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener()
    {
        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + images.size());
        Image image = images.get(position);
        lblTitle.setText(image.getName());
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



    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter
    {
        public    Bitmap jk=null;
        private LayoutInflater layoutInflater;
        Image image ;
        ImageView imageViewPreview;
        public MyViewPagerAdapter() {
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);
            imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);
           // imageViewPreview.setAdjustViewBounds(true);
           // imageViewPreview.setScaleType(ImageView.ScaleType.FIT_XY);
            Animation rotateimage = AnimationUtils.loadAnimation(getApplicationContext() , android.R.anim.fade_in);
            imageViewPreview.startAnimation(rotateimage);
            image = images.get(position);
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager wms = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            wms.getDefaultDisplay().getMetrics(metrics);
            final int height = metrics.heightPixels;
            final int width = metrics.widthPixels;
            Glide.with(getApplicationContext()).load(image.getLarge()).asBitmap().into(new BitmapImageViewTarget(imageViewPreview) {
                @Override
                protected void setResource(Bitmap resource) {
                    Bitmap bitmaps = Bitmap.createScaledBitmap(resource, width, height, true);
                    super.setResource(bitmaps);
                    jk=resource;
                }
            });
            container.addView(view);
            return view;
        }
        @Override
        public int getCount() {
            return images.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
