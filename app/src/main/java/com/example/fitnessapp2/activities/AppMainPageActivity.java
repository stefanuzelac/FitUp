package com.example.fitnessapp2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.cardview.widget.CardView;

import com.example.fitnessapp2.R;
import com.example.fitnessapp2.data.User;
import com.example.fitnessapp2.utils.UserSessionManager;

public class AppMainPageActivity extends BaseActivity {
    //declaring my buttons
    private CardView cardWorkoutTimerButton, cardExercisesButton, cardNutritionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main_page);
        setupToolbarAndDrawer();

        // Get the currentUser from UserSessionManager
        User currentUser = UserSessionManager.getInstance(this).getCurrentUser();
        if (currentUser == null) {
            // Handle this case, maybe finish the activity or redirect to login
            finish();
            return;
        }

        //find the workout timer button in the layout file and add a listener to open the workout timer activity
        cardWorkoutTimerButton = findViewById(R.id.card_workout_timer_button);
        cardWorkoutTimerButton.setOnClickListener(v -> onWorkoutTimerButtonClick(cardWorkoutTimerButton));

        cardExercisesButton = findViewById(R.id.card_exercises_button);
        cardExercisesButton.setOnClickListener(v -> onExercisesButtonClick(cardExercisesButton));

        cardNutritionButton = findViewById(R.id.card_nutrition_button);
        cardNutritionButton.setOnClickListener(v -> onNutritionButtonClick(cardNutritionButton));

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