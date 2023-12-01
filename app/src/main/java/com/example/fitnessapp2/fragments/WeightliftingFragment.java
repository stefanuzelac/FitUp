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

public class WeightliftingFragment extends Fragment {

    private EditText nameInput, setsInput, repsInput, weightInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weightlifting, container, false);

        nameInput = view.findViewById(R.id.weightlifting_name_input);
        setsInput = view.findViewById(R.id.weightlifting_sets_input);
        repsInput = view.findViewById(R.id.weightlifting_reps_input);
        weightInput = view.findViewById(R.id.weightlifting_weight_input);

        // Add logic to handle user input if needed

        return view;
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

