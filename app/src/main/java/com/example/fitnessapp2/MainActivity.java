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
    SharedPreferences sharedPref;
    BiometricManager biometricManager;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    Integer loggedInUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize BiometricManager
        biometricManager = BiometricManager.from(this);

        dbHelper = new DatabaseHelper(MainActivity.this);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);

        String email = "";
        String password = "";

        sharedPref = getSharedPreferences("remember_me_pref", MODE_PRIVATE);

        boolean rememberMeCheckboxState = sharedPref.getBoolean("rememberMeCheckboxState", false);
        rememberMeCheckbox.setChecked(rememberMeCheckboxState);

        if (rememberMeCheckboxState) {
            email = sharedPref.getString("email", "");
            password = sharedPref.getString("password", "");
            loggedInUserId = sharedPref.getInt("loggedInUserId", -1);

            //set the checkbox to checked if the user has previously checked it
            rememberMeCheckbox.setChecked(true);
        }

        rememberMeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("rememberMeCheckboxState", isChecked);
            if (!isChecked) {

                editor.remove("email");
                editor.remove("password");
                editor.remove("loggedInUserId");
                editor.putBoolean("rememberMeCheckboxState", false);
            }
            editor.apply();
        });

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
                //get the user's ID
                loggedInUserId = user.getId();

                //store email, password, and loggedInUserId in SharedPreferences only if rememberMeCheckbox is checked
                if (rememberMeCheckbox.isChecked()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("email", enteredEmail);
                    editor.putInt("loggedInUserId", loggedInUserId);
                    editor.apply();
                }

                //start the app main page activity and close the current activity
                Intent intent = new Intent(MainActivity.this, AppMainPageActivity.class);
                intent.putExtra("loggedInUserId", loggedInUserId); //passing the user ID to the next activity
                startActivity(intent);
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (loggedInUserId != null && loggedInUserId != -1) {
            //retrieve the correct shared preferences file
            sharedPref = getSharedPreferences("remember_me_pref", MODE_PRIVATE);
            boolean rememberMeCheckboxState = sharedPref.getBoolean("rememberMeCheckboxState", false);

            if (rememberMeCheckboxState) {
                //check if biometric login is enabled
                sharedPref = getSharedPreferences("biometric_pref_" + loggedInUserId, MODE_PRIVATE);
                boolean biometricLoginEnabled = sharedPref.getBoolean("biometric_login_enabled", false);

                if (biometricLoginEnabled && biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS) {
                    Executor executor = ContextCompat.getMainExecutor(this);
                    biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                            super.onAuthenticationError(errorCode, errString);
                            Toast.makeText(MainActivity.this, "Biometric authentication canceled", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
                            Intent intent = new Intent(MainActivity.this, AppMainPageActivity.class);
                            intent.putExtra("loggedInUserId", loggedInUserId);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            super.onAuthenticationFailed();
                            Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                    promptInfo = new BiometricPrompt.PromptInfo.Builder()
                            .setTitle("FitUp Fingerprint Recognition")
                            .setSubtitle("Log in using your fingerprint credential.")
                            .setNegativeButtonText("Cancel")
                            .build();

                    biometricPrompt.authenticate(promptInfo);
                }
            }
            rememberMeCheckbox.setChecked(rememberMeCheckboxState);
        }
    }
}
