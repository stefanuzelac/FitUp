package com.example.fitnessapp2;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExerciseApiImpl implements ExerciseApi {
    //callback for when exercises are retrieved
    private ExerciseCallback callback;

    //API base URL
    private static final String BASE_URL = "https://exercises-by-api-ninjas.p.rapidapi.com/v1/exercises";

    @Override
    public void getExercises(String muscle, ExerciseCallback callback) {
        //set callback to the passed in callback
        this.callback = callback;

        //start new thread to make the API request and make sure my API is asynchronously done for project
        new Thread(() -> {
            try {
                //using OkHttp librabry create OkHttpClient to handle request
                OkHttpClient client = new OkHttpClient();

                //create new request with muscle parameter and API headers
                Request request = new Request.Builder()
                        .url(BASE_URL + "?muscle=" + muscle)
                        .get()
                        .addHeader("x-rapidapi-key", "fe2b64e3b1mshdb2a4cd7db409f4p1d1355jsn2bb1185b7009")
                        .addHeader("x-rapidapi-host", "exercises-by-api-ninjas.p.rapidapi.com")
                        .build();

                Response response = client.newCall(request).execute();

                String responseString = response.body().string();
                Log.d("ExerciseApiImpl", "API response: " + responseString);

                //create an ObjectMapper object to parse JSON response
                ObjectMapper mapper = new ObjectMapper();
                //read JSON response into a JsonNode
                JsonNode root = mapper.readTree(responseString);

                List<Exercise> exercises = new ArrayList<>();

                //loop through JsonNode to create Exercise objects
                for (JsonNode exerciseNode : root) {

                    //get the name, difficulty, and instructions from the exerciseNode
                    String name = exerciseNode.get("name").asText();
                    String difficulty = exerciseNode.get("difficulty").asText();
                    String instructions = exerciseNode.get("instructions").asText();

                    //create a new Exercise object with the name
                    exercises.add(new Exercise(name));

                    //set the difficulty and instructions on the last Exercise object in the list
                    //need to subtract 1 to get the index of the last element since I am using .size to get number of objects in list
                    exercises.get(exercises.size() - 1).setDifficulty(difficulty);
                    exercises.get(exercises.size() - 1).setInstructions(instructions);
                }

                //log the retrieved exercises so I can check if it's working and call the onSuccess method on the callback
                Log.d("ExerciseApiImpl", "Retrieved exercises: " + exercises);
                callback.onSuccess(exercises);
            } catch (IOException e) {
                //call the onError method on the callback if there was an error
                callback.onError(e);
            }
        }).start();
    }

    @Override
    public void setExerciseCallback(ExerciseCallback callback) {
        //set the callback to the passed in callback
        this.callback = callback;
    }

}