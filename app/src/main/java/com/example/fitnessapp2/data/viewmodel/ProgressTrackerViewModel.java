package com.example.fitnessapp2.data.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fitnessapp2.data.model.WorkoutLog;
import com.example.fitnessapp2.data.database.dao.WorkoutDAO;

import java.util.ArrayList;
import java.util.List;

public class ProgressTrackerViewModel extends ViewModel {
    private final MutableLiveData<List<WorkoutLog>> workoutLogs = new MutableLiveData<>();
    private final MutableLiveData<WorkoutLog> newWorkoutLog = new MutableLiveData<>();
    private String selectedDate;
    private final WorkoutDAO workoutDAO;
    private int userId;

    // Constructor
    public ProgressTrackerViewModel(WorkoutDAO workoutDAO) {
        this.workoutDAO = workoutDAO;
    }

    public LiveData<List<WorkoutLog>> getWorkoutLogs() {
        return workoutLogs;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        loadWorkoutLogs();
    }

    public void setSelectedDate(String date) {
        this.selectedDate = date;
        loadWorkoutLogs();
    }

    private void loadWorkoutLogs() {
        new Thread(() -> {
            try {
                List<WorkoutLog> logs = workoutDAO.getWorkoutLogsByUserIdAndDate(userId, selectedDate);
                if(logs.isEmpty()){
                    workoutLogs.postValue(new ArrayList<>()); // Post empty list if no logs found
                } else {
                    workoutLogs.postValue(logs);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Handle the error appropriately
            }
        }).start();
    }

    public void addNewWorkoutLog(WorkoutLog log) {
        new Thread(() -> {
            boolean insertResult = workoutDAO.insertWorkoutLog(log);
            if (insertResult) {
                // If insert is successful, reload logs to update LiveData
                loadWorkoutLogs();
            }
        }).start();
    }

    public LiveData<WorkoutLog> getNewWorkoutLog() {
        return newWorkoutLog;
    }
}