package com.example.fitnessapp2.data.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.example.fitnessapp2.data.database.dao.WorkoutDAO;

public class ProgressTrackerViewModelFactory implements ViewModelProvider.Factory {
    private final WorkoutDAO workoutDAO;

    public ProgressTrackerViewModelFactory(WorkoutDAO workoutDAO) {
        this.workoutDAO = workoutDAO;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProgressTrackerViewModel.class)) {
            return (T) new ProgressTrackerViewModel(workoutDAO);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

