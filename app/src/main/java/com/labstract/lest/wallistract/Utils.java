package com.labstract.lest.wallistract;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Adi on 21-01-2017.
 */
public class Utils {
    private String TAG = Utils.class.getSimpleName();
    private Context _context;
    private PrefManager pref;

    // constructor
    public Utils(Context context) {
        this._context = context;
        pref = new PrefManager(_context);
    }


    /*
     * getting screen width
     */
    @SuppressWarnings("deprecation")
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (NoSuchMethodError ignore) {
            // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }

    public void saveImageToSDCard(Bitmap bitmap,String ImageName) {
        File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), pref.getGalleryName());

        myDir.mkdirs();
        String fname = ImageName + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Handler handler = new Handler(Looper.getMainLooper());

            handler.post(new Runnable() {

                @Override
                public void run() {
                    try {
                        Toast.makeText(_context, _context.getString(R.string.toast_saved).replace("#", "\"" + pref.getGalleryName() + "\""),
                                Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, "Wallpaper saved to: " + file.getAbsolutePath());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(_context,
                                _context.getString(R.string.toast_saved_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                    }});

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(_context,
                    _context.getString(R.string.toast_saved_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void setAsWallpaper(final Bitmap bitmap,final Activity activity) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {


                try {
                    WallpaperManager wm = WallpaperManager.getInstance(_context);
                    DisplayMetrics metrics = new DisplayMetrics();
                    WindowManager wms = (WindowManager) activity.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                    wms.getDefaultDisplay().getMetrics(metrics);
                    int height = metrics.heightPixels;
                    int width = metrics.widthPixels;
                  //  Bitmap bitmaps = Bitmap.createScaledBitmap(bitmap, width, height, true);

                    wm.setBitmap(bitmap);
                    Toast.makeText(_context,
                            _context.getString(R.string.toast_wallpaper_set),
                            Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(_context,
                            _context.getString(R.string.toast_wallpaper_set_failed),
                            Toast.LENGTH_SHORT).show();
                }

                //Your UI code here
            }
        });
    }
}
