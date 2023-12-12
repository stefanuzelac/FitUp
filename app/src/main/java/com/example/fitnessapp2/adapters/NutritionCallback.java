package com.example.fitnessapp2.adapters;

import com.example.fitnessapp2.data.model.Food;

import java.util.List;

public interface NutritionCallback {
    void onSuccess(List<Food> nutritionList);
    void onError(Exception e);
}
