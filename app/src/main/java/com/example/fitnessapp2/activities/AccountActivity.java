package com.example.fitnessapp2.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessapp2.R;
import com.example.fitnessapp2.data.DatabaseHelper;
import com.example.fitnessapp2.data.User;
import com.example.fitnessapp2.data.UserDAOImpl;

public class AccountActivity extends BaseActivity {
    private TextView profileName, profileAge, profileEmail, profilePhoneNumber, currentHeight, currentWeight;
    private User user;
    private ImageView profileAvatar;
    private SeekBar heightSlider, weightSlider;
    private Button saveChangesButton;
    private UserDAOImpl userDao;
    private long lastSaveTime = 0; // Timestamp of the last save
    private final long SAVE_INTERVAL = 30000; // 30 seconds in milliseconds

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
                    Toast.makeText(AccountActivity.this, "Please wait before saving again.", Toast.LENGTH_SHORT).show();
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