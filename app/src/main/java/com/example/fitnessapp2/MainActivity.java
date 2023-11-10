package com.example.fitnessapp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class MainActivity extends BaseActivity {
    EditText loginEmail, loginPassword;
    Button loginButton, signUpButton;
    CheckBox rememberMeCheckbox;
    DatabaseHelper dbHelper;
    private PreferenceManager preferenceManager;
    boolean rememberMeCheckboxState;
    UserDAO userDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of the UserDAOImpl using the DatabaseHelper
        dbHelper = new DatabaseHelper(MainActivity.this);
        userDAO = new UserDAOImpl(dbHelper);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);

        String email = "";
        String password = "";

        preferenceManager = new PreferenceManager(this);

        rememberMeCheckboxState = preferenceManager.getRememberMePreference();
        rememberMeCheckbox.setChecked(rememberMeCheckboxState);

        if (rememberMeCheckboxState) {
            loginEmail.setText(preferenceManager.getEmailPreference());
        }
        //set the checkbox change listener
        rememberMeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setRememberMePreference(isChecked);
            if (!isChecked) {
                //clear preferences if user unchecks the remember me checkbox
                preferenceManager.clearPreferences();
            }
        });

        // Login button listener
        loginButton.setOnClickListener(view -> {
            // Get the email and password from the EditTexts
            String enteredEmail = loginEmail.getText().toString().trim();
            String enteredPassword = loginPassword.getText().toString().trim();

            // Check for empty fields
            if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Try to log the user in
            User user = userDAO.getUserByEmailAndPassword(enteredEmail, enteredPassword);
            if (user != null) {
                // Login success
                UserSessionManager.getInstance().setCurrentUser(user);

                // If remember me is checked, save credentials
                if (rememberMeCheckbox.isChecked()) {
                    preferenceManager.setEmailPreference(enteredEmail);
                }
                // GO to main app page
                Intent intent = new Intent(MainActivity.this, AppMainPageActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Clear the password field on failed login
               loginPassword.setText("");
               Toast.makeText(MainActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
        // Sign up button listener
        signUpButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void checkSession() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update the checkbox state from preferences
        rememberMeCheckbox.setChecked(preferenceManager.getRememberMePreference());
    }

    @Override
    protected void setupToolbarAndDrawer() {
    }

    @Override
    protected void updateNavigationHeaderWithUserData() {
    }
}