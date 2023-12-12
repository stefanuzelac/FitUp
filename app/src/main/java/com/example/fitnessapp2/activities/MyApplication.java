package com.example.fitnessapp2.activities;

import android.app.Application;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the ThreeTenABP library
        AndroidThreeTen.init(this);
    }
}
