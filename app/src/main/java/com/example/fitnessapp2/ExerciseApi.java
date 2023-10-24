package com.example.fitnessapp2;

//retrieving exercises from an API
public interface ExerciseApi {
    void getExercises(String muscle, ExerciseCallback callback);

    void setExerciseCallback(ExerciseCallback callback);
}