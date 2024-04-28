package com.example.focus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        //Get intent that started this activity
        Intent intent = getIntent();
        String username = intent.getStringExtra("thisUserName"); //Retrive the username

        // Set up the settings icon click listener
        Button changePassword;
        changePassword = findViewById(R.id.buttonChangePassword);
        changePassword.setOnClickListener(view -> {
            EditText currentPass = findViewById(R.id.editTextCurrentPassword);
            EditText newPass = findViewById(R.id.editTextNewPassword);

            String currentPassword = currentPass.getText().toString();
            String newPassword = newPass.getText().toString();

            String passwordHash = PasswordUtils.hashPassword(currentPassword);

            DBClass db=new DBClass(getApplicationContext(), "Database0");

            if(db.verifyPassword(username,passwordHash)) {
                if (!isValidPassword(newPassword)) {
                    Toast.makeText(SettingsActivity.this, "The new password is INVALID! Try again", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    //get hashed password
                    String passwordHash2 = PasswordUtils.hashPassword(newPassword);
                    // Credentials match, navigate to Home Page Activity
                    db.updatePassword(username, passwordHash2);
                    Toast.makeText(SettingsActivity.this, "Password changed SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                    Intent intentPasswordChanged = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentPasswordChanged);
                    finish();
                }
            } else {
                // Credentials don't match, show error
                Toast.makeText(SettingsActivity.this, "The currentPassword is incorrect.", Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    //check password validity
    private boolean isValidPassword(String password) {
        return password.matches("^.{8,}$"); //"^(?=.*[0-9])(?=.*[A-Z]).{8,}$"
    }
}