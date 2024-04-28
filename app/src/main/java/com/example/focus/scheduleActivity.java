package com.example.focus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class scheduleActivity extends AppCompatActivity {

    private TextView txtStartTime, txtEndTime;
    private int startHour, startMinute, endHour, endMinute;
    private boolean[] weekDays = new boolean[7]; // Array to store day selections

    private int alarmFrequency;
    private TextView alarmInfo;
    private int setDay;
    private int setMonth;
    private int setYear;
    private int setHr;
    private int setMin;
    public String alarmMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // Initialize TextViews for time display
        txtStartTime = findViewById(R.id.txtStartTime);
        txtEndTime = findViewById(R.id.txtEndTime);
        initializeTimePickers();

        // Initialize day toggle buttons
        setupDayToggles();

        // Set button to finalize the schedule
        Button btnSetSchedule = findViewById(R.id.btnSetSchedule);
        btnSetSchedule.setOnClickListener(v -> saveSchedule());
    }

    private void initializeTimePickers() {
        Calendar calendar = Calendar.getInstance();
        startHour = endHour = calendar.get(Calendar.HOUR_OF_DAY);
        startMinute = endMinute = calendar.get(Calendar.MINUTE);

        txtStartTime.setOnClickListener(v -> showTimePicker(true));
        txtEndTime.setOnClickListener(v -> showTimePicker(false));
    }

    private void showTimePicker(boolean isStartTime) {
        int hour = isStartTime ? startHour : endHour;
        int minute = isStartTime ? startMinute : endMinute;

        new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
            if (isStartTime) {
                startHour = hourOfDay;
                startMinute = minuteOfHour;
                txtStartTime.setText(String.format("%02d:%02d", startHour, startMinute));
            } else {
                endHour = hourOfDay;
                endMinute = minuteOfHour;
                txtEndTime.setText(String.format("%02d:%02d", endHour, endMinute));
            }
        }, hour, minute, true).show();
    }

    private void setupDayToggles() {
        int[] toggleButtonsIDs = new int[]{R.id.toggleSunday, R.id.toggleMonday, R.id.toggleTuesday,
                R.id.toggleWednesday, R.id.toggleThursday, R.id.toggleFriday,
                R.id.toggleSaturday};
        for (int i = 0; i < toggleButtonsIDs.length; i++) {
            ToggleButton toggleButton = findViewById(toggleButtonsIDs[i]);
            setupDayToggle(toggleButton, i);
        }
    }

    private void setupDayToggle(ToggleButton toggleButton, int dayIndex) {
        toggleButton.setOnClickListener(v -> weekDays[dayIndex] = toggleButton.isChecked());
    }

    private void saveSchedule() {
        StringBuilder scheduleBuilder = new StringBuilder();
        scheduleBuilder.append(String.format("%02d:%02d - %02d:%02d", startHour, startMinute, endHour, endMinute));
        for (boolean day : weekDays) {
            scheduleBuilder.append(",").append(day ? "1" : "0");
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MySchedules", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int scheduleCount = sharedPreferences.getInt("schedule_count", 0);
        editor.putString("schedule_" + scheduleCount, scheduleBuilder.toString());
        alarmMessage = scheduleBuilder.toString();
        editor.putInt("schedule_count", scheduleCount + 1);
        editor.apply();

        setResult(RESULT_OK);

        // send notification
        start_Sensing_new(0);

        // Navigate back to the Sign In Activity
        Intent scheduleIntent = new Intent(scheduleActivity.this, RoutinesActivity.class);
        startActivity(scheduleIntent);

    }

    public <Calender> void start_Sensing_new(int frequency) {  //testing for upload frequency.

        Log.d("MyActivity", "Alarm On with interval:" + frequency);
        Calendar cal = Calendar.getInstance();
        //cal = Calendar.getInstance();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            cal.set(Calendar.HOUR_OF_DAY,setHr);
            cal.set(Calendar.MINUTE,setMin);
        }


        Intent intent1 = new Intent(this, sendNotification.class);
        intent1.putExtra("alarmMessage", alarmMessage);
        PendingIntent pendingIntent = null;

        // set request code to interval number plus 30
        pendingIntent = PendingIntent.getBroadcast(this, 40, intent1, PendingIntent.FLAG_IMMUTABLE);

        //Generating object of alarmManager using getSystemService method. Here ALARM_SERVICE is used to receive alarm manager with intent at a time.
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + 60*1000, pendingIntent); //interval week apart
        Log.d("===Sensing alarm===", "One time alert alarm has been created. This alarm will send to a broadcast sensing receiver.");


    }
}
