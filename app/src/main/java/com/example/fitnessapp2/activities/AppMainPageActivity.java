package com.example.fitnessapp2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fitnessapp2.R;
import com.example.fitnessapp2.data.User;
import com.example.fitnessapp2.utils.UserSessionManager;
import com.google.android.material.button.MaterialButton;

public class AppMainPageActivity extends BaseActivity {
    //declaring my buttons
    private MaterialButton workoutTimerButton, exercisesButton, nutritionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main_page);
        setupToolbarAndDrawer();

        // Get the currentUser from UserSessionManager
        User currentUser = UserSessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Handle this case, maybe finish the activity or redirect to login
            finish();
            return;
        }

        //find the workout timer button in the layout file and add a listener to open the workout timer activity
        workoutTimerButton = findViewById(R.id.workout_timer_button);
        workoutTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWorkoutTimerButtonClick(workoutTimerButton);
            }
        });

        exercisesButton = findViewById(R.id.exercises_button);
        exercisesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExercisesButtonClick(exercisesButton);
            }
        });

        nutritionButton = findViewById(R.id.nutrition_button);
        nutritionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNutritionButtonClick(nutritionButton);
            }
        });

    }

    public void onWorkoutTimerButtonClick(View view) {
        Intent intent = new Intent(AppMainPageActivity.this, WorkoutTimerActivity.class);
        startActivity(intent);
    }

    public void onExercisesButtonClick(View view) {
        Intent intent = new Intent(AppMainPageActivity.this, ExercisesActivity.class);
        startActivity(intent);
    }

    public void onNutritionButtonClick(View view) {
        Intent intent = new Intent(AppMainPageActivity.this, NutritionActivity.class);
        startActivity(intent);
    }

}