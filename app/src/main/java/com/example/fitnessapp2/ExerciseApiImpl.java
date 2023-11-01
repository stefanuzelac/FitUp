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
    private ExerciseCallback callback;
    private static final String BASE_URL = "https://exercises-by-api-ninjas.p.rapidapi.com/v1/exercises";

    @Override
    public void getExercises(String muscle, ExerciseCallback callback) {
        this.callback = callback;

        new Thread(() -> {
            try {
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
                Log.d("API_RESPONSE", "Response for " + muscle + ": " + responseString);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(responseString);

                List<Exercise> exercises = new ArrayList<>();

                for (JsonNode exerciseNode : root) {

                    String name = exerciseNode.get("name").asText();
                    String difficulty = exerciseNode.get("difficulty").asText();
                    String instructions = exerciseNode.get("instructions").asText();

                    exercises.add(new Exercise(name));

                    exercises.get(exercises.size() - 1).setDifficulty(difficulty);
                    exercises.get(exercises.size() - 1).setInstructions(instructions);
                }

                callback.onSuccess(exercises);
            } catch (IOException e) {
                callback.onError(e);
            }
        }).start();
    }
    @Override    public void setExerciseCallback(ExerciseCallback callback) {
        //set the callback to the passed in callback
        this.callback = callback;
    }
}