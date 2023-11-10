package com.example.fitnessapp2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "fitnessapp2.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, last_name TEXT, email TEXT, password TEXT, phone TEXT, dob TEXT, age INTEGER, gender TEXT, height INTEGER, weight REAL, profile_picture TEXT, remember_me INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE IF NOT EXISTS workout_logs (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, exercise TEXT, sets INTEGER, reps INTEGER, weight REAL, date TEXT, FOREIGN KEY(user_id) REFERENCES users(id))");
        db.execSQL("CREATE TABLE IF NOT EXISTS meal_logs (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, meal TEXT, fats DOUBLE, carbs DOUBLE, protein DOUBLE, date TEXT, FOREIGN KEY(user_id) REFERENCES users(id))");
    }

    //called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + "users" + " ADD COLUMN remember_me INTEGER DEFAULT 0");
        }
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