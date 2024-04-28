package com.example.focus;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button refreshButton;
    private CalendarView calendarView;
    private View histogramView;
    private TextView screenTimeText;
    private TextView notificationText;
    private BottomNavigationView bottomNavigationView;

    //Thando's variables
    private TextView txtName;
    private Calendar cal;
    private Calendar userCalender;
    private List<UsageStats> stats;
    private List appsIconList;
    private List appsNames;
    private List appsUsageTime;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter programAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private long currentTime;
    private long midnight;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        welcomeText = findViewById(R.id.welcomeText);
        refreshButton = findViewById(R.id.refreshButton);
        calendarView = findViewById(R.id.calendarView);
//        View calendarViewLayout = (LinearLayout) View.inflate(this, R.layout.calender_view, null);
//        calendarView = calendarViewLayout.findViewById(R.id.calendarView);
//        spinner = findViewById(R.id.spinner);
//
//        CalendarAdapter adapter = new CalendarAdapter(this);
//        spinner.setAdapter(adapter);

        //Get intent that started this activity
        Intent intent = getIntent();
        String username = intent.getStringExtra("thisUserName"); //Retrive the username

        //Declare the database to check username and password
        DBClass db=new DBClass(getApplicationContext(), "Database0");
        //String name = db.getName(username);

        //welcomeText.setText("Welcome, "+ name);

        userCalender = Calendar.getInstance();
         //Set up the calendar view
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Handle the day change, update the histogram and other views
                //if (dayOfMonth < cal.get(Calendar.DAY_OF_MONTH) && year < cal.get(Calendar.YEAR) && month < cal.get(Calendar.MONTH) ) { // valid

                //userCalender.set(year, month+1, dayOfMonth);     // updating date in calendar
                userCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                userCalender.set(Calendar.MONTH, month);
                userCalender.set(Calendar.YEAR, year);
                Log.d("===Calendar Day===", "calendar Day " + dayOfMonth);
                Log.d("===Calendar Month===", "calendar Month " + month);

                Log.d("===Selected Day===", "selected Day " + userCalender.get(Calendar.DAY_OF_MONTH));
                Log.d("===Selected Month===", "selected Month " + userCalender.get(Calendar.MONTH));
                //Start time
                userCalender.set(Calendar.HOUR_OF_DAY, 0);
                userCalender.set(Calendar.MINUTE, 0);
                userCalender.set(Calendar.SECOND, 0);
                long startTime = userCalender.getTimeInMillis(); // this is midnight (start of the day)

                Log.d("===Selected Day1===", "selected Day1 " + userCalender.get(Calendar.DAY_OF_MONTH));
                Log.d("===Selected Month1===", "selected Month1 " + userCalender.get(Calendar.MONTH));

                //End time
                userCalender.set(Calendar.HOUR_OF_DAY, 23);
                userCalender.set(Calendar.MINUTE, 59);
                userCalender.set(Calendar.SECOND, 0);
                long endTime = userCalender.getTimeInMillis(); // this is midnight (start of the day)

                try {
                    displayTotalUsageTime(startTime, endTime);
                } catch (PackageManager.NameNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Set up the refresh button
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh the data, update histogram and text views
                currentTime = System.currentTimeMillis();
                try {
                    displayTotalUsageTime(midnight, currentTime);
                } catch (PackageManager.NameNotFoundException e) {
                    throw new RuntimeException(e);
                }
                calendarView.setDate(currentTime);
            }
        });

        // Set up navigation buttons
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navHome) {

                return true;
            } else if (id == R.id.navRoutines) {
                startActivity(new Intent(this, RoutinesActivity.class));
                return true;
            } else if (id == R.id.navProfile) {
                Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                profileIntent.putExtra("thisUserName", username);
                startActivity(profileIntent);
                //startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }

            return false;
        });

        // TODO: Implement data loading and update methods
        // Initialize...
        screenTimeText = findViewById(R.id.totalUsageTime);
        recyclerView = findViewById(R.id.rvProgram);
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        currentTime = System.currentTimeMillis();

        // start time
        cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        midnight = cal.getTimeInMillis(); // this is midnight (start of the day)

        // Request permission to access usage stats if not granted already
