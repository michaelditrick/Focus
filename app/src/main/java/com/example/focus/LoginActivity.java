package com.example.focus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {


    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonSignIn;

    public String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize the views
        editTextUsername = findViewById(R.id.signInUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        // Setup the login button click listener
        buttonSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Retrieve the input
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                /*
                TODO:Thando
                no changes needed if database is implemented

                 */

                //Declare the database to check username and password
                DBClass db=new DBClass(getApplicationContext(), "Database0");

                //hash it before comparing
                String passwordHash = PasswordUtils.hashPassword(password);

                // Compare with global variables
                if(db.verifyPassword(username,passwordHash)) {
                    // Credentials match, navigate to Home Page Activity
                    Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    homeIntent.putExtra("thisUserName", username);
                    startActivity(homeIntent);
                } else {
                    // Credentials don't match, show error
                    Toast.makeText(LoginActivity.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
                }

                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                //homeIntent.putExtra("thisUserName", username);
                startActivity(homeIntent);
            }});
    }
}