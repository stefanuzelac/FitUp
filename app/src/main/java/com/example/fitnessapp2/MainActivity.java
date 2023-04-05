package com.example.fitnessapp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
    EditText loginEmail, loginPassword;
    Button loginButton, signUpButton;
    CheckBox rememberMeCheckbox;
    DatabaseHelper dbHelper;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set instance of dbHelper class
        dbHelper = new DatabaseHelper(MainActivity.this);

        //initialize UI elements
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);

        //get SharedPreferences object for the app
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        //initialize email and password variables
        String email = "";
        String password = "";

        //check if the rememberMe flag is set for the user
        boolean rememberMe = sharedPref.getBoolean("rememberMe", false);
        if (rememberMe) {
            email = sharedPref.getString("email", "");
            password = sharedPref.getString("password", "");

            //set the checkbox to checked if the user has previously checked it
            rememberMeCheckbox.setChecked(true);
        }

        //set the email and password fields to the values stored in SharedPreferences
        loginEmail.setText(email);
        loginPassword.setText(password);

        //set up a listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get the entered email and password
                String enteredEmail = loginEmail.getText().toString().trim();
                String enteredPassword = loginPassword.getText().toString().trim();

                //validate that both fields are filled out
                if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check if the email and password combination exists in the database
                User user = dbHelper.getUser(enteredEmail.trim(), enteredPassword.trim());
                if (user == null) {
                    Toast.makeText(MainActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Get the user's ID
                loggedInUserId = user.getId();

                //store email and password in SharedPreferences
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("email", enteredEmail);
                editor.putString("password", enteredPassword);

                //store rememberMe state in SharedPreferences
                editor.putBoolean("rememberMe", rememberMeCheckbox.isChecked());

                //save changes to editor asynchronously
                editor.apply();

                //start the app main page activity and close the current activity
                Intent intent = new Intent(MainActivity.this, AppMainPageActivity.class);
                intent.putExtra("loggedInUserId", loggedInUserId); //passing the user ID to the next activity
                startActivity(intent);
                finish();


            }
        });

        //set up a listener for the sign up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }

}