package com.example.focus;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BackgroundService extends AppCompatActivity {
    private int alarmFrequency;
    private TextView alarmInfo;
    private int setDay;
    private int setMonth;
    private int setYear;
    private int setHr;
    private int setMin;
    public String alarmMessage;
    private List<String> schedules;
    private SharedPreferences sharedPreferences;
    private static final String TAG = "AppUsageBackgroundService";
    public void onCreate(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        //must get start times from the shared preference
        sharedPreferences = getSharedPreferences("MySchedules", Context.MODE_PRIVATE);
        schedules = loadSchedules();

        if (currentHour == 23 && currentMinute == 59) {
            // Get the app usage data and save it to the database

        } else {
            Log.d("===Didn't save  at 11:59===", "Not the right time to save app usage data");
        }
    }
    private List<String> loadSchedules() {
        List<String> loadedSchedules = new ArrayList<>();
        int scheduleCount = sharedPreferences.getInt("schedule_count", 0);
        for (int i = 0; i < scheduleCount; i++) {
            String schedule = sharedPreferences.getString("schedule_" + i, null);
            Integer startHour = sharedPreferences.getInt("startHour", 0);
            if (schedule != null) {
                loadedSchedules.add(schedule);
                Log.d("LoadSchedules", "Schedule loaded: " + schedule);
            }
        }
        Log.d("LoadSchedules", "Total schedules loaded: " + loadedSchedules.size());
        return loadedSchedules;
    }

    public void setNotifications() {
        Calendar cal = Calendar.getInstance();;
        // variables for the notifications
        setDay = cal.get(Calendar.DAY_OF_MONTH);     // initializing current day
        setYear = cal.get(Calendar.YEAR);            // initializing current year
        setMonth = cal.get(Calendar.MONTH);          // initializing current month

        // setHr = tp.getHour();
        // setMin = tp.getMinute();
        alarmMessage = String.valueOf(setMonth+1)+"/"+String.valueOf(setDay)+"/"+String.valueOf(setYear)+" at "+String.valueOf(setHr)+":"+String.valueOf(setMin);
        alarmInfo.setText("Alarm set for: " + alarmMessage);

        start_Sensing_new(0, cal);
    }

    public <Calender> void start_Sensing_new(int frequency, Calendar cal) {  //testing for upload frequency.

        Log.d("MyActivity", "Alarm On with interval:" + frequency);
        //cal = Calendar.getInstance();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            cal.set(Calendar.HOUR_OF_DAY,setHr);
            cal.set(Calendar.MINUTE,setMin - frequency);
        }
        //long timeInMillis = cal.getTimeInMillis();
        //long currentTime = System.currentTimeMillis();
        //cal.setTimeInMillis(timeInMillis);

        Intent intent1 = new Intent(BackgroundService.this, sendNotification.class);
        intent1.putExtra("alarmMessage", alarmMessage);
        PendingIntent pendingIntent = null;


        //Retrieve a PendingIntent that will perform a broadcast. getBroadcast takes 3 input parameters:
        //Context: The Context in which this PendingIntent should perform the broadcast.
        //requestCode: Private request code for the sender to identify the intent.
        // In this case, we are assigning a request code of 40 for the SendNotification class.
        //We can choose any arbitrary values for the request code.

        //Intent: The Intent to be broadcast. This value cannot be null.
        // Flags are constants associated with pending intent. More info at https://developer.android.com/reference/android/app/PendingIntent#FLAG_ONE_SHOT
        //Flag indicating that if the described PendingIntent already exists,
        // then keep it but replace its extra data with what is in this new Intent.

        // set request code to interval number plus 30
        pendingIntent = PendingIntent.getBroadcast(this, 40, intent1, PendingIntent.FLAG_IMMUTABLE);

        //Generating object of alarmManager using getSystemService method. Here ALARM_SERVICE is used to receive alarm manager with intent at a time.
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);

        //this method creates a repeating, exactly timed alarm
        //alarmManager setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), frequency*60*1000, pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent); //interval week apart
        Log.d("===Sensing alarm===", "One time alert alarm has been created. This alarm will send to a broadcast sensing receiver.");

        // A toast provides simple feedback/message on the screen about an operation in a small popup.
        Toast.makeText(this, "Alarm on " +alarmMessage +" has been set to repeat every "+ frequency +" minutes.", Toast.LENGTH_LONG).show();
    }

//    public void delete_set_alarm(int frequency, Calendar cal) {  //testing for upload frequency.
//
//        Log.d("MyActivity", "Alarm deleting" + frequency);
//        //cal = Calendar.getInstance();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            cal.set(Calendar.HOUR_OF_DAY,setHr);
//            cal.set(Calendar.MINUTE,setMin - frequency);
//        }
//
//        Intent intent1 = new Intent(this, sendNotification.class);
//        PendingIntent pendingIntent = null;
//
//        pendingIntent = PendingIntent.getBroadcast(this, 40, intent1, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//        //Generating object of alarmManager using getSystemService method. Here ALARM_SERVICE is used to receive alarm manager with intent at a time.
//        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
//
//        alarmManager.cancel(pendingIntent);
//
//        Log.d("===Alarm deleted===", "Yes.");
//
//        // A toast provides simple feedback/message on the screen about an operation in a small popup.
//        Toast.makeText(this, "Alarm Has Been Deleted.", Toast.LENGTH_LONG).show();
//    }
}