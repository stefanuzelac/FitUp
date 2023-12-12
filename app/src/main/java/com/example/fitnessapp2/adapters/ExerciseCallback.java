package com.example.fitnessapp2.adapters;

import com.example.fitnessapp2.data.model.Exercise;

import java.util.List;

public interface ExerciseCallback {

    void onSuccess(List<Exercise> exercises);

    void onError(Exception e);
}