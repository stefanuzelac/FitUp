package com.example.fitnessapp2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnessapp2.R;
import com.example.fitnessapp2.data.WorkoutDataPass;
import com.example.fitnessapp2.data.model.WorkoutLog;

import java.util.HashMap;
import java.util.Map;

public class WeightliftingFragment extends Fragment {
    private EditText nameInput, setsInput, repsInput, weightInput;
    private WorkoutDataPass dataPasser;
    private int userId;
    private String currentDate;
    private boolean isViewCreated = false;

    // Factory method to create a new instance of the fragment
    public static WeightliftingFragment newInstance(int userId, String currentDate) {
        WeightliftingFragment fragment = new WeightliftingFragment();
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        args.putString("currentDate", currentDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof WorkoutDataPass) {
            dataPasser = (WorkoutDataPass) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement WorkoutDataPass");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt("userId");
            currentDate = getArguments().getString("currentDate");
        }
        Log.d("WeightliftingFragment", "onCreate: started");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weightlifting, container, false);
        nameInput = view.findViewById(R.id.weightlifting_name_input);
        setsInput = view.findViewById(R.id.weightlifting_sets_input);
        repsInput = view.findViewById(R.id.weightlifting_reps_input);
        weightInput = view.findViewById(R.id.weightlifting_weight_input);
        isViewCreated = true;
        Log.d("WeightliftingFragment", "onCreateView: started");
        return view;

    }

    public boolean isViewCreated() {
        return isViewCreated;
    }

    // Method to collect workout details
    public Map<String, Object> getWorkoutDetails() {
        Map<String, Object> details = new HashMap<>();
        if (isViewCreated) {
            String name = nameInput != null && !nameInput.getText().toString().isEmpty() ? nameInput.getText().toString() : "N/A";
            int sets = setsInput != null && !setsInput.getText().toString().isEmpty() ? Integer.parseInt(setsInput.getText().toString()) : 0;
            int reps = repsInput != null && !repsInput.getText().toString().isEmpty() ? Integer.parseInt(repsInput.getText().toString()) : 0;
            double weight = weightInput != null && !weightInput.getText().toString().isEmpty() ? Double.parseDouble(weightInput.getText().toString()) : 0.0;

            details.put("name", name);
            details.put("sets", sets);
            details.put("reps", reps);
            details.put("weight", weight);
            Log.d("WeightliftingFragment", "Details: Name=" + name + ", Sets=" + sets + ", Reps=" + reps + ", Weight=" + weight);
        } else {
            Log.d("WeightliftingFragment", "View is not created yet.");
        }
        return details;
    }

    // Methods to set userId and currentDate
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCurrentDate(String date) {
        this.currentDate = date;
    }

    // Method to pass data to activity
    public void passDataToActivity() {
        if (dataPasser != null && isViewCreated) {
            WorkoutLog workoutLog = new WorkoutLog();
            workoutLog.setUserId(userId);
            workoutLog.setDate(currentDate);
            workoutLog.setWorkoutType("Weightlifting");
            workoutLog.setWorkoutDetails(getWorkoutDetails());

            dataPasser.OnWorkoutDataPass(workoutLog);
        }
    }



    // Include methods to retrieve user input data from the fragment
    public String getName() {
        return nameInput.getText().toString();
    }

    public int getSets() {
        return Integer.parseInt(setsInput.getText().toString());
    }

    public int getReps() {
        return Integer.parseInt(repsInput.getText().toString());
    }

    public double getWeight() {
        return Double.parseDouble(weightInput.getText().toString());
    }
}