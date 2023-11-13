package com.example.fitnessapp2.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.fitnessapp2.R;

public class SettingsActivity extends BaseActivity {
    private SharedPreferences sharedPref;
    Integer loggedInUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupToolbarAndDrawer();

        loggedInUserId = getIntent().getIntExtra("loggedInUserId", -1);
        sharedPref = getSharedPreferences("loggedInUserId", MODE_PRIVATE);

    }
}
