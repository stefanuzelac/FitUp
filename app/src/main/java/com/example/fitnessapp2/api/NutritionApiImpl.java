package com.example.fitnessapp2.api;

import android.util.Log;

import com.example.fitnessapp2.data.Food;
import com.example.fitnessapp2.adapters.NutritionCallback;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NutritionApiImpl implements NutritionApi {
    private NutritionCallback callback;
    private static final String BASE_URL ="https://nutrition-by-api-ninjas.p.rapidapi.com/v1/nutrition";

    public void getNutritionInfo(String searchTerm, NutritionCallback callback) {
        this.callback = callback;

        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(BASE_URL + "?query=" + searchTerm)
                        .get()
                        .addHeader("X-RapidAPI-Key", "fe2b64e3b1mshdb2a4cd7db409f4p1d1355jsn2bb1185b7009")
                        .addHeader("X-RapidAPI-Host", "nutrition-by-api-ninjas.p.rapidapi.com")
                        .build();

                Response response = client.newCall(request).execute();

                String responseString = response.body().string();
                Log.d("NutritionApiImpl", "API response: " + responseString);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(responseString);

                List<Food> nutritionList = new ArrayList<>();

                for (JsonNode nutritionNode : root) {
                    //get the required fields from the nutritionNode
                    String name = nutritionNode.get("name").asText();
                    double calories = nutritionNode.get("calories").asDouble();
                    double serving_size_g = nutritionNode.get("serving_size_g").asDouble();
                    double fat_total_g = nutritionNode.get("fat_total_g").asDouble();
                    double fat_saturated_g = nutritionNode.get("fat_saturated_g").asDouble();
                    double protein_g = nutritionNode.get("protein_g").asDouble();
                    double sodium_mg = nutritionNode.get("sodium_mg").asDouble();
                    double potassium_mg = nutritionNode.get("potassium_mg").asDouble();
                    double cholesterol_mg = nutritionNode.get("cholesterol_mg").asDouble();
                    double carbohydrates_total_g = nutritionNode.get("carbohydrates_total_g").asDouble();
                    double fiber_g = nutritionNode.get("fiber_g").asDouble();
                    double sugar_g = nutritionNode.get("sugar_g").asDouble();

                    //creating a new Food object with the required fields
                    nutritionList.add(new Food(
                            name,
                            calories,
                            serving_size_g,
                            fat_total_g,
                            fat_saturated_g,
                            protein_g,
                            sodium_mg,
                            potassium_mg,
                            cholesterol_mg,
                            carbohydrates_total_g,
                            fiber_g,
                            sugar_g));
                }
                //log retrieved nutrition info and call the onSuccess method on callback
                Log.d("NutritionApiImpl", "Retrieved nutrition information: " + nutritionList);
                callback.onSuccess(nutritionList);
            } catch (IOException e) {
                //call  onError method on  callback if there is an error
                callback.onError(e);
            }
        }).start();
    }

    @Override
    public void setNutritionCallback(NutritionCallback callback) {
        //set the callback to the passed in callback
        this.callback = callback;
    }
}
