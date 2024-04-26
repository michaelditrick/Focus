package com.example.focus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RoutinesActivity extends AppCompatActivity implements ScheduleAdapter.ScheduleAdapterListener {

    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private List<String> schedules;
    private SharedPreferences sharedPreferences;
    private static final int ADD_SCHEDULE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedpreferences = getSharedPreferences("MySchedules", MODE_PRIVATE);
        Integer startHour = sharedpreferences.getInt("startHour",10);

        Log.d("===startHour===", "is:" + startHour);



        if (schedules == null || schedules.isEmpty()) {
            // If no schedules are found, display a message or handle the UI accordingly

            Log.d("===Empty-schedule===", "Alarm On with interval:" + schedules);
            // recyclerView.setVisibility(View.GONE);
            //findViewById(R.id.btnAddSchedule).setVisibility(View.VISIBLE);
        } else {
            // Initialize RecyclerView and adapter with loaded schedules
            adapter = new ScheduleAdapter(schedules, this);
            Log.d("===in- else - check===", "Alarm On with interval:" + schedules);
            recyclerView.setAdapter(adapter);
        }

        // Add schedule button click listener
        findViewById(R.id.btnAddSchedule).setOnClickListener(v -> {
            Intent scheduleIntent = new Intent(RoutinesActivity.this, scheduleActivity.class);
            startActivityForResult(scheduleIntent, ADD_SCHEDULE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_SCHEDULE_REQUEST && resultCode == RESULT_OK && data != null) {
            String newSchedule = data.getStringExtra("newSchedule");
            schedules.add(newSchedule);
            saveSchedulesToSharedPreferences(schedules);
            if (adapter == null) {
                // Initialize RecyclerView and adapter if it's not already initialized
                adapter = new ScheduleAdapter(schedules, this);
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
                findViewById(R.id.btnAddSchedule).setVisibility(View.GONE);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDeleteSchedule(int position, String schedule) {
        schedules.remove(position);
        saveSchedulesToSharedPreferences(schedules);
        adapter.notifyDataSetChanged();
    }

    private void saveSchedulesToSharedPreferences(List<String> schedules) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(schedules);
        editor.putString("schedules", json);
        editor.apply();
    }


}
