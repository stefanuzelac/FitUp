package com.example.fitnessapp2.api;

import com.example.fitnessapp2.adapters.NutritionCallback;

public interface NutritionApi {
    void getNutritionInfo(String searchTerm, NutritionCallback callback);
    void setNutritionCallback(NutritionCallback callback);
}
