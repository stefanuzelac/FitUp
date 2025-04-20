package com.example.fitnessapp2.data.database.dao;

import com.example.fitnessapp2.data.model.Exercise;

import java.util.List;

public interface ExerciseDAO {
    List<Exercise> getExercisesByMuscle(String muscleGroup);
}
