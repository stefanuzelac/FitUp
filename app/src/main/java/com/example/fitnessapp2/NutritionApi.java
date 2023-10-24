package com.example.fitnessapp2;

public interface NutritionApi {
    void getNutritionInfo(String searchTerm, NutritionCallback callback);
    void setNutritionCallback(NutritionCallback callback);
}
