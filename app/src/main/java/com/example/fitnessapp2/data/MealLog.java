package com.example.fitnessapp2.data;

public class MealLog {
    private int id;
    private int userId;
    private String meal;
    private double fats;
    private double carbs;
    private double protein;
    private String date;

    public MealLog(int id, int userId, String meal, double fats, double carbs, double protein, String date) {
        this.id = id;
        this.userId = userId;
        this.meal = meal;
        this.fats = fats;
        this.carbs = carbs;
        this.protein = protein;
        this.date = date;
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

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
