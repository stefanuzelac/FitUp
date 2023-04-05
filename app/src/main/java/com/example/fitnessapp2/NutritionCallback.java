package com.example.fitnessapp2;

import java.util.List;

// Interface for handling success or error responses from the NutritionApi
public interface NutritionCallback {
    void onSuccess(List<NutritionInfo> nutritionList);

    // Error response is received from NutritionApi
    void onError(Exception e);
}
