package com.example.fitnessapp2;

//retrieving nutrition information from API
public interface NutritionApi {
    //retrieving list of nutrition information for a specific food search term from the API
    void getNutritionInfo(String searchTerm, NutritionCallback callback);

    //setting the NutritionCallback instance for handling success and error response
    void setNutritionCallback(NutritionCallback callback);
}
