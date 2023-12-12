package com.example.fitnessapp2.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1; // Updated database version
    private static final String DATABASE_NAME = "FitUp.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating users table
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, last_name TEXT, email TEXT, password TEXT, phone TEXT, dob TEXT, age INTEGER, gender TEXT, height INTEGER, weight REAL, profile_picture TEXT, remember_me INTEGER DEFAULT 0)");

        // Creating workout_logs table with workout_type column
        db.execSQL("CREATE TABLE workout_logs (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, workout_type TEXT, exercise TEXT, sets INTEGER, reps INTEGER, weight REAL, date TEXT, workout_details TEXT, FOREIGN KEY(user_id) REFERENCES users(id))");

        // Creating meal_logs table
        db.execSQL("CREATE TABLE meal_logs (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, meal TEXT, fats DOUBLE, carbs DOUBLE, protein DOUBLE, date TEXT, FOREIGN KEY(user_id) REFERENCES users(id))");
    }

    // The onUpgrade method is not necessary at this stage of development
    // and can be implemented later when database versioning becomes important.

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method will be implemented later
    }

    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("users", null, null);
        db.close();
    }

    public void recreateTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

}