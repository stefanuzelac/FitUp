package com.example.fitnessapp2;

import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AccountActivity extends BaseActivity {
    private TextView profileName, profileAge, profileEmail, profilePhoneNumber;
    private User user;
    private SharedPreferences sharedPref;
    private UserDAOImpl userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setupToolbarAndDrawer();

        // Initialize TextViews
        profileName = findViewById(R.id.profileName);
        profileAge = findViewById(R.id.profileAge);
        profileEmail = findViewById(R.id.profileEmail);
        profilePhoneNumber = findViewById(R.id.profilePhoneNumber);

        // get the email and password of the currently logged in user from SharedPreferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String email = sharedPref.getString("email", "");
        String password = sharedPref.getString("password", "");

        // get the user data from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        userDao = new UserDAOImpl(databaseHelper);

        // get the user data from the database
        user = userDao.getUserByEmailAndPassword(email, password);

        // get the user data from the database
        user = userDao.getUserByEmailAndPassword(email, password);

        // set the full name, age, email, and phone number in the TextViews
        if (user != null) {
            profileName.setText(user.getName() + " " + user.getLastName());
            profileAge.setText(String.valueOf(user.getAge()));
            profileEmail.setText(user.getEmail());
            profilePhoneNumber.setText(user.getPhone());
        }
    }
}