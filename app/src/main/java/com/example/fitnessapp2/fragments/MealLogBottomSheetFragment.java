package com.example.fitnessapp2.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessapp2.R;
import com.example.fitnessapp2.data.model.MealLog;
import com.example.fitnessapp2.data.viewmodel.MealLogViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MealLogBottomSheetFragment extends BottomSheetDialogFragment {
    private Spinner mealTypeSpinner;
    private EditText fatsInput, carbsInput, proteinInput;
    private Button datePickerButton, submitButton;
    private MealLogViewModel viewModel;
    private int userId;
    private String selectedDate;

    public static MealLogBottomSheetFragment newInstance(int userId) {
        MealLogBottomSheetFragment fragment = new MealLogBottomSheetFragment();
        Bundle args = new Bundle();
        args.putInt("USER_ID", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_meal_log_bottom_sheet, container, false);

        mealTypeSpinner = view.findViewById(R.id.mealTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.meal_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeSpinner.setAdapter(adapter);

        if (getArguments() != null) {
            userId = getArguments().getInt("USER_ID");
        }
        fatsInput = view.findViewById(R.id.fatsInput);
        carbsInput = view.findViewById(R.id.carbsInput);
        proteinInput = view.findViewById(R.id.proteinInput);
        datePickerButton = view.findViewById(R.id.datePickerButton);
        datePickerButton.setOnClickListener(v -> showDatePickerDialog());

        submitButton = view.findViewById(R.id.submitMealLogButton);
        submitButton.setOnClickListener(v -> submitMealLog());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(requireActivity()).get(MealLogViewModel.class);
    }

    private void submitMealLog() {
        String mealType = mealTypeSpinner.getSelectedItem().toString();
        String fatsStr = fatsInput.getText().toString();
        String carbsStr = carbsInput.getText().toString();
        String proteinStr = proteinInput.getText().toString();

        // Validation
        if (fatsStr.isEmpty() || carbsStr.isEmpty() || proteinStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double fats, carbs, protein;
        try {
            fats = Double.parseDouble(fatsStr);
            carbs = Double.parseDouble(carbsStr);
            protein = Double.parseDouble(proteinStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if a date has been selected
        if (selectedDate == null || selectedDate.isEmpty()) {
            Toast.makeText(getContext(), "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the MealLog object with the selected date
        MealLog mealLog = new MealLog(-1, userId, mealType, fats, carbs, protein, selectedDate);
        viewModel.setMealLog(mealLog);
        dismiss();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Handle the date selection
                    calendar.set(year1, monthOfYear, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    selectedDate = dateFormat.format(calendar.getTime()); // Store the date
                }, year, month, day);
        datePickerDialog.show();
    }
}