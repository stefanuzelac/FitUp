package com.example.fitnessapp2.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp2.data.database.dao.ExerciseDAO;
import com.example.fitnessapp2.data.database.daoimpl.ExerciseDAOImpl;
import com.example.fitnessapp2.data.model.Exercise;
import com.example.fitnessapp2.adapters.ExercisesAdapter;
import com.example.fitnessapp2.R;

import java.util.ArrayList;
import java.util.List;

public class ExercisesActivity extends BaseActivity {
    private ExerciseDAO exerciseDAO;
    private RecyclerView exercisesRecyclerView;
    private ExercisesAdapter exercisesAdapter;
    private CardView bicepsCard, tricepsCard, chestCard, backCard,
            shouldersCard, legsCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        setupToolbarAndDrawer();

        exerciseDAO = new ExerciseDAOImpl(this);
        // set up the RecyclerView and adapter
        exercisesRecyclerView = findViewById(R.id.exercises_recycler_view);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exercisesAdapter = new ExercisesAdapter(new ArrayList<>());
        exercisesRecyclerView.setAdapter(exercisesAdapter);

        bicepsCard = findViewById(R.id.biceps_card);
        bicepsCard.setOnClickListener(view -> fetchExercises("biceps"));

        tricepsCard = findViewById(R.id.triceps_card);
        tricepsCard.setOnClickListener(view -> fetchExercises("triceps"));

        chestCard = findViewById(R.id.chest_card);
        chestCard.setOnClickListener(view -> fetchExercises("chest"));

        backCard = findViewById(R.id.back_card);
        backCard.setOnClickListener(view -> fetchExercises("back"));

        shouldersCard = findViewById(R.id.shoulders_card);
        shouldersCard.setOnClickListener(view -> fetchExercises("shoulders"));

        legsCard = findViewById(R.id.legs_card);
        legsCard.setOnClickListener(view -> fetchExercises("legs"));
    }

    private void fetchExercises(String muscleGroup) {
        new AsyncTask<Void, Void, List<Exercise>>() {
            @Override
            protected List<Exercise> doInBackground(Void... voids) {
                // Replace with a call to your DAO
                return exerciseDAO.getExercisesByMuscle(muscleGroup);
            }

            @Override
            protected void onPostExecute(List<Exercise> exercises) {
                // Update your adapter with the fetched exercises
                exercisesAdapter.updateExercises(exercises);
            }
        }.execute();
    }

}