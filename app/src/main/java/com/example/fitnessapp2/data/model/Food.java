package com.example.fitnessapp2.data.model;

//nutrition object
public class Food {
    private String name;
    private double calories, serving_size_g, fat_total_g, fat_saturated_g, protein_g,
            sodium_mg, potassium_mg, cholesterol_mg, carbohydrates_total_g, fiber_g, sugar_g;

    public Food(String name, double calories, double serving_size_g, double fat_total_g,
                double fat_saturated_g, double protein_g, double sodium_mg,
                double potassium_mg, double cholesterol_mg, double carbohydrates_total_g,
                double fiber_g, double sugar_g) {
        this.name = name;
        this.calories = calories;
        this.serving_size_g = serving_size_g;
        this.fat_total_g = fat_total_g;
        this.fat_saturated_g = fat_saturated_g;
        this.protein_g = protein_g;
        this.sodium_mg = sodium_mg;
        this.potassium_mg = potassium_mg;
        this.cholesterol_mg = cholesterol_mg;
        this.carbohydrates_total_g = carbohydrates_total_g;
        this.fiber_g = fiber_g;
        this.sugar_g = sugar_g;
    }

    public String getName() {
        return name;
    }

    public double getCalories() {
        return calories;
    }

    public double getServing_size_g() {
        return serving_size_g;
    }

    public double getFat_total_g() {
        return fat_total_g;
    }

    public double getFat_saturated_g() {
        return fat_saturated_g;
    }

    public double getProtein_g() {
        return protein_g;
    }

    public double getSodium_mg() {
        return sodium_mg;
    }

    public double getPotassium_mg() {
        return potassium_mg;
    }

    public double getCholesterol_mg() {
        return cholesterol_mg;
    }

    public double getCarbohydrates_total_g() {
        return carbohydrates_total_g;
    }

    public double getFiber_g() {
        return fiber_g;
    }

    public double getSugar_g() {
        return sugar_g;
    }
}
