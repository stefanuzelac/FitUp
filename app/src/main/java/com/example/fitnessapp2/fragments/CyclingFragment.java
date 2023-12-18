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

public class CyclingFragment extends Fragment {
    private EditText routeInput, durationInput, averageSpeedInput;
    private int userId;
    private String currentDate;

    public static CyclingFragment newInstance(int userId, String currentDate) {
        CyclingFragment fragment = new CyclingFragment();
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
        View view = inflater.inflate(R.layout.fragment_cycling, container, false);
        routeInput = view.findViewById(R.id.cycling_route_input);
        durationInput = view.findViewById(R.id.cycling_duration_input);
        averageSpeedInput = view.findViewById(R.id.cycling_avg_speed_input);
        return view;
    }

    public Map<String, Object> getWorkoutDetails() {
        Map<String, Object> details = new HashMap<>();
        String route = routeInput.getText().toString().isEmpty() ? "N/A" : routeInput.getText().toString();
        String duration = durationInput.getText().toString().isEmpty() ? "0" : durationInput.getText().toString();
        float averageSpeed = averageSpeedInput.getText().toString().isEmpty() ? 0f : Float.parseFloat(averageSpeedInput.getText().toString());

        details.put("route", route);
        details.put("duration", duration);
        details.put("averageSpeed", averageSpeed);
        return details;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCurrentDate(String date) {
        this.currentDate = date;
    }

    public String getRoute() {
        return routeInput.getText().toString();
    }

    public String getDuration() {
        return durationInput.getText().toString();
    }

    public float getAverageSpeed() {
        return Float.parseFloat(averageSpeedInput.getText().toString());
    }
}