//        if (!hasUsageAccessPermission()) {
//        requestUsageAccessPermission();
//        } else {
//            try {
//                displayTotalUsageTime(midnight, currentTime);
//            } catch (PackageManager.NameNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }

        // Display Apps data and total usage time data
        try {
            displayTotalUsageTime(midnight, currentTime);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: Add methods for data handling and UI updates
    private boolean hasUsageAccessPermission() {
        // Check if permission is granted
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return usageStatsManager != null && usageStatsManager.getAppStandbyBucket() != UsageStatsManager.STANDBY_BUCKET_ACTIVE;
        }
        return (ContextCompat.checkSelfPermission(HomeActivity.this,
                android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestUsageAccessPermission() {
        boolean status = hasUsageAccessPermission();
        Log.d("===Permissions===", "Status of permissions " + status);
        if (status) {
            return;
        }
        ActivityCompat.requestPermissions(HomeActivity.this,
            new String[] {
                    Manifest.permission.PACKAGE_USAGE_STATS
            }, 0);
        //Request permission
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // Permission granted, display total usage time
            try {
                displayTotalUsageTime(midnight, currentTime);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void displayTotalUsageTime(long startTime, long endTime) throws PackageManager.NameNotFoundException {
        long totalTime = getTotalUsageTime(startTime, endTime);
        long hours = totalTime / (1000 * 60 * 60);
        long minutes = (totalTime / (1000 * 60)) % 60;
        long seconds = (totalTime / 1000) % 60;
        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        screenTimeText.setText("Total Screen Time: \n" + formattedTime);

        displayAppUsageTime();
    }

    public long getTotalUsageTime(long startTime, long endTime) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        //long currentTime = System.currentTimeMillis();
        Log.d("===endTime===", "End time in minutes " + (endTime/ (1000 * 60)) % 60);

        Log.d("===Midnight===", "Midnight time in minutes " + (startTime/ (1000 * 60)) % 60);

        Log.d("===endTime Day===", "End Day " + (endTime/ (1000 * 60 * 60 * 24)) % 24);

        Log.d("===Current Day===", "current Day " + cal.get(Calendar.DAY_OF_MONTH));
        Log.d("===Current Month===", "current Month " + cal.get(Calendar.MONTH));

        long totalUsageTime1 = 0;

        //calculating screen interaction
        stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startTime, endTime);
        for (UsageStats usageStats : stats) {
            totalUsageTime1 += usageStats.getTotalTimeInForeground();
        }
        return totalUsageTime1;
    }

    public void displayAppUsageTime() throws PackageManager.NameNotFoundException {
        // This is to display the individual apps and their usage time
        appsIconList = new ArrayList<>();
        appsUsageTime = new ArrayList<>();
        List<List> myPair;
        try {
            myPair = getAppUsageTime();
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        appsNames = myPair.get(1);
        appsIconList = myPair.get(0);//getDrawable(appsNames);
        appsUsageTime = myPair.get(2);

        // Create instance of program adapter. Pass context and all other elements to the constructor
        programAdapter = new ProgramAdapter(this, appsUsageTime, appsIconList);

        // Attach adapter with the recyclerView
        recyclerView.setAdapter(programAdapter);
    }

    // List<List<Drawable>, List<String>, List<String>>
    public List<List> getAppUsageTime() throws PackageManager.NameNotFoundException {
        Map<String, Pair> appUsageTime = new HashMap<>();
        PackageManager packageManager = getPackageManager();
        List<List>finalList = new ArrayList<>();
        List<String>appsNames = new ArrayList<>();
        List<String>appsTimeList = new ArrayList<>();
        List<Drawable> appIcons = new ArrayList<>();

        for (UsageStats usageStats : stats) {
            String packageName = usageStats.getPackageName();
            long totalTimeInForeground = usageStats.getTotalTimeInForeground();

            if (isUserLaunchedApp(packageName)){
                // get the icon of the apps too
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                Drawable appIcon = packageManager.getApplicationIcon(applicationInfo);

                //Put the app name, usage time and icon in the map
                appUsageTime.put(packageName, new Pair<>(totalTimeInForeground, appIcon));
            }
        }

        // put the information in a list instead to make display in the
        for (Map.Entry<String, Pair> entry : appUsageTime.entrySet()) {
            String packageName = entry.getKey();
            Pair<Long, Drawable> pair = appUsageTime.get(packageName);
            Long usageTime = pair.first;
            Drawable drawable = pair.second;

            long hours = usageTime / (1000 * 60 * 60);
            long minutes = (usageTime / (1000 * 60)) % 60;
            long seconds = (usageTime / 1000) % 60;
            String formattedTime;
            if (hours >= 1){
                formattedTime = String.format("%02dhrs %02dmin,", hours, minutes);
            } else if (minutes >= 1) {
                formattedTime = String.format("%02dmin %02dsec", minutes, seconds);
            }
            else {formattedTime = String.format("%02dsec", seconds);}

            if (usageTime != null && seconds > 0) { //edit this to get time that is greater than 1 minute.
                appsNames.add(packageName);
                appsTimeList.add(formattedTime);
                appIcons.add(drawable);
            }
        }

        finalList.add(appIcons);
        finalList.add(appsNames);
        finalList.add(appsTimeList);
        return finalList;
    }

    public List<Drawable> getDrawable(List<String> appNames) throws PackageManager.NameNotFoundException {
        List<Drawable> appIcons = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        for (String packageName : appNames) {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            Drawable appIcon = packageManager.getApplicationIcon(applicationInfo);
            appIcons.add(appIcon);
        }
        return appIcons;
    }
    public boolean isUserLaunchedApp(String packageName) {
        // You can add your own logic here to determine if an app is user-launched or not
        // For example, you could check if the app has a launcher activity, or use a list of known system/background apps
        // Here's a simple example that excludes apps with the package name starting with "com.google.android"

        return (!packageName.startsWith("com.google.android") && !packageName.startsWith("com.android") && !packageName.startsWith("com.motorola"));
    }

    // This is a background event that checks if the time is 11:59 every minute.
    private void scheduleBackgroundService() {
        Intent intent = new Intent(this, BackgroundService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Schedule the task to run every minute
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60 * 1000, pendingIntent);
    }
}
