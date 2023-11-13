package com.example.fitnessapp2.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String PREFERENCES_FILE_KEY = "com.fitnessapp2.preferences";
    private static final String REMEMBER_ME_KEY = "remember_me";
    private static final String EMAIL_KEY = "email";

    private SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
    }

    public boolean getRememberMePreference() {
        return sharedPreferences.getBoolean(REMEMBER_ME_KEY, false);
    }

    public void setRememberMePreference(boolean rememberMe) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(REMEMBER_ME_KEY, rememberMe);
        editor.apply();
    }

    public String getEmailPreference() {
        return sharedPreferences.getString(EMAIL_KEY, "");
    }

    public void setEmailPreference(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL_KEY, email);
        editor.apply();
    }

    public void clearPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(EMAIL_KEY);
        editor.remove(REMEMBER_ME_KEY);
        editor.apply();
    }
}