package com.example.fitnessapp2.data;

import java.util.List;

public interface WorkoutDAO {
    boolean insertWorkoutLog (WorkoutLog log);
    List<WorkoutLog> getWorkoutLogsByUserId(int userId);
    boolean updateWorkoutLog (WorkoutLog log);
    void deleteWorkoutLog (int id);
}
