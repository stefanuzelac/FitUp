package com.example.fitnessapp2;

import java.util.List;

//interface for handling success or error responses from the ExerciseApi
public interface ExerciseCallback {
    void onSuccess(List<Exercise> exercises);

    //error response is received from ExerciseApi
    void onError(Exception e);
}