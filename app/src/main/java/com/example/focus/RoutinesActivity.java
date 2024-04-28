package com.example.focus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RoutinesActivity extends AppCompatActivity implements ScheduleAdapter.ScheduleAdapterListener {

    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private List<String> schedules;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines);

        sharedPreferences = getSharedPreferences("MySchedules", Context.MODE_PRIVATE);
        schedules = loadSchedules();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScheduleAdapter(schedules, this);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnAddSchedule).setOnClickListener(v -> {
            Intent intent = new Intent(this, scheduleActivity.class);
            startActivityForResult(intent, 1); // requestCode 1 for adding schedule
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String newSchedule = data.getStringExtra("newSchedule");
            schedules.add(newSchedule);
            adapter.notifyDataSetChanged();
            saveSchedules();
        }
    }

    private List<String> loadSchedules() {
        List<String> loadedSchedules = new ArrayList<>();
        int scheduleCount = sharedPreferences.getInt("schedule_count", 0);
        for (int i = 0; i < scheduleCount; i++) {
            String schedule = sharedPreferences.getString("schedule_" + i, null);
            if (schedule != null) {
                loadedSchedules.add(schedule);
                Log.d("LoadSchedules", "Schedule loaded: " + schedule);
            }
        }
        Log.d("LoadSchedules", "Total schedules loaded: " + loadedSchedules.size());
        return loadedSchedules;
    }

    private void saveSchedules() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("schedule_count", schedules.size());
        for (int i = 0; i < schedules.size(); i++) {
            editor.putString("schedule_" + i, schedules.get(i));
        }
        editor.apply();
    }

    @Override
    public void onDeleteSchedule(int position, String schedule) {
        schedules.remove(position);
        adapter.notifyDataSetChanged();
        saveSchedules();
    }

}
