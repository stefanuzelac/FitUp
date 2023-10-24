package com.example.fitnessapp2;

import java.util.List;

public interface NutritionCallback {
    void onSuccess(List<NutritionInfo> nutritionList);
    void onError(Exception e);
}
