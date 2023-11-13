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

public class SwimmingFragment extends Fragment {

    private EditText lapCountInput;
    private EditText poolSizeInput;
    private EditText swimTimeInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swimming, container, false);

        lapCountInput = view.findViewById(R.id.swimming_lap_count_input);
        poolSizeInput = view.findViewById(R.id.swimming_pool_size_input);
        swimTimeInput = view.findViewById(R.id.swimming_time_input);

        // Logic to handle user input can be added here

        return view;
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
