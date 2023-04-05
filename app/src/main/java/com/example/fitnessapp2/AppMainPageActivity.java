package com.example.fitnessapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AppMainPageActivity extends BaseActivity {
    //declaring my buttons
    private Button workoutTimerButton, exercisesButton, nutritionButton;
    int loggedInUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main_page);
        setupToolbarAndDrawer();

        //retrieve the loggedInUserId from the Intent extras and pass it to the BaseActivity
        loggedInUserId = getIntent().getIntExtra("loggedInUserId", -1);
        setLoggedInUserId(loggedInUserId); // Call the new method to set loggedInUserId in BaseActivity

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