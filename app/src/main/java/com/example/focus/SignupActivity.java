package com.example.focus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class SignupActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextUsername;
    private EditText editTextGender;
    private EditText editTextPassword;
    private Button buttonCreateAccount;
    private TextView textViewSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextName = findViewById(R.id.editTextName);
        editTextGender = findViewById(R.id.editTextGender);
        editTextAge = findViewById(R.id.editTextAge);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);


        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Simple validation
                if( editTextUsername.getText().toString().isEmpty()  ||  editTextUsername.getText().toString().isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate the inputs
                if (!isValidUsername( editTextUsername.getText().toString())) {
                    Toast.makeText(SignupActivity.this, "Invalid Username!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidPassword(editTextPassword.getText().toString())) {
                    Toast.makeText(SignupActivity.this, "Invalid password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*
                TODO: Thando
                //DBClass db=new DBClass(getApplicationContext(), "Database0");
                //check if the selected username already exists

                if (db.checkUsername(editTextUsername.getText().toString())) {
                    Toast.makeText(SignupActivity.this, "The selected username is taken. Try another one!", Toast.LENGTH_SHORT).show();
                    return;

                else {
                    //get hashed password
                    //String passwordHash = PasswordUtils.hashPassword(editTextPassword.getText().toString());

                    //Save the data in the database
                    //db.addInfo(editTextName.getText().toString(), editTextAge.getText().toString(), editTextGender.getText().toString(), editTextUsername.getText().toString(),passwordHash);

                    // Navigate back to the Sign In Activity
                    Intent signInIntent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(signInIntent);
                }
                */

                if (false) {
                    Toast.makeText(SignupActivity.this, "The selected username is xxxx taken. Try another one!", Toast.LENGTH_SHORT).show();
                }
                else {

                    // Navigate back to the Sign In Activity
                    Intent signInIntent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(signInIntent);
                }
            }




            //check username validity
            private boolean isValidUsername(String username) {
                return username.matches("[A-Za-z0-9]{5,}");
            }
            //check password validity
            private boolean isValidPassword(String password) {
                return password.matches("^.{8,}$"); //"^(?=.*[0-9])(?=.*[A-Z]).{8,}$"
            }

        });

    }
}