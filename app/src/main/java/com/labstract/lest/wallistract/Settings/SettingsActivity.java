package com.labstract.lest.wallistract.Settings;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.labstract.lest.wallistract.CardView.MainActivity;
import com.labstract.lest.wallistract.PrefManager;
import com.labstract.lest.wallistract.R;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Adi on 21-01-2017.
 */
public class SettingsActivity extends Activity {
    private PrefManager pref;
    final static int RQS_1 = 1;
    private TextView  txtNoOfGridColumns, txtGalleryName;
    private Button btnSave;
    private Switch changewallpaperswitch;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ArrayList<String> choosenAlbum=new ArrayList<>();
    Calendar calendar=null;
    private CheckBox  stockSnap,isoRepublic,Kaboom,pexels,Pixabay,SplitShare,tommy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        txtNoOfGridColumns = (TextView) findViewById(R.id.txtNoOfColumns);
        txtGalleryName = (TextView) findViewById(R.id.txtGalleryName);
        btnSave = (Button) findViewById(R.id.btnSave);
        changewallpaperswitch=(Switch)findViewById(R.id.changewallpaper);
        pref = new PrefManager(getApplicationContext());



        // Number of grid columns
        txtNoOfGridColumns.setText(String.valueOf(pref.getNoOfGridColumns()));

        // Gallery name
        txtGalleryName.setText(pref.getGalleryName());


        changewallpaperswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    LayoutInflater factory = LayoutInflater.from(SettingsActivity.this);
                    final View deleteDialogView = factory.inflate(R.layout.choosesetwallpaperdaily, null);
                    stockSnap=(CheckBox)deleteDialogView.findViewById(R.id.stockSnap);
                    isoRepublic=(CheckBox)deleteDialogView.findViewById(R.id.isoRepublic);
                    Kaboom=(CheckBox)deleteDialogView.findViewById(R.id.Kaboom);
                    pexels=(CheckBox)deleteDialogView.findViewById(R.id.pexels);
                    Pixabay=(CheckBox)deleteDialogView.findViewById(R.id.Pixabay);
                    SplitShare=(CheckBox)deleteDialogView.findViewById(R.id.SplitShare);
                    tommy=(CheckBox)deleteDialogView.findViewById(R.id.Tommy);
                    deleteDialog.setView(deleteDialogView);
                    deleteDialogView.findViewById(R.id.OkButton).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            choosenAlbum.clear();
                            if(stockSnap.isChecked())
                            {
                                choosenAlbum.add("stockSnap");
                            }
                            if(isoRepublic.isChecked())
                            {
                                choosenAlbum.add("isoRepublic");
                            }
                            if(Kaboom.isChecked())
                            {
                                choosenAlbum.add("Kaboom");
                            }
                            if(pexels.isChecked())
                            {
                                choosenAlbum.add("pexels");
                            }
                            if(Pixabay.isChecked())
                            {
                                choosenAlbum.add("Pixabay");
                            }
                            if(SplitShare.isChecked())
                            {
                                choosenAlbum.add("SplitShare");
                            }
                            if(tommy.isChecked())
                            {
                                choosenAlbum.add("tommy");
                            }
                            deleteDialog.dismiss();
                            final Calendar c = Calendar.getInstance();
                            final Calendar calSet = (Calendar) c.clone();
                            mHour = c.get(Calendar.HOUR_OF_DAY);
                            mMinute = c.get(Calendar.MINUTE);
                            TimePickerDialog timePickerDialog = new TimePickerDialog(SettingsActivity.this,
                                    new TimePickerDialog.OnTimeSetListener() {

                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay,
                                                              int minute) {
                                            String timee=String.valueOf(hourOfDay+":"+minute);
                                            Toast.makeText(getApplicationContext(),timee,Toast.LENGTH_SHORT).show();
                                            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                            calSet.set(Calendar.MINUTE, minute);
                                            calSet.set(Calendar.SECOND, 0);
                                            calSet.set(Calendar.MILLISECOND, 0);
                                            calendar=calSet;
                                        }
                                    }, mHour, mMinute, false);
                            timePickerDialog.show();
                        }
                    });
                    deleteDialogView.findViewById(R.id.CancelButton).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteDialog.dismiss();
                            choosenAlbum.clear();
                        }
                    });
                    deleteDialog.show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Not Checked",Toast.LENGTH_LONG).show();
                }
            }
        });


        // Save settings button click listener
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Validating the data before saving to shared preferences

                // validate number of grid columns
                String no_of_columns = txtNoOfGridColumns.getText().toString()
                        .trim();
                if (no_of_columns.length() == 0 || !isInteger(no_of_columns)) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.toast_enter_valid_grid_columns),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // validate gallery name
                String galleryName = txtGalleryName.getText().toString().trim();
                if (galleryName.length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.toast_enter_gallery_name),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // Check for setting changes
                if (!no_of_columns.equalsIgnoreCase(String.valueOf(pref
                        .getNoOfGridColumns()))
                        || !galleryName.equalsIgnoreCase(pref.getGalleryName())) {
                    // User changed the settings
                    // save the changes and launch SplashScreen to initialize
                    // the app again
                    pref.setNoOfGridColumns(Integer.parseInt(no_of_columns));
                    pref.setGalleryName(galleryName);
                    if(calendar!=null) {
                        pref.setSelectedAlbum(choosenAlbum);
                        setAlarm(calendar);
                    }
                    // start the app from SplashScreen
                    Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                    // Clear all the previous activities
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    // user not modified any values in the form
                    // skip saving to shared preferences
                    // just go back to previous activity
                    onBackPressed();
                }

            }
        });

    }
    private void setAlarm(Calendar targetCal){

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);

    }
    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
