package com.example.focus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

public class AppUsageBackgroundService extends BroadcastReceiver {
    private static final String TAG = "AppUsageBackgroundService";

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        if (currentHour == 23 && currentMinute == 59) {
            // Get the app usage data and save it to the database
            try {
                HomeActivity homeActivity = new HomeActivity();
                long currentTime = System.currentTimeMillis();
                //homeActivity.getTotalUsageTime(currentTime);
                homeActivity.getAppUsageTime();
//                List<List<String>> myPair = mainActivity.getAppUsageTime();
//                DBAppsUsage db = new DBAppsUsage(context, "AppsDatabase0");
//                db.addUsage("Thando", mainActivity.getDate(), String.valueOf(mainActivity.getTotalUsageTime()), myPair.get(0), myPair.get(1));
                Log.d("===Saved Data at 11:59===", "Data Saved.");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("===Didn't save  at 11:59===", "Not the right time to save app usage data");
        }
    }
}