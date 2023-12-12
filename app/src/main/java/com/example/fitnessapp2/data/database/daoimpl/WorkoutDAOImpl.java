package com.example.fitnessapp2.data.database.daoimpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fitnessapp2.data.model.WorkoutLog;
import com.example.fitnessapp2.data.database.DatabaseHelper;
import com.example.fitnessapp2.data.database.dao.WorkoutDAO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutDAOImpl implements WorkoutDAO {
    private DatabaseHelper databaseHelper;
    private Gson gson;

    public WorkoutDAOImpl(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        this.gson = new Gson();
    }

    @Override
    public boolean insertWorkoutLog(WorkoutLog log) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", log.getUserId());
        contentValues.put("workout_type", log.getWorkoutType());
        contentValues.put("date", log.getDate());
        Log.d("WorkoutDAOImpl", "Inserting log with date: " + log.getDate());
        contentValues.put("workout_details", gson.toJson(log.getWorkoutDetails()));

        long result = db.insert("workout_logs", null, contentValues);
        db.close();
        Log.d("WorkoutDAOImpl", "Inserting into DB: " + contentValues.toString());
        return result != -1;

    }

    @Override
    public List<WorkoutLog> getWorkoutLogsByUserId(int userId) {
        List<WorkoutLog> logs = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM workout_logs WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int userIdIndex = cursor.getColumnIndex("user_id");
                int workoutTypeIndex = cursor.getColumnIndex("workout_type");
                int dateIndex = cursor.getColumnIndex("date");
                int detailsIndex = cursor.getColumnIndex("workout_details");

                Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
                Map<String, Object> details = gson.fromJson(cursor.getString(detailsIndex), type);

                WorkoutLog log = new WorkoutLog(
                        cursor.getInt(idIndex),
                        cursor.getInt(userIdIndex),
                        cursor.getString(workoutTypeIndex),
                        cursor.getString(dateIndex),
                        details
                );
                logs.add(log);
            } while (cursor.moveToNext());
        }
        Log.d("WorkoutDAOImpl", "getWorkoutLogsByUserId: fetching logs for user " + userId);
        cursor.close();
        db.close();
        return logs;
    }

    @Override
    public List<WorkoutLog> getWorkoutLogsByUserIdAndDate(int userId, String date) {
        List<WorkoutLog> logs = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM workout_logs WHERE user_id = ? AND date = ?", new String[]{String.valueOf(userId), date});
        Log.d("WorkoutDAOImpl", "Fetching logs for date: " + date);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int userIdIndex = cursor.getColumnIndex("user_id");
                int workoutTypeIndex = cursor.getColumnIndex("workout_type");
                int dateIndex = cursor.getColumnIndex("date");
                int detailsIndex = cursor.getColumnIndex("workout_details");

                Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
                Map<String, Object> details = gson.fromJson(cursor.getString(detailsIndex), type);

                WorkoutLog log = new WorkoutLog(
                        cursor.getInt(idIndex),
                        cursor.getInt(userIdIndex),
                        cursor.getString(workoutTypeIndex),
                        cursor.getString(dateIndex),
                        details
                );
                logs.add(log);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return logs;
    }

    @Override
    public boolean updateWorkoutLog(WorkoutLog log) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("workout_type", log.getWorkoutType());
        contentValues.put("date", log.getDate());
        contentValues.put("workout_details", gson.toJson(log.getWorkoutDetails()));

        int result = db.update("workout_logs", contentValues, "id = ?", new String[]{String.valueOf(log.getId())});
        db.close();
        return result > 0;
    }

    @Override
    public void deleteWorkoutLog(int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete("workout_logs", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}