package com.example.fitnessapp2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class WorkoutDAOImpl implements WorkoutDAO {
    private DatabaseHelper databaseHelper;

    // Constructor to initialize the database helper object

    public WorkoutDAOImpl(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    // Method to insert a new workout log into the database
    @Override
    public boolean insertWorkoutLog(WorkoutLog log) {
        // Get the database in write mode
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", log.getUserId());
        contentValues.put("exercise", log.getExercise());
        contentValues.put("sets", log.getSets());
        contentValues.put("reps", log.getReps());
        contentValues.put("weight", log.getWeight());
        contentValues.put("date", log.getDate());

        // Insert the new row, returning the primary key value of the new row
        long result = db.insert("workout_logs", null, contentValues);

        // Close the database connection
        db.close();

        // Return true if insertion was successful, false otherwise
        return result != -1;
    }

    // Method to fetch all workout logs for a specific user
    @Override
    public List<WorkoutLog> getWorkoutLogsByUserId(int userId) {
        List<WorkoutLog> logs = new ArrayList<>();

        // Get the database in read mode
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Execute the query to select all logs for a given user
        Cursor cursor = db.rawQuery("SELECT * FROM workout_logs WHERE user_id = ?", new String[]{String.valueOf(userId)});

        // Get the index for each column
        int idIndex = cursor.getColumnIndex("id");
        int userIdIndex = cursor.getColumnIndex("user_id");
        int exerciseIndex = cursor.getColumnIndex("exercise");
        int setsIndex = cursor.getColumnIndex("sets");
        int repsIndex = cursor.getColumnIndex("reps");
        int weightIndex = cursor.getColumnIndex("weight");
        int dateIndex = cursor.getColumnIndex("date");

        // Check if any of the column indexes are -1
        if (idIndex == -1 || userIdIndex == -1 || exerciseIndex == -1 || setsIndex == -1 || repsIndex == -1 || weightIndex == -1 || dateIndex == -1) {
            // Handle the error properly
            cursor.close();
            db.close();
            return logs; // Return an empty list or throw an exception
        }

        // Move to the first row of results & iterate over all rows
        if (cursor.moveToFirst()) {
            do {
                // Create a new WorkoutLog object and populate its fields from the current row in the cursor
                WorkoutLog log = new WorkoutLog(
                        cursor.getInt(idIndex),
                        cursor.getInt(userIdIndex),
                        cursor.getString(exerciseIndex),
                        cursor.getInt(setsIndex),
                        cursor.getInt(repsIndex),
                        cursor.getDouble(weightIndex),
                        cursor.getString(dateIndex)
                );
                // Add the log to the list of logs
                logs.add(log);
            } while (cursor.moveToNext()); // Move to the next row
        }
        // Close the cursor and database connection
        cursor.close();
        db.close();

        // Return the list of workout logs
        return logs;
    }


    // Method to update a workout log in the database
    @Override
    public boolean updateWorkoutLog(WorkoutLog log) {
        // Get the database in write mode
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put("exercise", log.getExercise());
        contentValues.put("sets", log.getSets());
        contentValues.put("reps", log.getReps());
        contentValues.put("weight", log.getWeight());
        contentValues.put("date", log.getDate());

        // Update the log in the database for the specified ID
        int result = db.update("workout_logs", contentValues, "id = ?", new String[]{String.valueOf(log.getId())});

        // Close the database connection
        db.close();

        // Return true if the update was successful, false otherwise
        return result > 0;
    }

    // Method to delete a workout log from the database
    @Override
    public void deleteWorkoutLog(int id) {
        // Get the database in write mode
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Delete the log with the specified ID
        db.delete("workout_logs", "id = ?", new String[]{String.valueOf(id)});

        // Close the database connection
        db.close();
    }
}
