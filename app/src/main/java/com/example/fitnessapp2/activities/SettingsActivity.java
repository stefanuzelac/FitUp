package com.example.fitnessapp2.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.widget.SwitchCompat;

import com.example.fitnessapp2.R;

public class SettingsActivity extends BaseActivity {
    private SharedPreferences sharedPref;
    private SwitchCompat themeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupToolbarAndDrawer();

        themeSwitch = findViewById(R.id.themeSwitch);
        sharedPref = getSharedPreferences("app_settings", MODE_PRIVATE);

        // Check current theme and set switch position
        boolean isDarkTheme = sharedPref.getBoolean("dark_theme", false);
        themeSwitch.setChecked(isDarkTheme);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("dark_theme", isChecked);
            editor.apply();

            // Trigger the recreate to see the theme change
            recreate();
        });

    }
}
