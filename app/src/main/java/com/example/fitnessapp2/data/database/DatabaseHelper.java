package com.example.fitnessapp2.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
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

        // Creating new table for exercises
        String CREATE_EXERCISES_TABLE = "CREATE TABLE exercises (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "muscle TEXT," +
                "equipment TEXT," +
                "difficulty TEXT," +
                "instructions TEXT," +
                "media_path TEXT)";
        db.execSQL(CREATE_EXERCISES_TABLE);
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

    public boolean isExerciseDataSeeded() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM exercises", null);
        boolean hasData = false;
        if (cursor.moveToFirst()) {
            hasData = cursor.getInt(0) > 0;
        }
        cursor.close();
        db.close();
        return hasData;
    }

    public void seedExerciseData() {
        if (isExerciseDataSeeded()) return;

        SQLiteDatabase db = this.getWritableDatabase();

        insertExercise(db, "Barbell Curl", "biceps", "barbell", "beginner", "Stand with barbell, curl up and lower slowly.", "android.resource://com.example.fitnessapp2/raw/barbell_curl");
        insertExercise(db, "Triceps Dip", "triceps", "bodyweight", "beginner", "Lower your body between parallel bars and press back up.", "android.resource://com.example.fitnessapp2/raw/triceps_dip");
        insertExercise(db, "Bench Press", "chest", "barbell", "intermediate", "Lower the bar to chest and press up.", "android.resource://com.example.fitnessapp2/raw/dumbbell_bench_press");
        insertExercise(db, "Pullups", "back", "bodyweight", "intermediate", "Pull yourself up until chin is over the bar.", "android.resource://com.example.fitnessapp2/raw/pullups");
        insertExercise(db, "Shoulder Press", "shoulders", "dumbbell", "intermediate", "Press dumbbells overhead and lower slowly.", "android.resource://com.example.fitnessapp2/raw/shoulder_press");
        insertExercise(db, "Squats", "legs", "barbell", "intermediate", "Lower down by bending knees, then stand back up.", "android.resource://com.example.fitnessapp2/raw/squats");

        db.close();
    }

    private void insertExercise(SQLiteDatabase db, String name, String muscle, String equipment, String difficulty, String instructions, String mediaPath) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("muscle", muscle);
        values.put("equipment", equipment);
        values.put("difficulty", difficulty);
        values.put("instructions", instructions);
        values.put("media_path", mediaPath);
        db.insert("exercises", null, values);
    }

}