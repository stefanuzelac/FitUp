package com.example.fitnessapp2.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.fitnessapp2.R;
import com.example.fitnessapp2.data.database.DatabaseHelper;
import com.example.fitnessapp2.data.model.User;
import com.example.fitnessapp2.data.database.daoimpl.UserDAOImpl;
import com.google.android.material.snackbar.Snackbar;

public class AccountActivity extends BaseActivity {
    private TextView profileName, profileAge, profileEmail, profilePhoneNumber, currentHeight, currentWeight;
    private User user;
    private ImageView profileAvatar;
    private SeekBar heightSlider, weightSlider;
    private Button saveChangesButton;
    private UserDAOImpl userDao;
    private long lastSaveTime = 0; // Timestamp of the last save
    private final long SAVE_INTERVAL = 30000; // 30 seconds in milliseconds
    private Snackbar cooldownSnackbar;
    private Handler snackbarHandler = new Handler();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setupToolbarAndDrawer();

        // Initialize TextViews and components
        profileName = findViewById(R.id.profileName);
        profileAge = findViewById(R.id.profileAge);
        profileEmail = findViewById(R.id.profileEmail);
        profilePhoneNumber = findViewById(R.id.profilePhoneNumber);
        profileAvatar = findViewById(R.id.profileAvatar);
        heightSlider = findViewById(R.id.heightSlider);
        weightSlider = findViewById(R.id.weightSlider);
        currentHeight = findViewById(R.id.currentHeight);
        currentWeight = findViewById(R.id.currentWeight);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        // Initialize UserDAOImpl
        userDao = new UserDAOImpl(new DatabaseHelper(this));

        // Load initial user data
        loadLatestUserData();

        // Slider change listeners
        heightSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (user != null) {
                    user.setHeight(progress);
                    currentHeight.setText(progress + " cm"); // Update height display
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        weightSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (user != null) {
                    user.setWeight(progress);
                    currentWeight.setText(progress + " kg"); // Update weight display
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        saveChangesButton.setOnClickListener(view -> {
            if (user != null) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastSaveTime < SAVE_INTERVAL) {
                    // If the time since the last save is less than the interval, show a message
                    showCooldownSnackbar((SAVE_INTERVAL - (currentTime - lastSaveTime)) / 1000);
                } else {
                    // Update the user data in the database
                    userDao.updateUser(user);
                    // Refresh the session's user data
                    sessionManager.refreshCurrentUser();
                    Toast.makeText(AccountActivity.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();

                    // Update the lastSaveTime
                    lastSaveTime = currentTime;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showCooldownSnackbar(long remainingSeconds) {
        if (cooldownSnackbar == null || !cooldownSnackbar.isShownOrQueued()) {
            cooldownSnackbar = Snackbar.make(findViewById(R.id.content_frame),
                    "Wait " + remainingSeconds + "s before saving again.", Snackbar.LENGTH_INDEFINITE);

            // Apply the custom style defined in themes.xml
            cooldownSnackbar.getView().setBackgroundColor(getResources().getColor(R.color.primary, null)); // Use primary color
            TextView snackbarText = cooldownSnackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            snackbarText.setTextColor(getResources().getColor(R.color.on_primary, null)); // Use on-primary color

            Runnable snackbarRunnable = new Runnable() {
                long secondsLeft = remainingSeconds;

                @Override
                public void run() {
                    if (secondsLeft > 0) {
                        cooldownSnackbar.setText("Wait " + secondsLeft + "s before saving again.");
                        secondsLeft--;
                        snackbarHandler.postDelayed(this, 1000);
                    } else {
                        cooldownSnackbar.dismiss();
                    }
                }
            };
            snackbarHandler.postDelayed(snackbarRunnable, 1000);
            cooldownSnackbar.show();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        // Refresh user data each time the activity resumes
        loadLatestUserData();
    }

    private void loadLatestUserData() {
        // Refresh user data
        sessionManager.refreshCurrentUser();
        user = sessionManager.getCurrentUser();

        // Update UI with the latest user data
        updateUIWithUserData();
    }

    private void updateUIWithUserData() {
        if (user != null) {
            profileName.setText(user.getName() + " " + user.getLastName());
            profileAge.setText(String.valueOf(user.getAge()));
            profileEmail.setText(user.getEmail());
            profilePhoneNumber.setText(user.getPhone());
            heightSlider.setProgress(user.getHeight());
            weightSlider.setProgress((int) user.getWeight());
            currentHeight.setText(user.getHeight() + " cm");
            currentWeight.setText((int) user.getWeight() + " kg");

            // Set avatar based on gender
            if (user.getGender().equalsIgnoreCase("male")) {
                profileAvatar.setImageResource(R.drawable.ic_male_avatar);
            } else {
                profileAvatar.setImageResource(R.drawable.ic_female_avatar);
            }
        }
    }
}