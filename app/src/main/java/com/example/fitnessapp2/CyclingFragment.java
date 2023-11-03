package com.example.fitnessapp2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CyclingFragment extends Fragment {

    private EditText routeInput;
    private EditText durationInput;
    private EditText averageSpeedInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cycling, container, false);

        routeInput = view.findViewById(R.id.cycling_route_input);
        durationInput = view.findViewById(R.id.cycling_duration_input);
        averageSpeedInput = view.findViewById(R.id.cycling_avg_speed_input);

        // Logic to handle user input can be added here

        return view;
    }

    // Methods to retrieve user input data
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