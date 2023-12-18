package com.example.fitnessapp2.data.model;

import java.util.HashMap;
import java.util.Map;

public class WorkoutLog {
    private int id;
    private int userId;
    private String workoutType; // e.g., "Cycling", "Running", "Weightlifting", "Swimming"
    private String date;
    private Map<String, Object> workoutDetails;

    public WorkoutLog() {
        this.workoutDetails = new HashMap<>();
    }

    public WorkoutLog(int id, int userId, String workoutType, String date, Map<String, Object> details) {
        this.id = id;
        this.userId = userId;
        this.workoutType = workoutType;
        this.date = date;
        this.workoutDetails = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Object> getWorkoutDetails() {
        return workoutDetails;
    }

    public void setWorkoutDetails(Map<String, Object> workoutDetails) {
        this.workoutDetails = workoutDetails;
    }

    // Methods to add specific workout data
    public void addDetail(String key, Object value) {
        this.workoutDetails.put(key, value);
    }

    public Object getDetail(String key) {
        return this.workoutDetails.get(key);
    }

    // Override toString for easy logging
    @Override
    public String toString() {
        return "WorkoutLog{" +
                "id=" + id +
                ", userId=" + userId +
                ", workoutType='" + workoutType + '\'' +
                ", date='" + date + '\'' +
                ", workoutDetails=" + workoutDetails +
                '}';
    }
}