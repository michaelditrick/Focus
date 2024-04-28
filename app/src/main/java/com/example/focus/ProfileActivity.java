package com.example.focus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtName, txtAge, txtUsername, txtGender;
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    private RadioGroup moodRadioGroup;
    private Spinner spinnerFavoriteMusic;
    private boolean isFirstSelection = true; // To track the first selection
    private String[] songTitles; // Array of song titles
    private String[] songUrls; // Corresponding array of YouTube URLs for the songs
    private CheckBox checkboxYes, checkboxNo;
    private Spinner musicSpinner;

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
        // Initialize the RadioGroup and Spinner
        moodRadioGroup = findViewById(R.id.moodRadioGroup);



        //Get intent that started this activity
        Intent intent1 = getIntent();
        String username = intent1.getStringExtra("thisUserName"); //Retrive the username

        //Declare the database to check username and password
        DBClass db=new DBClass(getApplicationContext(), "Database0");
        String name = db.getName(username);
        Integer age = db.getAge(username);
        String gender = db.getGender(username);

        txtName.setText("Name: "+ name);
        txtAge.setText("Age: "+ age);
        txtUsername.setText("Userame: "+ username);
        txtGender.setText("Gender: "+ gender);

        // Set up the settings icon click listener
        imgSettings.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("thisUserName", username);
            startActivity(intent);
        });


        // Set up the sign out icon click listener
        imgSignOut.setOnClickListener(view -> {

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


        moodRadioGroup = findViewById(R.id.moodRadioGroup);
        moodRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = findViewById(checkedId);
            TextView txtStatus = findViewById(R.id.txtStatus);
            String moodStatus = radioButton.getText().toString();

            txtStatus.setText("Status: " + moodStatus);
        });



        spinnerFavoriteMusic = findViewById(R.id.spinnerFavoriteMusic);


        // Define an array of song titles
        songTitles = new String[]{
                "Select a song", // Placeholder for initial selection
                "RIP - Love ",
                "Enjoy",
                "How we Roll"
                // more song titles
        };

        // Define a corresponding array of YouTube URLs
        songUrls = new String[]{
                "None", //placeholder
                "https://www.youtube.com/watch?v=PwQmPLjnSPo&ab_channel=Faouzia",
                "https://www.youtube.com/watch?v=yVlUVsMUxPk&ab_channel=JUX",
                "https://www.youtube.com/watch?v=xnumI3_qt-w&ab_channel=CiaraVEVO"
                // more links
        };

        // Create an ArrayAdapter using the song titles array
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, songTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFavoriteMusic.setAdapter(adapter);

        // Set an item selected listener for the spinner
        spinnerFavoriteMusic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstSelection) {
                    isFirstSelection = false; // Change the flag after the first selection
                } else {
                    // Navigate to the YouTube URL if a valid song is selected (not the first placeholder)
                    if (position > 0) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(songUrls[position]));
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action when nothing is selected
            }
        });


        //rec stuffs
        checkboxYes = findViewById(R.id.checkboxYes);
        checkboxNo = findViewById(R.id.checkboxNo);

        checkboxYes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkboxNo.setChecked(false); // Uncheck "No" when "Yes" is checked
                Toast.makeText(ProfileActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
            }
        });

        checkboxNo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkboxYes.setChecked(false); // Uncheck "Yes" when "No" is checked
                Toast.makeText(ProfileActivity.this, "Come back next time to experience more exciting app updates", Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseExecutor.shutdown(); // Shutdown the executor when the activity is destroyed
    }



    }

