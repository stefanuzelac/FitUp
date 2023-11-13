package com.example.fitnessapp2.api;

import com.example.fitnessapp2.adapters.ExerciseCallback;

//retrieving exercises from an API
public interface ExerciseApi {
    void getExercises(String muscle, ExerciseCallback callback);

    void setExerciseCallback(ExerciseCallback callback);
}