package com.example.focus;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class scheduleActivity extends AppCompatActivity {

    private int startHour, startMinute, endHour, endMinute;
    private final boolean[] weekDays = new boolean[7]; // Sun, Mon, Tue, Wed, Thu, Fri, Sat
    private TextView txtStartTime, txtEndTime;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        sharedPreferences = getSharedPreferences("MySchedules", MODE_PRIVATE);

        txtStartTime = findViewById(R.id.txtStartTime);
        txtEndTime = findViewById(R.id.txtEndTime);

        txtStartTime.setOnClickListener(v -> showTimePicker(true));
        txtEndTime.setOnClickListener(v -> showTimePicker(false));

        setupDayToggle((ToggleButton) findViewById(R.id.toggleSunday), 0);
        setupDayToggle((ToggleButton) findViewById(R.id.toggleMonday), 1);
        setupDayToggle((ToggleButton) findViewById(R.id.toggleTuesday), 2);
        setupDayToggle((ToggleButton) findViewById(R.id.toggleWednesday), 3);
        setupDayToggle((ToggleButton) findViewById(R.id.toggleThursday), 4);
        setupDayToggle((ToggleButton) findViewById(R.id.toggleFriday), 5);
        setupDayToggle((ToggleButton) findViewById(R.id.toggleSaturday), 6);

        // Repeat this for the other days...

        Button btnSetSchedule = findViewById(R.id.btnSetSchedule);
        btnSetSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSchedule();
                Log.d("===save-weeks===", "Alarm On with interval:" + weekDays[0]);
                Intent intent = new Intent(scheduleActivity.this, RoutinesActivity.class);
                startActivity(intent);
            }
        });




    }

    private void showTimePicker(boolean isStartTime) {
        int hour = isStartTime ? startHour : endHour;
        int minute = isStartTime ? startMinute : endMinute;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int selectedHour, int selectedMinute) -> {
                    if (isStartTime) {
                        startHour = selectedHour;
                        startMinute = selectedMinute;
                        txtStartTime.setText(String.format("%02d:%02d", startHour, startMinute));
                    } else {
                        endHour = selectedHour;
                        endMinute = selectedMinute;
                        txtEndTime.setText(String.format("%02d:%02d", endHour, endMinute));
                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }

    private void setupDayToggle(ToggleButton toggleButton, int dayIndex) {
        toggleButton.setOnClickListener(v -> weekDays[dayIndex] = toggleButton.isChecked());
    }

    private void saveSchedule() {
        // Save the schedule details to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("startHour", startHour);
        editor.putInt("startMinute", startMinute);
        editor.putInt("endHour", endHour);
        editor.putInt("endMinute", endMinute);
        for (int i = 0; i < weekDays.length; i++) {
            editor.putBoolean("day_" + i, weekDays[i]);
        }
        editor.apply();



    }
}
