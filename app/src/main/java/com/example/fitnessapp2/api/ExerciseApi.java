package com.example.fitnessapp2.api;

import com.example.fitnessapp2.adapters.ExerciseCallback;

public interface ExerciseApi {
    void getExercises(String muscle, ExerciseCallback callback);

    void setExerciseCallback(ExerciseCallback callback);
}