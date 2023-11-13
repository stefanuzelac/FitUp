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

public class RunningFragment extends Fragment {

    private EditText distanceInput;
    private EditText timeInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_running, container, false);

        distanceInput = view.findViewById(R.id.running_distance_input);
        timeInput = view.findViewById(R.id.running_time_input);

        // Add logic to handle user input if needed

        return view;
    }

    // Include methods to retrieve user input data from the fragment
    public double getDistance() {
        return Double.parseDouble(distanceInput.getText().toString());
    }

    public String getTime() {
        return timeInput.getText().toString();
    }
}