package com.labstract.lest.wallistract.FullScreenViewSlider;

import android.app.DialogFragment;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import com.labstract.lest.wallistract.GridActivities.GridFragment;
import com.labstract.lest.wallistract.GridActivities.Image;
import com.labstract.lest.wallistract.R;
import com.labstract.lest.wallistract.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by Adi on 26-01-2017.
 */
public class SlideshowDialogFragment extends DialogFragment implements CropImageView.OnCropImageCompleteListener  {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<Image> images;
    Utils utils;
    Bitmap bitmap;
    private ViewPager viewPager;
    private  ImageView fullImageView;

    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;
    private LayoutInflater layoutInflaterforimage;
    private LinearLayout llSetWallpaper, llDownloadWallpaper;
   public static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }
    GridFragment gridFragment;
    private CropImageView mCropImageView;

    @Override
    public View onCreateView(LayoutInflater inflater,final ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) v.findViewById(R.id.title);
        lblDate = (TextView) v.findViewById(R.id.date);
       // llSetWallpaper = (LinearLayout)v.findViewById(R.id.llSetWallpaper);
       // llDownloadWallpaper = (LinearLayout)v.findViewById(R.id.llDownloadWallpaper);
        //fullImageView=(ImageView)v.findViewById(R.id.fullImageView);
        images = (ArrayList<Image>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + images.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
      //  Toolbar toolbar = (Toolbar) v.findViewById(R.id.my_toolbar);
        utils = new Utils(getActivity().getApplicationContext());
        View decorView =getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        // setting layout buttons alpha/opacity
        llSetWallpaper.getBackground().setAlpha(70);
        llDownloadWallpaper.getBackground().setAlpha(70);


        llSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridFragment=new GridFragment();
                final Image image1=images.get(viewPager.getCurrentItem());
                AsyncTask<Void,Void,Void> my_task=new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Looper.prepare();
                        try
                        {
                            DisplayMetrics metrics = new DisplayMetrics();
                            WindowManager wm = (WindowManager) getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                            wm.getDefaultDisplay().getMetrics(metrics);
                            int height = metrics.heightPixels;
                            int width = metrics.widthPixels;

                            WallpaperManager wms = WallpaperManager.getInstance(getActivity().getApplicationContext());

                            wms.suggestDesiredDimensions(width , height);

                            Bitmap bitmaps = Glide.with(getActivity()).load(image1.getLarge()).asBitmap().into(height,width).get();
                            Bitmap bitmap = Bitmap.createScaledBitmap(bitmaps, width, height, true);

                     //       Intent intent=new Intent(getActivity(),crop.class);
                       //     startActivity(intent);
                            Uri imageUri;
/*
                            try {
                                Bitmap bitmapscrop = Glide.with(getActivity()).load(image1.getLarge()).asBitmap().into(-1,-1).get();
                                File file = new File(getActivity().getFilesDir(), "Image"
                                        + new Random().nextInt() + ".jpeg");
                                FileOutputStream out = getActivity().openFileOutput(file.getName(),
                                        Context.MODE_WORLD_READABLE);
                                bitmapscrop.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                out.flush();
                                out.close();
                                //get absolute path
                                String realPath = file.getAbsolutePath();
                                File f = new File(realPath);
                                imageUri = Uri.fromFile(f);
                              //  CropImage.activity(imageUri).start(getActivity());
                                        Intent intent = CropImage.activity(imageUri)
                                        .setGuidelines(CropImageView.Guidelines.ON)
                                        .setFixAspectRatio(true)
                                        .getIntent(getActivity());
                                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

                            } catch (Exception e) {
                                Log.e("Your Error Message", e.getMessage());
                            }
*/

                          /*  Uri uri=Uri.parse("https://s3.amazonaws.com/images.seroundtable.com/google-donald-trump-1466511123.jpg");
                            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.addFlags(1);
                            intent.setDataAndType(uri, "image*//*");
                            intent.putExtra("mimeType", "image*//*");
                            startActivity(Intent.createChooser(intent, "Set as:"));*/
                            //  utils.setAsWallpaper(bitmap,getActivity());
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

            }
        });

        llDownloadWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Image image2=images.get(viewPager.getCurrentItem());

                AsyncTask<Void,Void,Void> my_tasks=new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Looper.prepare();
                        try
                        {
                            Bitmap bitmaps = Glide.with(getActivity()).load(image2.getLarge()).asBitmap().into(512, 492).get();
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
        setCurrentItem(selectedPosition);
        return v;
    }

        private void setCurrentItem(int position) {
            viewPager.setCurrentItem(position, false);
            displayMetaInfo(selectedPosition);
        }


        //  page change listener
        ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

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
            lblDate.setText(image.getTimestamp());
        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {
        public    Bitmap jk=null;
        private LayoutInflater layoutInflater;
        Image image ;
        ImageView imageViewPreview;
        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            image = images.get(position);
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager wms = (WindowManager)getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            wms.getDefaultDisplay().getMetrics(metrics);
            final int height = metrics.heightPixels;
            final int width = metrics.widthPixels;


            Glide.with(getActivity()).load(image.getLarge()).asBitmap().into(new BitmapImageViewTarget(imageViewPreview) {
                @Override
                protected void setResource(Bitmap resource) {
                    // Do bitmap magic here
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

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        handleCropResult(result);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            handleCropResult(result);
        }
    }


    private void handleCropResult(CropImageView.CropResult result) {
        if (result.getError() == null) {
            if (result.getUri() != null) {
                Toast.makeText(getActivity().getApplicationContext(), "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e("AIC", "Failed to crop image", result.getError());
            Toast.makeText(getActivity(), "Image crop failed: " + result.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }



}
