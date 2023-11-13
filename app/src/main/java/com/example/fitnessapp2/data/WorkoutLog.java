package com.example.fitnessapp2.data;

public class WorkoutLog {
    private int id;
    private int userId;
    private String exercise;
    private int sets;
    private int reps;
    private double weight;
    private String date;

    // Constructor
    public WorkoutLog(int id, int userId, String exercise, int sets, int reps, double weight, String date) {
        this.id = id;
        this.userId = userId;
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.date = date;
    }

    // Getters and Setters for each field
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

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // You may also want to override toString() for easy logging
    @Override
    public String toString() {
        return "WorkoutLog{" +
                "id=" + id +
                ", userId=" + userId +
                ", exercise='" + exercise + '\'' +
                ", sets=" + sets +
                ", reps=" + reps +
                ", weight=" + weight +
                ", date='" + date + '\'' +
                '}';
    }
}
