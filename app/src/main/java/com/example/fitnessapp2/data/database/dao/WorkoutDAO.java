package com.example.fitnessapp2.data.database.dao;

import com.example.fitnessapp2.data.model.WorkoutLog;

import java.util.List;

public interface WorkoutDAO {
    boolean insertWorkoutLog (WorkoutLog log);
    List<WorkoutLog> getWorkoutLogsByUserId(int userId);
    List<WorkoutLog> getWorkoutLogsByUserIdAndDate(int userId, String date);
    boolean updateWorkoutLog (WorkoutLog log);
    void deleteWorkoutLog (int id);
}
