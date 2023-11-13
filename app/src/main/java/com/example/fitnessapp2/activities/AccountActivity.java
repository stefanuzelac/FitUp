package com.example.fitnessapp2.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.example.fitnessapp2.data.DatabaseHelper;
import com.example.fitnessapp2.R;
import com.example.fitnessapp2.data.User;
import com.example.fitnessapp2.data.UserDAOImpl;

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