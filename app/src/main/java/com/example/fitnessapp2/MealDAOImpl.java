package com.example.fitnessapp2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MealDAOImpl implements MealDAO {
    private DatabaseHelper databaseHelper;

    // Constructor to initialize the database helper object
    public MealDAOImpl(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    // Method to insert a new meal log into the database
    @Override
    public boolean insertMealLog(MealLog log) {
        // Get the database in write mode
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", log.getUserId());
        contentValues.put("meal", log.getMeal());
        contentValues.put("fats", log.getFats());
        contentValues.put("carbs", log.getCarbs());
        contentValues.put("protein", log.getProtein());
        contentValues.put("date", log.getDate());

        // Insert the new row, returning the primary key value of the new row
        long result = db.insert("meal_logs", null, contentValues);

        // Close the database connection
        db.close();

        // Return true if insertion was successful, false otherwise
        return result != -1;
    }

    @Override
    public List<MealLog> getMealLogsByUserId(int userId) {
        List<MealLog> logs = new ArrayList<>();

        // Get the database in read mode
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Execute the query to select all logs for a given user
        Cursor cursor = db.rawQuery("SELECT * FROM meal_logs WHERE user_id = ?", new String[]{String.valueOf(userId)});

        // Get the index for each column
        int idIndex = cursor.getColumnIndex("id");
        int userIdIndex = cursor.getColumnIndex("user_id");
        int mealIndex = cursor.getColumnIndex("meal");
        int fatsIndex = cursor.getColumnIndex("fats");
        int carbsIndex = cursor.getColumnIndex("carbs");
        int proteinIndex = cursor.getColumnIndex("protein");
        int dateIndex = cursor.getColumnIndex("date");

        // Check if any of the column indexes are -1
        if (idIndex == -1 || userIdIndex == -1 || mealIndex == -1 || fatsIndex == -1 || carbsIndex == -1 || proteinIndex == -1 || dateIndex == -1) {
            // Handle the error properly
            cursor.close();
            db.close();
            return logs; // Return an empty list or throw an exception
        }

        // Move to the first row of results & iterate over all rows
        if (cursor.moveToFirst()) {
            do {
                // Create a new MealLog object and populate its fields from the current row in the cursor
                MealLog log = new MealLog(
                        cursor.getInt(idIndex),
                        cursor.getInt(userIdIndex),
                        cursor.getString(mealIndex),
                        cursor.getDouble(fatsIndex),
                        cursor.getDouble(carbsIndex),
                        cursor.getDouble(proteinIndex),
                        cursor.getString(dateIndex)
                );
                // Add the log to the list of logs
                logs.add(log);
            } while (cursor.moveToNext()); // Move to the next row
        }
        // Close the cursor and database connection
        cursor.close();
        db.close();

        // Return the list of meal logs
        return logs;
    }


    // Method to update a meal log in the database
    @Override
    public boolean updateMealLog(MealLog log) {
        // Get the database in write mode
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put("meal", log.getMeal());
        contentValues.put("fats", log.getFats());
        contentValues.put("carbs", log.getCarbs());
        contentValues.put("protein", log.getProtein());
        contentValues.put("date", log.getDate());

        // Update the log in the database for the specified ID
        int result = db.update("meal_logs", contentValues, "id = ?", new String[]{String.valueOf(log.getId())});

        // Close the database connection
        db.close();

        // Return true if the update was successful, false otherwise
        return result > 0;
    }

    // Method to delete a meal log from the database
    @Override
    public void deleteMealLog(int id) {
        // Get the database in write mode
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Delete the log with the specified ID
        db.delete("meal_logs", "id = ?", new String[]{String.valueOf(id)});

        // Close the database connection
        db.close();
    }
}