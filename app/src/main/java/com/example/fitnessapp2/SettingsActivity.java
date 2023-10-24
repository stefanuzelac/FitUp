package com.example.fitnessapp2;

import androidx.biometric.BiometricManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

public class SettingsActivity extends BaseActivity {
    private Switch biometricSwitch;
    private BiometricManager biometricManager;
    private SharedPreferences sharedPref;
    Integer loggedInUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupToolbarAndDrawer();

        biometricSwitch = findViewById(R.id.switch_biometric);
        biometricManager = BiometricManager.from(this);

        loggedInUserId = getIntent().getIntExtra("loggedInUserId", -1);
        sharedPref = getSharedPreferences("biometric_pref_" + loggedInUserId, MODE_PRIVATE);


        boolean isBiometricEnabled = sharedPref.getBoolean("biometric_login_enabled", false);
        biometricSwitch.setChecked(isBiometricEnabled);

        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                biometricSwitch.setEnabled(true);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                biometricSwitch.setEnabled(false);
                break;
        }

        biometricSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            if (isChecked) {
                // Handle switch checked
                editor.putBoolean("biometric_login_enabled", true);
            } else {
                // Handle switch unchecked
                editor.remove("biometric_login_enabled");
            }
            editor.apply();
        });
    }
}
