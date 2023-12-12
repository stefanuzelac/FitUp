package com.example.fitnessapp2.fragments;

import android.os.Bundle;
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

public class SwimmingFragment extends Fragment {
    private EditText lapCountInput, poolSizeInput, swimTimeInput;
    private WorkoutDataPass dataPasser;
    private int userId;
    private String currentDate;

    public static SwimmingFragment newInstance(int userId, String currentDate) {
        SwimmingFragment fragment = new SwimmingFragment();
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        args.putString("currentDate", currentDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt("userId");
            currentDate = getArguments().getString("currentDate");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swimming, container, false);
        lapCountInput = view.findViewById(R.id.swimming_lap_count_input);
        poolSizeInput = view.findViewById(R.id.swimming_pool_size_input);
        swimTimeInput = view.findViewById(R.id.swimming_time_input);
        return view;
    }

    // Method to collect workout details
    public Map<String, Object> getWorkoutDetails() {
        Map<String, Object> details = new HashMap<>();
        details.put("lapCount", Integer.parseInt(lapCountInput.getText().toString()));
        details.put("poolSize", Float.parseFloat(poolSizeInput.getText().toString()));
        details.put("swimTime", swimTimeInput.getText().toString());
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
        if (dataPasser != null) {
            WorkoutLog workoutLog = new WorkoutLog(0, userId, "Swimming", currentDate, getWorkoutDetails());
            dataPasser.OnWorkoutDataPass(workoutLog);
        }
    }

    // Methods to retrieve user input data
    public int getLapCount() {
        return Integer.parseInt(lapCountInput.getText().toString());
    }

    public float getPoolSize() {
        return Float.parseFloat(poolSizeInput.getText().toString());
    }

    public String getSwimTime() {
        return swimTimeInput.getText().toString();
    }
}