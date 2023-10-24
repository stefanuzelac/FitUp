package com.example.fitnessapp2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ExercisesActivity extends BaseActivity implements ExerciseCallback {
    private ExerciseApi exerciseApi;
    private RecyclerView exercisesRecyclerView;
    private ExercisesAdapter exercisesAdapter;
    private Button bicepsButton, tricepsButton, chestButton, latsButton,
            middleBackButton, lowerBackButton, glutesButton, hamstringsButton, quadricepsButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        setupToolbarAndDrawer();
        //set up the RecyclerView and adapter
        exercisesRecyclerView = findViewById(R.id.exercises_recycler_view);
        //set the layout manager
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exercisesAdapter = new ExercisesAdapter(new ArrayList<>());
        exercisesRecyclerView.setAdapter(exercisesAdapter);

        //set up the exercise API and listener for each body button
        exerciseApi = new ExerciseApiImpl();
        exerciseApi.setExerciseCallback(this);

        bicepsButton = findViewById(R.id.biceps_button);
        bicepsButton.setOnClickListener(view -> onBicepsButtonClick());

        tricepsButton = findViewById(R.id.triceps_button);
        tricepsButton.setOnClickListener(view -> onTricepsButtonClick());

        chestButton = findViewById(R.id.chest_button);
        chestButton.setOnClickListener(view -> onChestButtonClick());

        latsButton = findViewById(R.id.lats_button);
        latsButton.setOnClickListener(view -> onLatsButtonClick());

        middleBackButton = findViewById(R.id.middle_back_button);
        middleBackButton.setOnClickListener(view -> onMiddleBackButtonClick());

        lowerBackButton = findViewById(R.id.lower_back_button);
        lowerBackButton.setOnClickListener(view -> onLowerBackButtonClick());

        glutesButton = findViewById(R.id.glutes_button);
        glutesButton.setOnClickListener(view -> onGlutesButtonClick());

        hamstringsButton = findViewById(R.id.hamstrings_button);
        hamstringsButton.setOnClickListener(view -> onHamstringsButtonClick());

        quadricepsButton = findViewById(R.id.quadriceps_button);
        quadricepsButton.setOnClickListener(view -> onQuadricepsButtonClick());
    }
    //if api request succeeded
    @Override
    public void onSuccess(List<Exercise> exerciseList) {
        //make sure i am running on the UI thread
        runOnUiThread(() -> {
            //update the adapter with the new list of exercises received from API
            exercisesAdapter.updateExercises(exerciseList);
            //notify the adapter that the data has changed
            exercisesAdapter.notifyDataSetChanged();
            //print a log message to indicate that the exercises list was updated - debugging purposes
            Log.d("ExercisesActivity", "Updated exercises list with "
                    + exerciseList.size()
                    + " exercises");
        });
    }
    @Override
    public void onError(Exception e) {
        runOnUiThread(() -> Toast.makeText(this, "Error while getting exercises: "
                + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
    //when the biceps button is clicked make a call to the API to get a list of exercises for biceps, same procedure for other buttons
    public void onBicepsButtonClick() {
        exerciseApi.getExercises("biceps", this);
    }
    public void onTricepsButtonClick() {
        exerciseApi.getExercises("triceps", this);
    }
    public void onChestButtonClick() {
        exerciseApi.getExercises("chest", this);
    }
    public void onLatsButtonClick() {
        exerciseApi.getExercises("lats", this);
    }
    public void onMiddleBackButtonClick() {
        exerciseApi.getExercises("middle_back", this);
    }
    public void onLowerBackButtonClick() {
        exerciseApi.getExercises("lower_back", this);
    }
    public void onGlutesButtonClick() {
        exerciseApi.getExercises("glutes", this);
    }
    public void onHamstringsButtonClick() {
        exerciseApi.getExercises("hamstrings", this);
    }
    public void onQuadricepsButtonClick() {
        exerciseApi.getExercises("quadriceps", this);
    }

}
