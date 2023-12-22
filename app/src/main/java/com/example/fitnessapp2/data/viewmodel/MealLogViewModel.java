package com.example.fitnessapp2.data.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fitnessapp2.data.model.MealLog;

public class MealLogViewModel extends ViewModel {
    private final MutableLiveData<MealLog> mealLogLiveData = new MutableLiveData<>();

    public void setMealLog(MealLog mealLog) {
        mealLogLiveData.setValue(mealLog);
    }

    public LiveData<MealLog> getMealLog() {
        return mealLogLiveData;
    }
}
