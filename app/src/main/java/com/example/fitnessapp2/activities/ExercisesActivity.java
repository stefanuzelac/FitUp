package com.example.fitnessapp2.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp2.data.Exercise;
import com.example.fitnessapp2.api.ExerciseApi;
import com.example.fitnessapp2.api.ExerciseApiImpl;
import com.example.fitnessapp2.adapters.ExerciseCallback;
import com.example.fitnessapp2.adapters.ExercisesAdapter;
import com.example.fitnessapp2.R;

import java.util.ArrayList;
import java.util.List;

public class ExercisesActivity extends BaseActivity implements ExerciseCallback {
    private ExerciseApi exerciseApi;
    private RecyclerView exercisesRecyclerView;
    private ExercisesAdapter exercisesAdapter;
    private CardView bicepsCard, tricepsCard, chestCard, latsCard,
            middleBackCard, lowerBackCard, glutesCard, hamstringsCard, quadricepsCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        setupToolbarAndDrawer();

        // set up the RecyclerView and adapter
        exercisesRecyclerView = findViewById(R.id.exercises_recycler_view);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exercisesAdapter = new ExercisesAdapter(new ArrayList<>());
        exercisesRecyclerView.setAdapter(exercisesAdapter);

        //set up the exercise API and listener for each body button
        exerciseApi = new ExerciseApiImpl();
        exerciseApi.setExerciseCallback(this);

        bicepsCard = findViewById(R.id.biceps_card);
        bicepsCard.setOnClickListener(view -> onBicepsButtonClick());

        tricepsCard = findViewById(R.id.triceps_card);
        tricepsCard.setOnClickListener(view -> onTricepsButtonClick());

        chestCard = findViewById(R.id.chest_card);
        chestCard.setOnClickListener(view -> onChestButtonClick());

        latsCard = findViewById(R.id.lats_card);
        latsCard.setOnClickListener(view -> onLatsButtonClick());

        middleBackCard = findViewById(R.id.middle_back_card);
        middleBackCard.setOnClickListener(view -> onMiddleBackButtonClick());

        lowerBackCard = findViewById(R.id.lower_back_card);
        lowerBackCard.setOnClickListener(view -> onLowerBackButtonClick());

        glutesCard = findViewById(R.id.glutes_card);
        glutesCard.setOnClickListener(view -> onGlutesButtonClick());

        hamstringsCard = findViewById(R.id.hamstrings_card);
        hamstringsCard.setOnClickListener(view -> onHamstringsButtonClick());

        quadricepsCard = findViewById(R.id.quadriceps_card);
        quadricepsCard.setOnClickListener(view -> onQuadricepsButtonClick());
    }

    //if api request succeeded
    @Override
    public void onSuccess(List<Exercise> exerciseList) {
        //make sure I am running on the UI thread
        runOnUiThread(() -> {
            Log.d("DEBUG", "onSuccess called with " + exerciseList.size() + " exercises");

            exercisesRecyclerView.setLayoutManager(null);
            exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            exercisesAdapter.updateExercises(exerciseList);
            new Handler().postDelayed(() -> exercisesRecyclerView.scrollToPosition(0), 100);

            Log.d("DEBUG", "RecyclerView should now be scrolled to the top");
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
