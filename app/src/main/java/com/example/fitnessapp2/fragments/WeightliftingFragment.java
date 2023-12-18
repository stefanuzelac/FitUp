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
import java.util.HashMap;
import java.util.Map;

public class WeightliftingFragment extends Fragment {
    private EditText nameInput, setsInput, repsInput, weightInput;
    private int userId;
    private String currentDate;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt("userId");
            currentDate = getArguments().getString("currentDate");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weightlifting, container, false);
        nameInput = view.findViewById(R.id.weightlifting_name_input);
        setsInput = view.findViewById(R.id.weightlifting_sets_input);
        repsInput = view.findViewById(R.id.weightlifting_reps_input);
        weightInput = view.findViewById(R.id.weightlifting_weight_input);
        return view;
    }

    // Method to collect workout details
    public Map<String, Object> getWorkoutDetails() {
        Map<String, Object> details = new HashMap<>();
        String name = nameInput.getText().toString().isEmpty() ? "N/A" : nameInput.getText().toString();
        int sets = setsInput.getText().toString().isEmpty() ? 0 : Integer.parseInt(setsInput.getText().toString());
        int reps = repsInput.getText().toString().isEmpty() ? 0 : Integer.parseInt(repsInput.getText().toString());
        double weight = weightInput.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(weightInput.getText().toString());

        details.put("name", name);
        details.put("sets", sets);
        details.put("reps", reps);
        details.put("weight", weight);
        return details;
    }

    // Methods to set userId and currentDate
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCurrentDate(String date) {
        this.currentDate = date;
    }

    public String getName() {
        return nameInput.getText().toString();
    }
}