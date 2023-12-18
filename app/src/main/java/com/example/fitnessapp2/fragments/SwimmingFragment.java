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

public class SwimmingFragment extends Fragment {
    private EditText lapCountInput, poolSizeInput, swimTimeInput;
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

    public Map<String, Object> getWorkoutDetails() {
        Map<String, Object> details = new HashMap<>();
        int lapCount = lapCountInput.getText().toString().isEmpty() ? 0 : Integer.parseInt(lapCountInput.getText().toString());
        float poolSize = poolSizeInput.getText().toString().isEmpty() ? 0f : Float.parseFloat(poolSizeInput.getText().toString());
        String swimTime = swimTimeInput.getText().toString().isEmpty() ? "0" : swimTimeInput.getText().toString();

        details.put("lapCount", lapCount);
        details.put("poolSize", poolSize);
        details.put("swimTime", swimTime);
        return details;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCurrentDate(String date) {
        this.currentDate = date;
    }

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