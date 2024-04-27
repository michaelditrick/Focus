package com.example.focus;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class scheduleActivity extends AppCompatActivity {

    private TextView txtStartTime, txtEndTime;
    private int startHour, startMinute, endHour, endMinute;
    private boolean[] weekDays = new boolean[7]; // Array to store day selections

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
        editor.putInt("schedule_count", scheduleCount + 1);
        editor.apply();

        setResult(RESULT_OK);
        // Navigate back to the Sign In Activity
        Intent scheduleIntent = new Intent(scheduleActivity.this, RoutinesActivity.class);
        startActivity(scheduleIntent);
        ///finish();
    }
}
