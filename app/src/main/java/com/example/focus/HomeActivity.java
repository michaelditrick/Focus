package com.example.focus;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button refreshButton;
    private CalendarView calendarView;
    private View histogramView;
    private TextView screenTimeText;
    private TextView notificationText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        welcomeText = findViewById(R.id.welcomeText);
        refreshButton = findViewById(R.id.refreshButton);
        calendarView = findViewById(R.id.calendarView);


        // Set up the calendar view
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Handle the day change, update the histogram and other views
            }
        });

        // Set up the refresh button
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh the data, update histogram and text views
            }
        });

        // Set up navigation buttons

        // Set up onClickListener for Home button
        Button navHome = findViewById(R.id.navHome);
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        Button navRoutines = findViewById(R.id.navRoutines);
        navRoutines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RoutinesActivity.class);
                startActivity(intent);
            }
        });

        // Set up onClickListener for Profile button
        Button navProfile = findViewById(R.id.navProfile);
        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // TODO: Implement data loading and update methods
    }

    // TODO: Add methods for data handling and UI updates
}
