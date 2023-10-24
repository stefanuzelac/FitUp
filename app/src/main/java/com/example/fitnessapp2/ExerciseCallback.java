package com.example.fitnessapp2;

import java.util.List;

public interface ExerciseCallback {

    void onSuccess(List<Exercise> exercises);

    void onError(Exception e);
}