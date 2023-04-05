package com.example.fitnessapp2;

//retrieving exercises from an API
public interface ExerciseApi {
    //retrieving a list of exercises for a specific muscle from the API
    void getExercises(String muscle, ExerciseCallback callback);

    //setting the ExerciseCallback instance for handling success and error responses
    void setExerciseCallback(ExerciseCallback callback);
}