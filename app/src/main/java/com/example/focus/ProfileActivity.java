package com.example.focus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtName, txtAge, txtUsername, txtGender;
    private ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtName = findViewById(R.id.txtName);
        txtAge = findViewById(R.id.txtAge);
        txtUsername = findViewById(R.id.txtUsername);
        txtGender = findViewById(R.id.txtGender);
        ImageView imgSettings = findViewById(R.id.imgSettings);
        ImageView imgSignOut = findViewById(R.id.imgSignOut);

        // Set up the settings icon click listener
        imgSettings.setOnClickListener(view -> startActivity(new Intent(this, SettingsActivity.class)));

        // Set up the sign out icon click listener
        imgSignOut.setOnClickListener(view -> {

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Fetch user data
        fetchUserData();
    }

    private void fetchUserData() {
        databaseExecutor.execute(() -> {
            //sample fetching:
            // lets try to fetch from a different thread : Thando

            runOnUiThread(() -> {

            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseExecutor.shutdown(); // Shutdown the executor when the activity is destroyed
    }



    }

