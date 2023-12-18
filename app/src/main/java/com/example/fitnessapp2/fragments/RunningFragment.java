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
import com.example.fitnessapp2.data.model.WorkoutLog;

import java.util.HashMap;
import java.util.Map;

public class RunningFragment extends Fragment {
    private EditText distanceInput, timeInput;
    private int userId;
    private String currentDate;

    public static RunningFragment newInstance(int userId, String currentDate) {
        RunningFragment fragment = new RunningFragment();
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
        View view = inflater.inflate(R.layout.fragment_running, container, false);
        distanceInput = view.findViewById(R.id.running_distance_input);
        timeInput = view.findViewById(R.id.running_time_input);
        return view;
    }

    public Map<String, Object> getWorkoutDetails() {
        Map<String, Object> details = new HashMap<>();
        double distance = distanceInput.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(distanceInput.getText().toString());
        String time = timeInput.getText().toString().isEmpty() ? "0" : timeInput.getText().toString();

        details.put("distance", distance);
        details.put("time", time);
        return details;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCurrentDate(String date) {
        this.currentDate = date;
    }

    public double getDistance() {
        return Double.parseDouble(distanceInput.getText().toString());
    }

    public String getTime() {
        return timeInput.getText().toString();
    }
}